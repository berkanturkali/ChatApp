package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.MessageFromItemLayoutBinding
import com.example.chatapp.databinding.MessageToItemLayoutBinding
import com.example.chatapp.model.Message
import com.example.chatapp.utils.StorageManager

class MessagesAdapter(val storageManager: StorageManager) :
    ListAdapter<Message, RecyclerView.ViewHolder>(MESSAGE_COMPARATOR) {

    companion object {
        val MESSAGE_COMPARATOR = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem.message == newItem.message

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem == newItem
        }
        const val RECEIVER_TYPE = 0
        const val SENDER_TYPE = 1
    }

    inner class ReceiverViewHolder(private val binding: MessageFromItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.apply {
                textGchatDateOther.visibility = View.GONE
                textGchatUserOther.text = storageManager.getFullname()
                textGchatMessageOther.text = message.message
            }
        }
    }

    inner class SenderViewHolder(private val binding: MessageToItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.apply {
                textGchatDateMe.visibility = View.GONE
                textGchatMessageMe.text = message.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RECEIVER_TYPE -> ReceiverViewHolder(
                MessageFromItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SENDER_TYPE -> SenderViewHolder(
                MessageToItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Exception("No viewtype")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == RECEIVER_TYPE) {
            (holder as ReceiverViewHolder).bind(getItem(position))
        } else {
            (holder as SenderViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).sender == storageManager.getEmail()) {
            SENDER_TYPE
        } else {
            RECEIVER_TYPE
        }
    }
}
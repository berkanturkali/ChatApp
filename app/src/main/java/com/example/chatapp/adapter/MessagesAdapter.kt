package com.example.chatapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.JoinItemLayoutBinding
import com.example.chatapp.databinding.MessageFromItemLayoutBinding
import com.example.chatapp.databinding.MessageToItemLayoutBinding
import com.example.chatapp.model.Message
import com.example.chatapp.utils.*
import java.util.*

class MessagesAdapter(val storageManager: StorageManager) :
    ListAdapter<Message, RecyclerView.ViewHolder>(MESSAGE_COMPARATOR) {

    companion object {
        val MESSAGE_COMPARATOR = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return (oldItem is Message.TextMessage && newItem is Message.TextMessage &&
                        oldItem.message == newItem.message) ||
                        (oldItem is Message.LogMessage && newItem is Message.LogMessage &&
                                oldItem.type == newItem.type)
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem == newItem
        }
        const val RECEIVER_TYPE = 0
        const val SENDER_TYPE = 1
        const val LOG_TYPE = 2
    }

    inner class ReceiverViewHolder(private val binding: MessageFromItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message.TextMessage) {
            binding.apply {
                val date: String = when {
                    isToday(message.createdAt) -> {
                        "Today"
                    }
                    isYesterday(message.createdAt) -> {
                        "Yesterday"
                    }
                    else -> {
                        message.createdAt.getDate(Constants.DATE_PATTERN)
                    }
                }
                val calender = Calendar.getInstance()
                calender.timeInMillis = message.createdAt
                val mDate = calender.get(Calendar.DAY_OF_MONTH)
                if (bindingAdapterPosition > 0) {
                    if (getItem(bindingAdapterPosition - 1) !is Message.TextMessage
                        && bindingAdapterPosition - 1 == 0
                    ) {
                        textGchatDateOther.visibility = View.VISIBLE
                    } else {
                        var position = bindingAdapterPosition
                        do {
                            position -= 1
                        } while (getItem(position) !is Message.TextMessage)
                        val previousMessage =
                            getItem(position) as Message.TextMessage
                        calender.timeInMillis = (previousMessage.createdAt)
                        val prevDate = calender.get(Calendar.DAY_OF_MONTH)
                        if (mDate > prevDate) {
                            textGchatDateOther.visibility = View.VISIBLE
                        } else {
                            textGchatDateOther.visibility = View.GONE
                        }
                    }
                } else {
                    textGchatDateOther.visibility = View.VISIBLE
                }
                textGchatDateOther.text = date
                textGchatUserOther.text = message.sender
                textGchatMessageOther.text = message.message
                textGchatTimestampOther.text = message.createdAt.getDate()
            }
        }
    }

    inner class SenderViewHolder(private val binding: MessageToItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message.TextMessage) {
            binding.apply {
                binding.apply {
                    val date: String = when {
                        isToday(message.createdAt) -> {
                            "Today"
                        }
                        isYesterday(message.createdAt) -> {
                            "Yesterday"
                        }
                        else -> {
                            message.createdAt.getDate(Constants.DATE_PATTERN)
                        }
                    }
                    val calender = Calendar.getInstance()
                    calender.timeInMillis = message.createdAt
                    val mDate = calender.get(Calendar.DAY_OF_MONTH)
                    if (bindingAdapterPosition > 0) {
                        if (getItem(bindingAdapterPosition - 1) !is Message.TextMessage
                            && bindingAdapterPosition - 1 == 0
                        ) {
                            textGchatDateMe.visibility = View.VISIBLE
                        } else {
                            var position = bindingAdapterPosition
                            do {
                                position -= 1
                            } while (getItem(position) !is Message.TextMessage)
                            val previousMessage =
                                getItem(position) as Message.TextMessage
                            calender.timeInMillis = (previousMessage.createdAt)
                            val prevDate = calender.get(Calendar.DAY_OF_MONTH)
                            if (mDate > prevDate) {
                                textGchatDateMe.visibility = View.VISIBLE
                            } else {
                                textGchatDateMe.visibility = View.GONE
                            }
                        }
                    } else {
                        textGchatDateMe.visibility = View.VISIBLE
                    }
                    textGchatMessageMe.setLinkTextColor(Color.WHITE)
                    textGchatDateMe.text = date
                    textGchatMessageMe.text = message.message
                    textGchatTimestampMe.text = message.createdAt.getDate()
                }
            }
        }
    }

    inner class JoinViewHolder(private val binding: JoinItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(join: Message.LogMessage) {
            binding.apply {
                joinedTv.text = join.content
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
            LOG_TYPE -> JoinViewHolder(
                JoinItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Exception("No viewtype")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        message?.let {
            when (it) {
                is Message.TextMessage -> {
                    val viewType = getItemViewType(position)
                    if (viewType == RECEIVER_TYPE) {
                        (holder as ReceiverViewHolder).bind(it)
                    } else {
                        (holder as SenderViewHolder).bind(it)
                    }
                }
                is Message.LogMessage -> {
                    if (it.type == "join" || it.type == "leave") {
                        (holder as JoinViewHolder).bind(it)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Message.TextMessage -> {
                if ((getItem(position) as Message.TextMessage).sender == storageManager.getFullname()) {
                    SENDER_TYPE
                } else {
                    RECEIVER_TYPE
                }
            }
            else -> {
                LOG_TYPE
            }
        }
    }
}
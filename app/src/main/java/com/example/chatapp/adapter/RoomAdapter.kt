package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.RoomItemLayoutBinding
import com.example.chatapp.model.Room

class RoomAdapter(private val onClickListener: OnRoomClick) :
    ListAdapter<Room, RoomAdapter.RoomViewHolder>(ROOM_COMPARATOR) {

    companion object {
        val ROOM_COMPARATOR = object : DiffUtil.ItemCallback<Room>() {
            override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder =
        RoomViewHolder(
            RoomItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item) }
    }

    inner class RoomViewHolder(private val binding: RoomItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    item?.let {
                        onClickListener.onRoomItemClick(it)
                    }
                }
            }
        }

        fun bind(room: Room) {
            binding.apply {
                roomTv.text = room.name
            }
        }
    }

    interface OnRoomClick {
        fun onRoomItemClick(room: Room)
    }
}


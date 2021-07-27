package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.databinding.UserItemLayoutBinding
import com.example.chatapp.model.User

class UserAdapter : PagingDataAdapter<User, UserAdapter.UserViewHolder>(USER_COMPARATOR) {

    private lateinit var clickListener: OnClickListener

    fun setClickListener(listener: OnClickListener) {
        clickListener = listener
    }

    companion object {
        val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.email == newItem.email

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            UserItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(currentItem)
        }
    }

    inner class UserViewHolder(private val binding: UserItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    item?.let {
                        clickListener.onClick(it)
                    }
                }
            }
        }

        fun bind(user: User) {
            binding.apply {
                userNameTv.text = String.format(
                    root.context.resources.getString(R.string.full_name),
                    user.firstname,
                    user.lastname
                )
                aboutTv.text = user.aboutMe
            }
        }
    }

    interface OnClickListener {
        fun onClick(user: User)
    }
}
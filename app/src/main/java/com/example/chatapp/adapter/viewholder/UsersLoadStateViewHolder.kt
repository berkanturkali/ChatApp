package com.example.chatapp.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.databinding.UsersLoadStateFooterViewItemLayoutBinding

class UsersLoadStateViewHolder(
    private val binding: UsersLoadStateFooterViewItemLayoutBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.apply {
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): UsersLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.users_load_state_footer_view_item_layout, parent, false)
            val binding = UsersLoadStateFooterViewItemLayoutBinding.bind(view)
            return UsersLoadStateViewHolder(binding, retry)
        }
    }
}
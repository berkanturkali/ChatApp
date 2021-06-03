package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentMessagesLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMessages: Fragment(R.layout.fragment_messages_layout) {

    private var _binding:FragmentMessagesLayoutBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMessagesLayoutBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
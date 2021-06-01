package com.example.chatapp.view.authflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentAuthMainLayoutBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAuthMain : Fragment(R.layout.fragment_auth_main_layout) {

    private var _binding: FragmentAuthMainLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAuthMainLayoutBinding.bind(view)
        initButtons()
    }

    private fun initButtons() {
        binding.apply {
            signupBtn.setOnClickListener {

            }
            loginBtn.setOnClickListener {

            }
        }
    }

    private fun navigateToDest(id: Int) {
        findNavController().navigate(id)
    }
}
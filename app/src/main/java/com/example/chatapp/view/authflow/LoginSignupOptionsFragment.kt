package com.example.chatapp.view.authflow

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginSignupOptionsBinding
import com.example.chatapp.view.BaseFragment

class LoginSignupOptionsFragment :
    BaseFragment<FragmentLoginSignupOptionsBinding>(FragmentLoginSignupOptionsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        binding.apply {
            signupBtn.setOnClickListener {
                navigateToDest(R.id.action_loginSignupOptionsFragment_to_signupFragment)
            }
            loginBtn.setOnClickListener {
                navigateToDest(R.id.action_fragmentAuthMain_to_fragmentLogin)
            }
        }
    }

    private fun navigateToDest(id: Int) {
        findNavController().navigate(id)
    }
}
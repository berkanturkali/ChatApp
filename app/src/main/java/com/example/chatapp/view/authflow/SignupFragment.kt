package com.example.chatapp.view.authflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignupBinding
import com.example.chatapp.model.User
import com.example.chatapp.utils.showSnack
import com.example.chatapp.utils.text
import com.example.chatapp.view.BaseFragment
import com.example.chatapp.viewmodel.authflow.SignupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val mViewModel by viewModels<SignupFragmentViewModel>()
    private var email: String = ""
    private var password: String = ""
    private var fn: String = ""
    private var ln: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        subscribeObservers()
    }

    private fun initButtons() {
        binding.apply {
            signupBtn.setOnClickListener {
                email = emailEt.text()
                password = pwEt.text()
                fn = fnEt.text()
                ln = lnEt.text()
                if (mViewModel.isValid(email, password, fn, ln)) signup()
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.signupInfo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                handleResource(
                    resource,
                    successFunc = {
                        showSnack(resource.data!!,R.color.colorSuccess)
                        navigateToLoginFragment()
                    },
                    errorFunc = {
                        showSnack(resource.message!!)
                    }
                )
            }
        }
        mViewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showSnack(message)
            }
        }
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
    }

    private fun signup() {
        val user = User(
            email = email,
            password = password,
            firstname = fn,
            lastname = ln,
        )
        mViewModel.signup(user)
    }
}
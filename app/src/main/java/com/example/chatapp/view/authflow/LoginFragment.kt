package com.example.chatapp.view.authflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.utils.showSnack
import com.example.chatapp.utils.trim
import com.example.chatapp.view.BaseFragment
import com.example.chatapp.viewmodel.authflow.LoginFragmentViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val mViewModel by viewModels<LoginFragmentViewModel>()

    private var email = ""
    private var password = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        subscribeObservers()
    }

    private fun initButtons() {
        binding.apply {
            loginBtn.setOnClickListener {
                email = emailEt.trim()
                password = pwEt.trim()
                if (mViewModel.isValid(email, password)) login()
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showSnack(message)
            }
        }
        mViewModel.loginInfo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                handleResource(
                    resource,
                    successFunc = {
                        showSnack(getString(R.string.login_success_message), R.color.colorSuccess)
                        mViewModel.setTokenAndUserInfo(resource.data!!.token,
                            resource.data.email,
                            resource.data.fullname)
                        navigateToHome()
                    },
                    errorFunc = { showSnack(resource.message!!) }
                )
            }
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFlowContainerFragment)
    }

    private fun login() {
        val credentials = JsonObject()
        credentials.addProperty("email", email)
        credentials.addProperty("password", password)
        mViewModel.login(credentials)
    }

}
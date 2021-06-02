package com.example.chatapp.view.authflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginLayoutBinding
import com.example.chatapp.utils.*
import com.example.chatapp.viewmodel.MainActivityViewModel
import com.example.chatapp.viewmodel.authflow.FragmentLoginViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class FragmentLogin : Fragment(R.layout.fragment_login_layout) {

    private var _binding: FragmentLoginLayoutBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel by activityViewModels<MainActivityViewModel>()
    private val mViewModel by viewModels<FragmentLoginViewModel>()

    @Inject
    lateinit var storageManager: StorageManager


    private var email = ""
    private var password = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginLayoutBinding.bind(view)
        initButtons()
        subscribeObservers()
    }

    private fun initButtons() {
        binding.apply {
            loginBtn.setOnClickListener {
                email = emailEt.text.toString().trim()
                password = passwordEt.text.toString().trim()
                mViewModel.validate(email, password)
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.isValid.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isValid ->
                if (isValid) {
                    login()
                } else {
                    binding.apply {
                        emailEt.validate("Valid email is required") { s -> s.isValidEmail() }
                        passwordEt.validate("Valid password is required") { pw -> pw.isValidPassword() }
                    }
                }
            }
        }
        mViewModel.loginInfo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                handleResource(
                    activityViewModel,
                    resource,
                    {
                        storageManager.setTokenAndMail(resource.data!!.token, resource.data.email)
                        binding.root.showSnack(R.color.colorSuccess, "Success")
                    },
                    {
                        binding.root.showSnack(message = resource.message!!)
                    }
                )
            }
        }
    }

    private fun login() {
        val credentials = JsonObject()
        credentials.addProperty("email", email)
        credentials.addProperty("password", password)
        mViewModel.login(credentials)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
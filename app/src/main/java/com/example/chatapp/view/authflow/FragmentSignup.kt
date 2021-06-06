package com.example.chatapp.view.authflow

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignupLayoutBinding

import com.example.chatapp.model.User
import com.example.chatapp.utils.*
import com.example.chatapp.viewmodel.MainActivityViewModel
import com.example.chatapp.viewmodel.authflow.FragmentSignupViewModel
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSignup : Fragment(R.layout.fragment_signup_layout) {

    private val mViewModel by viewModels<FragmentSignupViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()
    private var _binding: FragmentSignupLayoutBinding? = null
    private val binding get() = _binding!!
    private var email: String = ""
    private var password: String = ""
    private var fn: String = ""
    private var ln: String = ""
    private var aboutMe: String = ""
    private var isDeleted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupLayoutBinding.bind(view)

        initButtons()
        initWidgets()
        subscribeObservers()
    }

    private fun initButtons() {
        binding.apply {
            signupBtn.setOnClickListener {
                email = emailEt.text.toString().trim()
                password = passwordEt.text.toString().trim()
                fn = firstNameEt.text.toString().trim()
                ln = lastNameEt.text.toString().trim()
                mViewModel.validate(
                    email, password, fn, ln
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initWidgets() {
        binding.apply {
            interestsInputEt.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (interestsInputEt.text.isNotBlank() && interestsInputEt.text.isNotEmpty()) {
                        isDeleted = false
                        mViewModel.addInterests(interestsInputEt.text.toString().trim())
                        interestsInputEt.setText("")
                    }
                }
                return@setOnKeyListener true
            }
            aboutMeEt.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            aboutMeEt.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            aboutMeEt.isScrollbarFadingEnabled = true
            aboutMeEt.maxLines = 4
            nestedScrollView.setOnTouchListener { _, _ ->
                aboutMeEt.parent.requestDisallowInterceptTouchEvent(false)
                return@setOnTouchListener false
            }
            aboutMeEt.setOnTouchListener { _, _ ->
                aboutMeEt.parent.requestDisallowInterceptTouchEvent(true)
                return@setOnTouchListener false
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.isValid.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isValid ->
                if (isValid) {
                    signup()
                } else {
                    binding.apply {
                        emailEt.validate("Valid email address required") { s -> s.isValidEmail() }
                        passwordEt.validate("Password length should greater than six chars") { pw -> pw.isValidPassword() }
                        firstNameEt.validate("Firstname can not be empty") { fn -> fn.isValid() }
                        lastNameEt.validate("Lastname can not be empty") { ln -> ln.isValid() }
                    }
                }
            }
        }
        mViewModel.signupInfo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                handleResource(
                    activityViewModel,
                    resource,
                    {
                        binding.root.showSnack(R.color.colorSuccess, resource.data!!)
                    },
                    {
                        binding.root.showSnack(R.color.colorDanger, resource.message!!)
                    }
                )
            }
        }
        mViewModel.interests.observe(viewLifecycleOwner) {
            if (it.isNotEmpty() && !isDeleted) {
                createNewChip(it.last(), binding.interestsGroupFL)
            }
        }
    }

    private fun createNewChip(interest: String, group: FlexboxLayout) {
        val chip = layoutInflater.inflate(R.layout.my_chip, group, false) as Chip
        chip.text = interest
        chip.closeIconTint = ContextCompat.getColorStateList(requireContext(), R.color.white)
        chip.isCloseIconVisible = true
        chip.isCheckable = false
        chip.isClickable = true
        group.addView(chip, group.childCount - 1)
        chip.setOnCloseIconClickListener {
            isDeleted = true
            group.removeView(chip)
            mViewModel.removeInterest(chip.text.toString())
        }
    }

    private fun signup() {
        aboutMe = binding.aboutMeEt.text.toString().trim()
        val interests = mViewModel.interests.value
        val user = User(
            email = email,
            password = password,
            firstname = fn,
            lastname = ln,
            interests = interests ?: mutableListOf(),
            aboutMe = aboutMe
        )
        mViewModel.signup(user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
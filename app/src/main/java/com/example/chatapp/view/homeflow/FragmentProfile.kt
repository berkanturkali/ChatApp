package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentProfileLayoutBinding
import com.example.chatapp.model.User
import com.example.chatapp.utils.handleResource
import com.example.chatapp.utils.showSnack
import com.example.chatapp.viewmodel.homeflow.FragmentHomeMainViewModel
import com.google.android.material.chip.Chip

class FragmentProfile : Fragment(R.layout.fragment_profile_layout) {
    private var _binding: FragmentProfileLayoutBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<FragmentHomeMainViewModel>(ownerProducer = { requireParentFragment().requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileLayoutBinding.bind(view)
        binding.cardView.setBackgroundResource(R.drawable.card_view_bg)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        homeViewModel.me.observe(viewLifecycleOwner) {
            handleResource(
                resource = it,
                successFunc = {
                    setFields(it.data!!)
                },
                errorFunc = {
                    binding.root.showSnack(R.color.colorDanger, it.message!!)
                }
            )
        }
    }

    private fun setFields(user: User) {
        binding.apply {
            userNameTv.text = String.format(
                resources.getString(R.string.full_name),
                user.firstname,
                user.lastname
            )
            emailTv.text = user.email
            aboutMeTv.text = user.aboutMe
            createChips(user.interests)
        }
    }

    private fun createChips(interests: MutableList<String>) {
        if (interests.isNotEmpty()) {
            binding.interestsFlexBox.removeAllViews()
            interests.forEach { interest ->
                val chip = layoutInflater.inflate(
                    R.layout.my_chip,
                    binding.interestsFlexBox,
                    false
                ) as Chip
                chip.text = interest
                chip.isCheckable = false
                chip.isClickable = false
                binding.interestsFlexBox.addView(chip, binding.interestsFlexBox.childCount - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
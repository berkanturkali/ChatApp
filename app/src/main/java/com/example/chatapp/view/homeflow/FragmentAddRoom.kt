package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentAddRoomLayoutBinding
import com.example.chatapp.model.Room
import com.example.chatapp.utils.handleResource
import com.example.chatapp.utils.isValidEmail
import com.example.chatapp.utils.showSnack
import com.example.chatapp.utils.validate
import com.example.chatapp.viewmodel.homeflow.FragmentAddRoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAddRoom : Fragment(R.layout.fragment_add_room_layout) {

    private var room: String = ""

    private var _binding: FragmentAddRoomLayoutBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: FragmentAddRoomViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddRoomLayoutBinding.bind(view)
        initButtons()
        subscribeObservers()
    }

    private fun initButtons() {
        binding.apply {
            addRoomBtn.setOnClickListener {
                room = roomEt.text.toString().trim()
                mViewModel.validate(room)
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.isValid.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isValid ->
                if (isValid) {
                    mViewModel.addRoom(Room(room))
                } else {
                    binding.apply {
                        roomEt.validate("Valid room name is required") { s -> s.isValidEmail() }
                    }
                }
            }
        }
        mViewModel.roomInfo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                handleResource(
                    resource = resource,
                    successFunc = {
                        binding.root.showSnack(R.color.colorSuccess, resource.data!!)
                    },
                    errorFunc = {
                        binding.root.showSnack(R.color.colorDanger, resource.message!!)
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
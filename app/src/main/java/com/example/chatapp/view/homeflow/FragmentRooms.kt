package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.adapter.RoomAdapter
import com.example.chatapp.databinding.FragmentRoomsLayoutBinding
import com.example.chatapp.model.Room
import com.example.chatapp.utils.handleResource
import com.example.chatapp.utils.showSnack
import com.example.chatapp.viewmodel.homeflow.FragmentRoomsViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FragmentChats"

@AndroidEntryPoint
class FragmentRooms : Fragment(R.layout.fragment_rooms_layout), RoomAdapter.OnRoomClick {

    private var _binding: FragmentRoomsLayoutBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: FragmentRoomsViewModel by viewModels()
    private lateinit var roomAdapter: RoomAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRoomsLayoutBinding.bind(view)
        mViewModel.getRooms()
        initRecycler()
        subscribeObservers()
    }

    private fun initRecycler() {
        roomAdapter = RoomAdapter(this)
        binding.roomRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = roomAdapter
            setHasFixedSize(true)
        }
    }

    private fun subscribeObservers() {
        mViewModel.rooms.observe(viewLifecycleOwner) {
            handleResource(
                resource = it,
                successFunc = {
                    roomAdapter.submitList(it.data!!)
                },
                errorFunc = {
                    binding.root.showSnack(R.color.colorDanger, it.message!!)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRoomItemClick(room: Room) {
        val action = FragmentRoomsDirections.actionFragmentRoomsToFragmentMessages(room = room.name)
        findNavController().navigate(action)
    }
}
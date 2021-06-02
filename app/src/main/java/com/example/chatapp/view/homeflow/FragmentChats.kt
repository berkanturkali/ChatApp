package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chatapp.R
import com.example.chatapp.adapter.RoomAdapter
import com.example.chatapp.databinding.FragmentChatsLayoutBinding
import com.example.chatapp.utils.handleResource
import com.example.chatapp.utils.showSnack
import com.example.chatapp.viewmodel.homeflow.FragmentChatsViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FragmentChats"

@AndroidEntryPoint
class FragmentChats : Fragment(R.layout.fragment_chats_layout) {

    private var _binding: FragmentChatsLayoutBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: FragmentChatsViewModel by viewModels()
    private lateinit var mAdapter: RoomAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatsLayoutBinding.bind(view)
        mViewModel.getRooms()
        subscribeObservers()


    }

    private fun initRecycler() {
        mAdapter = RoomAdapter()
        binding.roomRv.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
            setHasFixedSize(true)
        }
    }

    private fun subscribeObservers() {
        mViewModel.rooms.observe(viewLifecycleOwner) {
            handleResource(
                resource = it,
                successFunc = {
                    initRecycler()
                    mAdapter.submitList(it.data!!)
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
}
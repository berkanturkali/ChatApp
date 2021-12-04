package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.adapter.RoomAdapter
import com.example.chatapp.databinding.FragmentRoomsBinding
import com.example.chatapp.model.Room
import com.example.chatapp.utils.ItemClickListener
import com.example.chatapp.utils.showSnack
import com.example.chatapp.view.BaseFragment
import com.example.chatapp.viewmodel.homeflow.RoomsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate),
    ItemClickListener<Room> {

    private val mFragmentViewModel: RoomsFragmentViewModel by viewModels()

    @Inject
    lateinit var roomAdapter: RoomAdapter

    @Inject
    lateinit var socket: Socket

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        subscribeObservers()
        binding.swipeRefresh.setOnRefreshListener {
            mFragmentViewModel.getRooms()
        }
        binding.addRoomFab.setOnClickListener {
            findNavController().navigate(R.id.action_roomsFragment_to_addRoomFragment)
        }
    }

    private fun initRecycler() {
        roomAdapter.setListener(this)
        binding.roomRv.apply {
            adapter = roomAdapter
            setHasFixedSize(true)
        }
    }

    private fun subscribeObservers() {
        mFragmentViewModel.rooms.observe(viewLifecycleOwner) {
            handleResource(
                resource = it,
                successFunc = {
                    showEmptyView(it.data!!.isEmpty())
                    roomAdapter.submitList(it.data)
                    binding.swipeRefresh.isRefreshing = false
                },
                errorFunc = {
                    showSnack(it.message!!)
                    binding.swipeRefresh.isRefreshing = false
                }
            )
        }

        socket.on("update") {
            val update = it[0].toString().toBoolean()
            if (update) mFragmentViewModel.getRooms()
        }
    }

    private fun showEmptyView(empty: Boolean) {
        binding.emptyView.visibility = if (empty) View.VISIBLE else View.GONE
        binding.roomRv.visibility = if (empty) View.GONE else View.VISIBLE
    }

    override fun onItemClick(item: Room) {
        val action = RoomsFragmentDirections.actionFragmentRoomsToFragmentMessages(item.name)
        findNavController().navigate(action)
    }
}
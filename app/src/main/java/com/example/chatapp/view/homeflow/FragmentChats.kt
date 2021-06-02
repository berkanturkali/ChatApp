package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.adapter.MessagesAdapter
import com.example.chatapp.adapter.RoomAdapter
import com.example.chatapp.databinding.FragmentChatsLayoutBinding
import com.example.chatapp.model.Message
import com.example.chatapp.utils.StorageManager
import com.example.chatapp.utils.handleResource
import com.example.chatapp.utils.showSnack
import com.example.chatapp.viewmodel.homeflow.FragmentChatsViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import javax.inject.Inject

private const val TAG = "FragmentChats"

@AndroidEntryPoint
class FragmentChats : Fragment(R.layout.fragment_chats_layout) {

    private var _binding: FragmentChatsLayoutBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: FragmentChatsViewModel by viewModels()
    private lateinit var roomAdapter: RoomAdapter

    private val messages = mutableListOf<Message>()

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var storageManager: StorageManager
    private var room = ""
    private val gson = Gson()
    private lateinit var messagesAdapter: MessagesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatsLayoutBinding.bind(view)
        mViewModel.getRooms()
        subscribeObservers()
        listenMessages()
        initButtons()
        initMessageRv()
    }

    private fun initMessageRv() {
        messagesAdapter = MessagesAdapter(storageManager)
        binding.messageRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messagesAdapter
            setHasFixedSize(true)
            (layoutManager as LinearLayoutManager).stackFromEnd = true
        }
    }

    private fun initRecycler() {
        roomAdapter = RoomAdapter()
        binding.roomRv.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            adapter = roomAdapter
            setHasFixedSize(true)
        }
    }

    private fun initButtons() {
        binding.sendMsgBtn.setOnClickListener {
            val message =
                Message(binding.messageEt.text.toString().trim(), storageManager.getEmail()!!, room)
            socket.emit("messageToServer", gson.toJson(message))
            binding.messageEt.setText("")
        }
    }

    private fun subscribeObservers() {
        mViewModel.rooms.observe(viewLifecycleOwner) {
            handleResource(
                resource = it,
                successFunc = {
                    initRecycler()
                    roomAdapter.submitList(it.data!!)
                    joinRoom(it.data[0].name)
                },
                errorFunc = {
                    binding.root.showSnack(R.color.colorDanger, it.message!!)
                }
            )
        }
    }

    private fun listenMessages() {
        socket.on("messageToClient") {
            val receivedMessage = gson.fromJson(it[0].toString(), Message::class.java)
            messages.add(receivedMessage)
            messagesAdapter.submitList(messages)
            messagesAdapter.notifyItemInserted(messages.size - 1)
        }
    }


    private fun joinRoom(room: String) {
        socket.emit("roomToJoin", room)
        socket.on("joinedRoom") {
            this.room = it[0].toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
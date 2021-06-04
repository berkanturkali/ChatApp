package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.adapter.MessagesAdapter
import com.example.chatapp.databinding.FragmentMessagesLayoutBinding
import com.example.chatapp.model.Message
import com.example.chatapp.utils.StorageManager
import com.example.chatapp.utils.handleResource
import com.example.chatapp.utils.showSnack
import com.example.chatapp.viewmodel.homeflow.FragmentMessagesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import java.util.*
import javax.inject.Inject

private const val TAG = "FragmentMessages"

@AndroidEntryPoint
class FragmentMessages : Fragment(R.layout.fragment_messages_layout) {

    private var _binding: FragmentMessagesLayoutBinding? = null
    private val binding get() = _binding!!

    private val args: FragmentMessagesArgs by navArgs()

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var storageManager: StorageManager
    private val gson = Gson()
    private lateinit var messagesAdapter: MessagesAdapter

    private val messages = mutableListOf<Message>()

    private val mViewModel by viewModels<FragmentMessagesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMessagesLayoutBinding.bind(view)
        initMessageRv()
        joinRoom(args.room)
        mViewModel.getHistory(args.room)
        listenMessages()
        initButtons()

        requireParentFragment().requireParentFragment().requireView()
            .findViewById<TextView>(R.id.drawer_toolbar_title).text = args.room
        subscribeObservers()

    }

    private fun subscribeObservers() {
        mViewModel.history.observe(viewLifecycleOwner) {
            handleResource(
                resource = it,
                successFunc = {
                    messages.addAll(it.data!!)
                    messagesAdapter.submitList(messages)
                },
                errorFunc = {
                    binding.root.showSnack(R.color.colorDanger, it.message!!)
                }
            )
        }
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

    private fun initButtons() {
        binding.sendMsgBtn.setOnClickListener {
            val message =
                Message.TextMessage(
                    binding.messageEt.text.toString().trim(),
                    storageManager.getEmail()!!,
                    args.room,
                    Date().time
                )
            socket.emit("messageToServer", gson.toJson(message))
            binding.messageEt.setText("")

        }
    }

    private fun listenMessages() {
        socket.on("messageToClient") {
            val receivedMessage = gson.fromJson(it[0].toString(), Message.TextMessage::class.java)
            messages.add(receivedMessage)
            messagesAdapter.submitList(messages)
            messagesAdapter.notifyItemInserted(messages.size - 1)
            binding.messageRv.post {
                binding.messageRv.scrollToPosition(messagesAdapter.itemCount - 1)
            }
        }
    }


    private fun joinRoom(room: String) {
        socket.emit("roomToJoin", room)
        socket.on("joinEvent") { message ->
            val logMessage =
                gson.fromJson(message[0].toString(), Message.LogMessage::class.java)
            if ("${storageManager.getFullname()} has joined" != logMessage.content) {
                messages.add(logMessage)
                messagesAdapter.submitList(messages)
                messagesAdapter.notifyItemInserted(messages.size - 1)
                binding.messageRv.post {
                    binding.messageRv.scrollToPosition(messagesAdapter.itemCount - 1)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        socket.off("messageToClient")
        socket.off("joinEvent")
        messages.clear()
    }
}
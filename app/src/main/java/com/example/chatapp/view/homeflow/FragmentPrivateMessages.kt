package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
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
import com.example.chatapp.viewmodel.homeflow.FragmentHomeMainViewModel
import com.example.chatapp.viewmodel.homeflow.FragmentPrivateMessagesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentPrivateMessages : Fragment(R.layout.fragment_messages_layout) {

    private var _binding: FragmentMessagesLayoutBinding? = null
    private val binding get() = _binding!!
    private val gson = Gson()
    private lateinit var messagesAdapter: MessagesAdapter

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var storageManager: StorageManager

    private val args: FragmentPrivateMessagesArgs by navArgs()

    private val mViewModel by viewModels<FragmentPrivateMessagesViewModel>()

    private val homeViewModel by viewModels<FragmentHomeMainViewModel>(ownerProducer = { requireParentFragment().requireParentFragment() })

    private val privateMessages = mutableListOf<Message>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMessagesLayoutBinding.bind(view)
        binding.headerLayout.headerContainer.isVisible = false
        initMessageRv()
        joinPrivateRoom(args.room)
        homeViewModel.setReceiver(storageManager.getEmail()!!)
        mViewModel.getHistory(args.room, true)
        initButtons()
        requireParentFragment().requireParentFragment().requireView()
            .findViewById<TextView>(R.id.drawer_toolbar_title).text = ""
        requireParentFragment().requireParentFragment().requireView()
            .findViewById<TextView>(R.id.user_name_tv).text = args.fullName
        subscribeObservers()

    }

    private fun subscribeObservers() {
        mViewModel.history.observe(viewLifecycleOwner) {
            handleResource(
                resource = it,
                successFunc = {
                    privateMessages.addAll(it.data!!)
                    messagesAdapter.submitList(privateMessages)
                },
                errorFunc = {
                    binding.root.showSnack(R.color.colorDanger, it.message!!)
                }
            )
        }
        homeViewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {message ->
                privateMessages.add(message)
                messagesAdapter.submitList(privateMessages)
                messagesAdapter.notifyItemInserted(privateMessages.size - 1)
                binding.messageRv.post {
                    binding.messageRv.scrollToPosition(messagesAdapter.itemCount - 1)
                }
            }
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
            if (binding.messageEt.text.isBlank()) {
                it.showSnack(R.color.colorDanger, "Please type something.")
                return@setOnClickListener
            }

            val message = Message.TextMessage(
                binding.messageEt.text.toString().trim(),
                storageManager.getEmail()!!,
                args.room,
                Date().time,
                receiver = args.room,
                isPrivate = true
            )
            socket.emit("privateMessageToServer", gson.toJson(message))

            binding.messageEt.setText("")
        }
    }

    private fun joinPrivateRoom(room: String) {
        socket.emit("privateRoomToJoin", room)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        homeViewModel.setReceiver(null)
        privateMessages.clear()
    }
}
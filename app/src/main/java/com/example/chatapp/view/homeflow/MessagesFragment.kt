package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatapp.R
import com.example.chatapp.adapter.MessagesAdapter
import com.example.chatapp.databinding.FragmentMessagesBinding
import com.example.chatapp.model.Message
import com.example.chatapp.model.OnItemLongClickListener
import com.example.chatapp.utils.Constants.MESSAGE_ID
import com.example.chatapp.utils.StorageManager
import com.example.chatapp.utils.getNavigationResult
import com.example.chatapp.utils.showSnack
import com.example.chatapp.view.BaseFragment
import com.example.chatapp.viewmodel.homeflow.MessagesFragmentViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

private const val TAG = "MessagesFragment"

@AndroidEntryPoint
class MessagesFragment : BaseFragment<FragmentMessagesBinding>(FragmentMessagesBinding::inflate),
    OnItemLongClickListener<Message.TextMessage> {

    private val args: MessagesFragmentArgs by navArgs()

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var storageManager: StorageManager

    @Inject
    lateinit var gson: Gson

    private lateinit var messagesAdapter: MessagesAdapter

    private val messages = mutableListOf<Message>()
    private lateinit var typingObject: JSONObject

    private val mViewModel by viewModels<MessagesFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerLayout.headerContainer.isVisible = true
        typingObject = JSONObject()
        initMessageRv()
        joinRoom(args.room)
        mViewModel.getHistory(args.room)
        updateMembers()
        listenMessages()
        typingObject.put("room", args.room)
        initButtons()
        subscribeObservers()
        binding.messageEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() || s.toString().isNotBlank()) {
                    typingObject.put("isTyping", true)
                    typingObject.put("user", storageManager.getFullname().toString())
                    socket.emit("typing", typingObject)
                } else {
                    typingObject.put("isTyping", false)
                    typingObject.put("user", storageManager.getFullname().toString())
                    socket.emit("typing", typingObject)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        listenTypingEvent()
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
                    showSnack(it.message!!)
                }
            )
        }
        getNavigationResult<String>(R.id.messagesFragment, MESSAGE_ID) { id ->
            socket.emit("unsend", id)
        }
    }

    private fun listenTypingEvent() {
        socket.on("displayTyping") {
            if (it[0].toString().toBoolean()) {
                var typingUsersList =
                    gson.fromJson<List<String>>(it[1].toString(), List::class.java)
                typingUsersList =
                    mViewModel.extractOwnName(typingUsersList, storageManager.getFullname()!!)
                val typingInfoStringResource = mViewModel.getTypingInfoString(typingUsersList)
                val typingUsers: String = if (typingUsersList.size >= 4) {
                    mViewModel.extractFirstThreeAndReturn(typingUsersList)
                } else {
                    TextUtils.join(", ", typingUsersList)
                }
                binding.headerLayout.typingInfoTv.text =
                    String.format(getString(typingInfoStringResource), typingUsers)
            } else {
                binding.headerLayout.typingInfoTv.text = ""
            }
        }
    }

    private fun updateMembers() {
        socket.on("updateMembers") {
            lifecycleScope.launch(Dispatchers.Main) {
                binding.headerLayout.headerTv.text = it[0].toString()
            }
        }
    }

    private fun initMessageRv() {
        messagesAdapter = MessagesAdapter(storageManager)
        messagesAdapter.setListener(this)
        binding.messageRv.apply {
            adapter = messagesAdapter
            setHasFixedSize(true)
        }
    }

    private fun initButtons() {
        binding.sendMsgBtn.setOnClickListener {
            if (binding.messageEt.text.isBlank()) {
                showSnack("Please type something.")
                return@setOnClickListener
            }

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
            val receivedMessage =
                gson.fromJson(it[0].toString(), Message.TextMessage::class.java)
            messages.add(receivedMessage)
            messagesAdapter.submitList(messages)
            messagesAdapter.notifyItemInserted(messages.size - 1)
            binding.messageRv.post {
                binding.messageRv.scrollToPosition(messagesAdapter.itemCount - 1)
            }
        }
        socket.on("deletedMessageId") {
            val deletedMessageId = it[0].toString()
            if (deletedMessageId != "-1") {
                if (messages.isNotEmpty()) {
                    messages.first { message ->
                        (message is Message.TextMessage && message._id == deletedMessageId)
                    }.apply {
                        val index = messagesAdapter.currentList.indexOf(this)
                        messages.removeAt(index)
                        messagesAdapter.notifyItemRemoved(index)
                    }
                }
            } else {
                showSnack("Something went wrong.")
            }
        }
    }


    private fun joinRoom(room: String) {
        socket.emit("roomToJoin", room)
        socket.on("joinEvent") { message ->
            val logMessage =
                gson.fromJson(message[0].toString(), Message.LogMessage::class.java)
            if ("${storageManager.getFullname()} has joined" != logMessage.content) {
                insertLogMessage(logMessage)
            }
        }
        socket.on("leaveEvent") { message ->
            val logMessage =
                gson.fromJson(message[0].toString(), Message.LogMessage::class.java)
            if ("${storageManager.getFullname()} has left" != logMessage.content) {
                insertLogMessage(logMessage)
            }
        }
    }

    private fun insertLogMessage(message: Message.LogMessage) {
        messages.add(message)
        messagesAdapter.submitList(messages)
        messagesAdapter.notifyItemInserted(messages.size - 1)
        binding.messageRv.post {
            binding.messageRv.scrollToPosition(messagesAdapter.itemCount - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        socket.off("displayTyping")
        socket.emit("leaveEvent", args.room)
        socket.off("messageToClient")
        socket.off("joinEvent")
        socket.off("leaveEvent")
        socket.off("updateMembers")
        socket.off("deletedMessageId")
        messages.clear()
    }

    override fun onItemLongClickListener(item: Message.TextMessage) {
        val action =
            MessagesFragmentDirections.actionMessagesFragmentToUnsendMessageDialog(item._id!!)
        findNavController().navigate(action)
    }
}
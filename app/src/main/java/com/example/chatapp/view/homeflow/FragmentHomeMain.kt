package com.example.chatapp.view.homeflow

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentHomeMainLayoutBinding
import com.example.chatapp.model.Message
import com.example.chatapp.model.User
import com.example.chatapp.utils.*
import com.example.chatapp.utils.Constants.NOTIFICATION_ID
import com.example.chatapp.viewmodel.homeflow.FragmentHomeMainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FragmentHomeMain"

@AndroidEntryPoint
class FragmentHomeMain : Fragment(R.layout.fragment_home_main_layout), OnLogoutClick {

    private var _binding: FragmentHomeMainLayoutBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: FragmentHomeMainViewModel by viewModels()

    private var receiver: String? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var headerView: View
    private val drawerSelectedItemIdKey = "DRAWER_SELECTED_ITEM_ID_KEY"
    private var drawerSelectedItemId = R.id.rooms
    private var title: String? = null

    @Inject
    lateinit var storageManager: StorageManager

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager


    private val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeMainLayoutBinding.bind(view)
        savedInstanceState?.let {
            drawerSelectedItemId = it.getInt(drawerSelectedItemIdKey, drawerSelectedItemId)
        }
        mViewModel.getMe()
        headerView = binding.navView.getHeaderView(0)
        createNotificationChannel()
        setupDrawer()
        setBackPressedHandler()
        connectSocket()
        updateList()
        listenPrivateMessages()
        subscribeObservers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(drawerSelectedItemIdKey, drawerSelectedItemId)
        super.onSaveInstanceState(outState)
    }

    private fun connectSocket() {
        socket.connect()
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit("setUser", storageManager.getFullname(), storageManager.getEmail())
            socket.emit("updateList", true)
        }
    }

    private fun updateList() {
        socket.on("updateList") {
            lifecycleScope.launch(Dispatchers.Main) {
                mViewModel.updateList(it[0].toString().toBoolean())
            }
        }
    }

    private fun listenPrivateMessages() {
        socket.on("privateMessageFromServer") {
            val receivedMessage =
                gson.fromJson(it[0].toString(), Message.TextMessage::class.java)
            if ((receivedMessage.room != receiver || receiver == null) && (receivedMessage.sender != storageManager.getFullname())) {
                displayNotification(receivedMessage)
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    mViewModel.setMessage(receivedMessage)
                }
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.me.observe(viewLifecycleOwner) {
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
        mViewModel.receiver.observe(viewLifecycleOwner) {
            receiver = it
        }
    }

    private fun displayNotification(receivedMessage: Message.TextMessage) {
        notificationBuilder.setContentTitle(receivedMessage.room)
            .setContentText(receivedMessage.message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val person = Person.Builder().setName(receivedMessage.sender).build()


        NotificationCompat.MessagingStyle(person)
            .addMessage(
                receivedMessage.message,
                receivedMessage.createdAt,
                person
            )            .setBuilder(notificationBuilder)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setFields(user: User) {
        headerView.findViewById<TextView>(R.id.fullNameTv).text =
            String.format(
                resources.getString(R.string.full_name),
                user.firstname,
                user.lastname
            )
        headerView.findViewById<TextView>(R.id.emailTv).text = user.email

    }


    private fun setupDrawer() {
        drawerLayout = binding.drawerLayout
        val navView = binding.navView
        val toolbar = binding.drawerToolbar
        val navGraphIds =
            listOf(
                R.navigation.rooms,
                R.navigation.add_room,
                R.navigation.profile,
                R.navigation.users
            )
        val controller = navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.drawer_container,
            currentItemId = drawerSelectedItemId,
            parentNavController = findNavController(),
            intent = requireActivity().intent,
            onLogoutClick = this
        )
        controller.observe(viewLifecycleOwner) { navController ->
            NavigationUI.setupWithNavController(
                toolbar,
                navController,
                drawerLayout
            )
            drawerSelectedItemId = navController.graph.id
            navController.addOnDestinationChangedListener { _, destination, _ ->
                title = destination.label.toString()
                binding.drawerToolbarTitle.text = title
                binding.userInfoLl.isVisible = destination.id == R.id.fragmentPrivateMessages
            }
        }
    }

    private fun setBackPressedHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.drawerLayout.isOpen) {
                binding.drawerLayout.close()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        socket.emit("disconnectEvent", storageManager.getEmail())
        socket.off("updateList")
        socket.off("privateMessageFromServer")
        storageManager.clearSharedPref()
        socket.close()
    }

    override fun onLogoutClick() {
        findNavController().navigate(R.id.action_FragmentHomeMain_to_fragmentAuthMain)
    }
}
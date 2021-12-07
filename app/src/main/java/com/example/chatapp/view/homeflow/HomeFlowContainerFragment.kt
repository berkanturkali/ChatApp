package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentHomeFlowContainerBinding
import com.example.chatapp.model.User
import com.example.chatapp.utils.StorageManager
import com.example.chatapp.utils.showSnack
import com.example.chatapp.view.BaseFragment
import com.example.chatapp.viewmodel.homeflow.HomeFlowContainerViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import javax.inject.Inject

@AndroidEntryPoint
class HomeFlowContainerFragment :
    BaseFragment<FragmentHomeFlowContainerBinding>(FragmentHomeFlowContainerBinding::inflate) {

    private val mViewModel by viewModels<HomeFlowContainerViewModel>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var headerView: View

    @Inject
    lateinit var storageManager: StorageManager

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var gson: Gson

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerView = binding.navView.getHeaderView(0)
        setupDrawer()
        setBackPressedHandler()
        connectSocket()
        subscribeObservers()
    }

    private fun connectSocket() {
        socket.connect()
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit("setUser", storageManager.getFullname(), storageManager.getEmail())
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
                    showSnack(it.message!!)
                }
            )
        }
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

    private fun setBackPressedHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.drawerLayout.isOpen) {
                binding.drawerLayout.close()
            }
        }
    }

    private fun setupDrawer() {
        drawerLayout = binding.drawerLayout
        val navView = binding.navView
        val toolbar = binding.drawerToolbar
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.drawer_container) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.roomsFragment), drawerLayout)
        navView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(toolbar, navController, drawerLayout)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            logout()
            true
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val title =
                if (destination.id == R.id.messagesFragment) MessagesFragmentArgs.fromBundle(
                    arguments!!).room else destination.label
            binding.drawerToolbar.title = title
        }
    }

    private fun logout() {
        storageManager.clearSharedPref()
        findNavController().navigate(R.id.action_homeFlowContainerFragment_to_loginSignupOptionsFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        storageManager.clearSharedPref()
        socket.off("update")
        socket.close()

    }
}
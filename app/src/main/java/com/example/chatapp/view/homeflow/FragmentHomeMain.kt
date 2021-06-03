package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentHomeMainLayoutBinding
import com.example.chatapp.model.User
import com.example.chatapp.utils.handleResource
import com.example.chatapp.utils.setupWithNavController
import com.example.chatapp.utils.showSnack
import com.example.chatapp.viewmodel.homeflow.FragmentHomeMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import javax.inject.Inject

@AndroidEntryPoint
class FragmentHomeMain : Fragment(R.layout.fragment_home_main_layout) {

    private var _binding: FragmentHomeMainLayoutBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: FragmentHomeMainViewModel by viewModels()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var headerView: View
    private val drawerSelectedItemIdKey = "DRAWER_SELECTED_ITEM_ID_KEY"
    private var drawerSelectedItemId = R.id.rooms
    private lateinit var title: String

    @Inject
    lateinit var socket: Socket

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeMainLayoutBinding.bind(view)
        savedInstanceState?.let {
            drawerSelectedItemId = it.getInt(drawerSelectedItemIdKey, drawerSelectedItemId)
        }
        mViewModel.getMe()
        headerView = binding.navView.getHeaderView(0)
        setupDrawer()
        setBackPressedHandler()
        socket.connect()
        subscribeObservers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(drawerSelectedItemIdKey, drawerSelectedItemId)
        super.onSaveInstanceState(outState)
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
            listOf(R.navigation.rooms, R.navigation.add_room, R.navigation.profile)
        val controller = navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.drawer_container,
            currentItemId = drawerSelectedItemId,
            parentNavController = findNavController(),
            intent = requireActivity().intent
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
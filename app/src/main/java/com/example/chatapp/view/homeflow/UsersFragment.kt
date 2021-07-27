package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.adapter.UsersLoadStateAdapter
import com.example.chatapp.databinding.FragmentUsersLayoutBinding
import com.example.chatapp.model.User
import com.example.chatapp.utils.StorageManager
import com.example.chatapp.utils.showSnack
import com.example.chatapp.viewmodel.homeflow.FragmentHomeMainViewModel
import com.example.chatapp.viewmodel.homeflow.FragmentUsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "UsersFragment"

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users_layout), UserAdapter.OnClickListener {

    private var _binding: FragmentUsersLayoutBinding? = null
    private val binding get() = _binding!!

    private val mViewModel by viewModels<FragmentUsersViewModel>()
    private lateinit var mAdapter: UserAdapter

    private val homeViewModel by viewModels<FragmentHomeMainViewModel>(ownerProducer = { requireParentFragment().requireParentFragment() })

    @Inject
    lateinit var storageManager: StorageManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUsersLayoutBinding.bind(view)
        initRecyclerView()
        subscribeObservers()
        binding.retryButton.setOnClickListener {
            mAdapter.retry()
        }
        binding.swipeRefresh.setOnRefreshListener {
            mAdapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        mAdapter = UserAdapter()
        mAdapter.setClickListener(this)
        mAdapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && mAdapter.itemCount == 0
            showEmptyList(isListEmpty)
            binding.apply {
                usersRv.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
            }
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                binding.root.showSnack(R.color.colorDanger, "\uD83D\uDE28 Wooops ${it.error}")
            }
        }
        binding.apply {
            usersRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = mAdapter.withLoadStateHeaderAndFooter(
                    header = UsersLoadStateAdapter { mAdapter.retry() },
                    footer = UsersLoadStateAdapter { mAdapter.retry() }
                )
                setHasFixedSize(true)
            }
        }
    }

    private fun showEmptyList(isListEmpty: Boolean) {
        binding.emptyList.isVisible = isListEmpty
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.users?.collectLatest { users ->
                mAdapter.submitData(users)
            }
        }
        homeViewModel.update.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { update ->

                if (update) mAdapter.refresh()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(user: User) {
        val action =
            UsersFragmentDirections.actionUsersFragmentToFragmentPrivateMessages(
                room = "${user.email}",
                fullName = String.format(
                    requireContext().resources.getString(R.string.full_name),
                    user.firstname,
                    user.lastname
                )
            )
        findNavController().navigate(action)
    }
}
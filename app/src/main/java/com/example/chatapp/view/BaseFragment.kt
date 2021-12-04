package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.chatapp.utils.Resource

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>,
) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    inline fun <reified T> handleResource(
        resource: Resource<T>,
        successFunc: () -> Unit,
        errorFunc: () -> Unit,
    ) {
        when (resource) {
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                successFunc()
            }
            is Resource.Error -> {
                errorFunc()
            }
        }
    }

    fun launchOnLifecycleScope(execute: suspend () -> Unit) =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            execute()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
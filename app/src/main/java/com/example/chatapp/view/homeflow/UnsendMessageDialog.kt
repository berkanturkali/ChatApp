package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.chatapp.databinding.DialogUnsendMessageBinding
import com.example.chatapp.utils.Constants.MESSAGE_ID
import com.example.chatapp.utils.setNavigationResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UnsendMessageDialog:BottomSheetDialogFragment() {

    private var _binding:DialogUnsendMessageBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UnsendMessageDialogArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogUnsendMessageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.unsendTv.setOnClickListener {
            setNavigationResult(MESSAGE_ID,args.id)
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
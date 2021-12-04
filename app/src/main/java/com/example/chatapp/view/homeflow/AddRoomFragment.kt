package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentAddRoomBinding
import com.example.chatapp.model.Room
import com.example.chatapp.utils.getFileName
import com.example.chatapp.utils.showSnack
import com.example.chatapp.utils.trim
import com.example.chatapp.view.BaseFragment
import com.example.chatapp.viewmodel.homeflow.AddRoomFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val TAG = "FragmentAddRoom"

@AndroidEntryPoint
class AddRoomFragment : BaseFragment<FragmentAddRoomBinding>(FragmentAddRoomBinding::inflate) {

    private var photoFile: File? = null
    private lateinit var getContent: ActivityResultLauncher<String>

    private var room: String = ""

    private val mViewModel: AddRoomFragmentViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        subscribeObservers()
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { image ->
            image?.let {
                binding.roomIv.setImageURI(it)
                val parcelFileDescriptor =
                    requireActivity().contentResolver.openFileDescriptor(it, "r", null)
                        ?: return@let
                photoFile = File(
                    requireContext().cacheDir,
                    requireContext().contentResolver.getFileName(it)
                )
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val outputStream = FileOutputStream(photoFile)
                inputStream.copyTo(outputStream)
            }
        }
    }

    private fun initButtons() {
        binding.apply {
            addRoomBtn.setOnClickListener {
                room = roomEt.trim()
                if (mViewModel.isValid(room)) saveRoom()
            }
            selectImage.setOnClickListener {
                getContent.launch("image/*")
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showSnack(message)
            }
        }
        mViewModel.roomInfo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                handleResource(
                    resource = resource,
                    successFunc = {
                        showSnack(resource.data!!,R.color.colorSuccess)
                    },
                    errorFunc = {
                        showSnack(resource.message!!)
                    }
                )
            }
        }
    }

    private fun saveRoom() {
        photoFile?.let {
            val imageBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", photoFile?.name, imageBody)
            mViewModel.addRoom(Room(room), body)
        } ?: mViewModel.addRoom(Room(room), null)
    }
}
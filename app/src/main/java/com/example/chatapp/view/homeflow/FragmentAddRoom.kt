package com.example.chatapp.view.homeflow

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentAddRoomLayoutBinding
import com.example.chatapp.model.Room
import com.example.chatapp.utils.*
import com.example.chatapp.viewmodel.homeflow.FragmentAddRoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val TAG = "FragmentAddRoom"
@AndroidEntryPoint
class FragmentAddRoom : Fragment(R.layout.fragment_add_room_layout) {

    private var photoFile: File? = null
    private lateinit var getContent: ActivityResultLauncher<String>

    private var room: String = ""

    private var _binding: FragmentAddRoomLayoutBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: FragmentAddRoomViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddRoomLayoutBinding.bind(view)
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
                room = roomEt.text.toString().trim()
                mViewModel.validate(room)
            }
            selectImage.setOnClickListener {
                getContent.launch("image/*")
            }
        }
    }

    private fun subscribeObservers() {
        mViewModel.isValid.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isValid ->
                if (isValid) {
                    saveRoom()
//                    mViewModel.addRoom(Room(room))
                } else {
                    binding.apply {
                        roomEt.validate("Valid room name is required") { s -> s.isValidEmail() }
                    }
                }
            }
        }
        mViewModel.roomInfo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resource ->
                handleResource(
                    resource = resource,
                    successFunc = {
                        binding.root.showSnack(R.color.colorSuccess, resource.data!!)
                    },
                    errorFunc = {
                        Log.i(TAG, "subscribeObservers: ${resource.message}")
                        binding.root.showSnack(R.color.colorDanger, resource.message!!)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.chatapp.repo

import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.utils.Resource
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeChatRepo @Inject constructor() : ChatRepo {

    private val rooms = mutableListOf<Room>()

    private val messageList = mutableListOf<Message.TextMessage>()

    private var shouldFail = false

    private var shouldReturnEmptyList = false

    override suspend fun addRoom(room: Room, body: MultipartBody.Part?): Resource<String> {
        return if (!shouldFail) {
            rooms.add(room)
            Resource.Success("${room.name} is created successfully.")
        }else{
            Resource.Error("error")
        }
    }

    override suspend fun getRooms(): Resource<List<Room>> {
        return if (!shouldFail) {
            if (!shouldReturnEmptyList) {
                for (i in 1..50) {
                    rooms.add(Room("Room $i"))
                }
            }
            Resource.Success(rooms)
        } else {
            Resource.Error("error")
        }
    }

    override suspend fun getHistory(room: String): Resource<List<Message.TextMessage>> {
        if (room.isEmpty()) {
            return Resource.Error("Empty Room")
        }
        messageList.add(Message.TextMessage("Hey", "John Doe", room))
        messageList.add(Message.TextMessage("Sup", "John Doe", room))
        return Resource.Success(messageList)
    }

    fun setFail(shouldFail: Boolean) {
        this.shouldFail = shouldFail
    }
}
package com.example.chatapp

import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.example.chatapp.repo.ChatRepo
import com.example.chatapp.utils.Resource
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeChatRepo @Inject constructor() : ChatRepo {

    private val roomList = mutableListOf<Room>()

    private val messageList = mutableListOf<Message.TextMessage>()

    override suspend fun addRoom(room: Room, body: MultipartBody.Part?): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getRooms(): Resource<List<Room>> {
        roomList.add(Room("Room 1"))
        roomList.add(Room("Room 2"))
        roomList.add(Room("Room 3"))
        return Resource.Success(roomList)


    }

    override suspend fun getHistory(room: String): Resource<List<Message.TextMessage>> {
        if (room.isEmpty()) {
            return Resource.Error("Empty Room")
        }
        messageList.add(Message.TextMessage("Hey", "Sender", room))
        messageList.add(Message.TextMessage("Sup", "Sender", room))
        return Resource.Success(messageList)
    }
}
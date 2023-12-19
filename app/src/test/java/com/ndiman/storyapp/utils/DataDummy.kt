package com.ndiman.storyapp.utils

import com.ndiman.storyapp.data.remote.response.ListStoryItem
import com.ndiman.storyapp.data.remote.response.LoginResponse
import com.ndiman.storyapp.data.remote.response.LoginResult

object DataDummy {
    fun generateDummyLoginResponse(): LoginResponse {
        val newsList = LoginResponse(
            LoginResult(
                "Onic",
                "user-vbnPQjvggr3KNmE8",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXZiblBRanZnZ3IzS05tRTgiLCJpYXQiOjE3MDI4OTE5OTh9.1uYHnqw5kHSNYyFweQJBvjv_X_jByHND6jMbfMT47k8"
            ),
            false,
            "success"
        )

        return newsList
    }

    fun generateDummyUser(): List<DummyUser> {
        return listOf(
            DummyUser("onic@gmail.com", "onic12345"),
        )
    }

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photoUrl + $i",
                "createdAt + $i",
                "name $i",
                "description $i",
                i.toDouble(),
                "id $i",
                i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}

data class DummyUser(val email: String, val password: String)
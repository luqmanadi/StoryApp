package com.ndiman.storyapp.utils

object DataDummy {
    fun generateDummyUser(): List<DummyUser> {
        return listOf(
            DummyUser("onic@gmail.com", "onic12345"),
        )
    }
}

data class DummyUser(val email: String, val password: String)
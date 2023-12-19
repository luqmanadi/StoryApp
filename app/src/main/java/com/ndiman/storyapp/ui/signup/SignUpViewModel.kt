package com.ndiman.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import com.ndiman.storyapp.data.StoryRepository

class SignUpViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun userRegister(name: String, email: String, password: String) = storyRepository.registerUser(name, email, password)
}
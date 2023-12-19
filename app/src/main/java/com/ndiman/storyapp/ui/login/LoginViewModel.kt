package com.ndiman.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndiman.storyapp.data.StoryRepository
import com.ndiman.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun loginUser(email: String, password: String) = storyRepository.loginUser(email, password)

    fun saveSession(userModel: UserModel){
        viewModelScope.launch {
            storyRepository.saveSession(userModel)
        }
    }
}
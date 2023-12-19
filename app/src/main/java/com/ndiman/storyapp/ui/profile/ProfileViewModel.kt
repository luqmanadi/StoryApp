package com.ndiman.storyapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ndiman.storyapp.data.StoryRepository
import com.ndiman.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun logout(){
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return storyRepository.getSession().asLiveData()
    }

    fun getThemeSetting(): LiveData<Boolean>{
        return storyRepository.getThemeSeting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            storyRepository.saveThemeSetting(isDarkModeActive)
        }
    }
}
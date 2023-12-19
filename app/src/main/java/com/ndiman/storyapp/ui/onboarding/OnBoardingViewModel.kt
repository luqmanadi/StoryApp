package com.ndiman.storyapp.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ndiman.storyapp.data.StoryRepository
import com.ndiman.storyapp.data.pref.UserModel

class OnBoardingViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun getSession(): LiveData<UserModel> = storyRepository.getSession().asLiveData()
}
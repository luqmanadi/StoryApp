package com.ndiman.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndiman.storyapp.data.StoryRepository
import com.ndiman.storyapp.data.di.Injection
import com.ndiman.storyapp.ui.addstory.AddStoryViewModel
import com.ndiman.storyapp.ui.home.MainViewModel
import com.ndiman.storyapp.ui.login.LoginViewModel
import com.ndiman.storyapp.ui.maps.MapsViewModel
import com.ndiman.storyapp.ui.onboarding.OnBoardingViewModel
import com.ndiman.storyapp.ui.profile.ProfileViewModel
import com.ndiman.storyapp.ui.signup.SignUpViewModel

class ViewModelFactory(private val repository: StoryRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OnBoardingViewModel::class.java) -> {
                OnBoardingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            synchronized(this) {
                instance = Injection.provideRepository(context)?.let { ViewModelFactory(it) }
            }
            return instance as ViewModelFactory
        }
    }
}
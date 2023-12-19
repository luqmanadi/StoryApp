package com.ndiman.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.ndiman.storyapp.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun getStoryLocation() = storyRepository.getStoriesLocation()
}
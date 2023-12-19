package com.ndiman.storyapp.ui.addstory

import androidx.lifecycle.ViewModel
import com.ndiman.storyapp.data.StoryRepository
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun createStory(file: File, description: String, lat: Double?, lon: Double?) = storyRepository.uploadImage(file, description, lat, lon)
}
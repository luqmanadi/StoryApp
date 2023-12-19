package com.ndiman.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ndiman.storyapp.data.StoryRepository
import com.ndiman.storyapp.data.remote.response.ListStoryItem

class MainViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun getThemeSetting(): LiveData<Boolean> {
        return storyRepository.getThemeSeting().asLiveData()
    }

    val storyAll: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getAllStory().cachedIn(viewModelScope)

}
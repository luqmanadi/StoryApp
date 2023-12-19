package com.ndiman.storyapp.data.di

import android.content.Context
import com.ndiman.storyapp.data.StoryRepository
import com.ndiman.storyapp.data.local.room.StoriesDatabase
import com.ndiman.storyapp.data.pref.UserPreference
import com.ndiman.storyapp.data.pref.dataStore
import com.ndiman.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection{
    fun provideRepository(context: Context): StoryRepository? {
        val database = StoriesDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref, database)
    }
}
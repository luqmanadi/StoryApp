package com.ndiman.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.ndiman.storyapp.data.local.room.StoriesDatabase
import com.ndiman.storyapp.data.pref.UserModel
import com.ndiman.storyapp.data.pref.UserPreference
import com.ndiman.storyapp.data.remote.response.AddStoryResponse
import com.ndiman.storyapp.data.remote.response.ListStoryItem
import com.ndiman.storyapp.data.remote.response.RegisterResponse
import com.ndiman.storyapp.data.remote.response.StoryResponse
import com.ndiman.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storiesDatabase: StoriesDatabase
) {

    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "StoryRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "StoryRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    fun getAllStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = true
            ),
            remoteMediator = StoriesRemoteMediator(storiesDatabase, apiService),
            pagingSourceFactory = {
                storiesDatabase.storiesDao().getAllStories()
            }
        ).liveData
    }

    fun uploadImage(imageFile: File, description: String, lat: Double?, lon: Double?) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            Log.d(TAG, "Cek Latitude: $lat, Cek Longitude: $lon")
            val successResponse = apiService.uploadImage(multipartBody, requestBody, lat, lon)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "StoryRepository: $jsonString")
            val errorResponse = Gson().fromJson(jsonString, AddStoryResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    fun getStoriesLocation() = liveData {
        try{
            val response = apiService.getStoriesWithLocation()
            emit(Result.Success(response.listStory))
        }catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "StoryRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, StoryResponse::class.java)
            emit(Result.Error(errorBody.message))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    suspend fun saveSession(userModel: UserModel) = userPreference.saveSession(userModel)

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() = userPreference.logout()

    fun getThemeSeting(): Flow<Boolean> {
        return userPreference.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        userPreference.saveThemeSetting(isDarkModeActive)


    companion object {

        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            storiesDatabase: StoriesDatabase
        ): StoryRepository? {
            synchronized(this) {
                instance = StoryRepository(apiService, userPreference, storiesDatabase)
            }
            return instance
        }
    }

}
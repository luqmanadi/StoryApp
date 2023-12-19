package com.ndiman.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ndiman.storyapp.data.remote.response.ListStoryItem

@Dao
interface StoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<ListStoryItem>)

    @Query("SELECT * FROM stories_entity")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM stories_entity")
    suspend fun deleteAll()

}
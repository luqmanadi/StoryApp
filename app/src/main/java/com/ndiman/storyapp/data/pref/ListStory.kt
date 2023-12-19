package com.ndiman.storyapp.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ListStory(
    val id: String,
    val name: String,
    val photoUrl: String,
    val description: String,
    val date: String
): Parcelable

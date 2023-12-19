package com.ndiman.storyapp.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoriesLocation(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
): Parcelable

package com.ndiman.storyapp.ui.detailstory

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ndiman.storyapp.data.pref.ListStory
import com.ndiman.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private var binding: ActivityDetailStoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_ID, ListStory::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ID)
        }

        Log.d("Cek", "Apa: $story")



        if (story != null){
            binding?.apply {
                Glide.with(this@DetailStoryActivity)
                    .load(story.photoUrl)
                    .into(binding!!.imageStory)
                nameProfile.text = story.name
                description.text = story.description
            }
        }

        binding?.topAppBar?.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }

}
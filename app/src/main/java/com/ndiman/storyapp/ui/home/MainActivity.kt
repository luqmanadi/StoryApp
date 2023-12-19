package com.ndiman.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndiman.storyapp.R
import com.ndiman.storyapp.databinding.ActivityMainBinding
import com.ndiman.storyapp.ui.ViewModelFactory
import com.ndiman.storyapp.ui.adapter.AllStoryAdapter
import com.ndiman.storyapp.ui.adapter.LoadingStateAdapter
import com.ndiman.storyapp.ui.addstory.AddStoryActivity
import com.ndiman.storyapp.ui.maps.MapsActivity
import com.ndiman.storyapp.ui.profile.ProfileActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCardStory.layoutManager = LinearLayoutManager(this)

        setUpAction()
        setUpTheme()
        setListStory()
    }

    private fun setListStory(){
        val adapter = AllStoryAdapter()
        binding.rvCardStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.storyAll.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setUpTheme(){
        viewModel.getThemeSetting().observe(this){isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setUpAction(){

        binding.topAppBar.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.menuProfile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.menuMaps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    private fun showListNull(state: Boolean) { binding.tvListKosong.visibility = if (state) View.VISIBLE else View.GONE }

    override fun onResume() {
        super.onResume()
        viewModel.storyAll
    }

}
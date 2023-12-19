package com.ndiman.storyapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ndiman.storyapp.R
import com.ndiman.storyapp.databinding.ActivityProfileBinding
import com.ndiman.storyapp.ui.ViewModelFactory
import com.ndiman.storyapp.ui.onboarding.OnBoardingActivity

class ProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var binding: ActivityProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.getSession().observe(this){user ->
            user?.let {
                binding?.nameProfile?.text = user.name
                if (!it.isLogin){
                    val intent = Intent(this, OnBoardingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        setUpAction()
        setUpTheme()
    }

    private fun setUpAction(){
        binding?.topAppBar?.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        binding?.btnLogout?.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.logout))
                .setMessage(resources.getString(R.string.logout_description))
                .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                    viewModel.logout()
                    Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton(resources.getString(R.string.no)){_,_ ->
                }
                .create()
                .show()

        }

        binding?.btnChangeLanguage?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

    }

    private fun setUpTheme(){
        viewModel.getThemeSetting().observe(this){isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.switchTheme?.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.switchTheme?.isChecked = false
            }
        }

        binding?.switchTheme?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
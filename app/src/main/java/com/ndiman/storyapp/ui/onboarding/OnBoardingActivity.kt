package com.ndiman.storyapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ndiman.storyapp.databinding.ActivityOnBoardingBinding
import com.ndiman.storyapp.ui.ViewModelFactory
import com.ndiman.storyapp.ui.home.MainActivity
import com.ndiman.storyapp.ui.login.LoginActivity

class OnBoardingActivity : AppCompatActivity() {

    private val viewModel by viewModels<OnBoardingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var binding: ActivityOnBoardingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.getSession().observe(this){user ->
            if (user.isLogin) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else{
                return@observe
            }
        }

        binding?.btnGabung?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
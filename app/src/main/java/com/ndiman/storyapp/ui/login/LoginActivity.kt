package com.ndiman.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ndiman.storyapp.R
import com.ndiman.storyapp.data.Result
import com.ndiman.storyapp.data.pref.UserModel
import com.ndiman.storyapp.databinding.ActivityLoginBinding
import com.ndiman.storyapp.ui.ViewModelFactory
import com.ndiman.storyapp.ui.home.MainActivity
import com.ndiman.storyapp.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private var binding:  ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupAction()
        playAnimation()


    }

    private fun setupAction(){
        binding?.registerButton?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding?.loginButton?.setOnClickListener {
            inputData()
        }

    }


    private fun inputData(){
        val email = binding?.inputEmail?.text.toString()
        val password = binding?.inputPassword?.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            showErrorDialog()
        }else{
            viewModel.loginUser(email, password).observe(this){result ->
                if (result != null){
                    when(result){
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            viewModel.saveSession(
                                UserModel(
                                    name = result.data.loginResult.name,
                                    token = result.data.loginResult.token,
                                    email = email,
                                    isLogin = true))
                            Log.d("error", "Cek Token : ${result.data.loginResult.token}")
                            showLoading(false)
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            MaterialAlertDialogBuilder(this)
                                .setTitle(resources.getString(R.string.success_login))
                                .setMessage(resources.getString(R.string.dialog_success_login))
                                .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                .create()
                                .show()
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showErrorDialog()
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showErrorDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.failed_login))
            .setMessage(resources.getString(R.string.dialog_failed_login))
            .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
            }
            .create()
            .show()
    }


    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding?.imageViewLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding?.titleLogin, View.ALPHA, 1f).setDuration(300)
        val textViewEmail = ObjectAnimator.ofFloat(binding?.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailInputText = ObjectAnimator.ofFloat(binding?.inputEmail, View.ALPHA, 1f).setDuration(300)
        val textViewPassword = ObjectAnimator.ofFloat(binding?.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordInputText = ObjectAnimator.ofFloat(binding?.inputPassword, View.ALPHA, 1f).setDuration(300)
        val orText = ObjectAnimator.ofFloat(binding?.textOr, View.ALPHA, 1f).setDuration(300)
        val divider1 = ObjectAnimator.ofFloat(binding?.divider1, View.ALPHA, 1f).setDuration(300)
        val divider2 = ObjectAnimator.ofFloat(binding?.divider2, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding?.loginButton, View.ALPHA, 1f).setDuration(300)
        val btnRegister = ObjectAnimator.ofFloat(binding?.registerButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(orText, divider1, divider2)
        }

        AnimatorSet().apply {
            playSequentially(title, textViewEmail, emailInputText, textViewPassword, passwordInputText, btnLogin, together, btnRegister)
            start()
        }
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
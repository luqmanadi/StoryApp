package com.ndiman.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ndiman.storyapp.R
import com.ndiman.storyapp.data.Result
import com.ndiman.storyapp.databinding.ActivitySignUpBinding
import com.ndiman.storyapp.ui.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpAction()
        playAnimation()

    }

    private fun setUpAction(){
        binding?.topAppBar?.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        binding?.registerButton?.setOnClickListener {
            inputData()
        }
    }


    private fun inputData(){
        val name = binding?.inputName?.text.toString().trim()
        val email = binding?.inputEmail?.text.toString().trim()
        val password = binding?.inputPassword?.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            showErrorDialog()
        } else{
            viewModel.userRegister(name,email, password).observe(this){result ->
                if (result != null){
                    when(result){
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                            MaterialAlertDialogBuilder(this)
                                .setTitle(resources.getString(R.string.succes_register))
                                .setMessage(resources.getString(R.string.dialog_success_register,email))
                                .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
                                    finish()
                                }
                                .create()
                                .show()

                        }
                        is Result.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            showErrorDataIsAlreadyTaken()
                            showLoading(false)
                        }

                    }
                }
            }
        }
    }

    private fun showErrorDataIsAlreadyTaken(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.failed_register))
            .setMessage(resources.getString(R.string.email_is_already_taken))
            .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
            }
            .create()
            .show()
    }

    private fun showErrorDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.failed_register))
            .setMessage(resources.getString(R.string.dialog_failed_register))
            .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
            }
            .create()
            .show()
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding?.imageViewSingUp, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding?.titleSignUp, View.ALPHA, 1f).setDuration(300)
        val textViewName = ObjectAnimator.ofFloat(binding?.nameTextView, View.ALPHA, 1f).setDuration(300)
        val nameInputText = ObjectAnimator.ofFloat(binding?.inputName, View.ALPHA, 1f).setDuration(300)
        val textViewEmail = ObjectAnimator.ofFloat(binding?.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailInputText = ObjectAnimator.ofFloat(binding?.inputEmail, View.ALPHA, 1f).setDuration(300)
        val textViewPassword = ObjectAnimator.ofFloat(binding?.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordInputText = ObjectAnimator.ofFloat(binding?.inputPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegister = ObjectAnimator.ofFloat(binding?.registerButton, View.ALPHA, 1f).setDuration(300)


        AnimatorSet().apply {
            playSequentially(title, textViewName, nameInputText, textViewEmail ,emailInputText, textViewPassword,passwordInputText, btnRegister)
            start()
        }
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}


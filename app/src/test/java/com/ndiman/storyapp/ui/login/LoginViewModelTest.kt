package com.ndiman.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ndiman.storyapp.data.Result
import com.ndiman.storyapp.data.StoryRepository
import com.ndiman.storyapp.data.remote.response.LoginResponse
import com.ndiman.storyapp.utils.DataDummy
import com.ndiman.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{

    private val dummyUser = DataDummy.generateDummyUser()
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository : StoryRepository
    private val dummyLogin = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Login Should Not Error and Return Success`() {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLogin)

        `when`(storyRepository.loginUser(dummyUser[0].email,dummyUser[0].password)).thenReturn(expectedLogin)

        val actualResponse = loginViewModel.loginUser(dummyUser[0].email,dummyUser[0].password).getOrAwaitValue()
        Mockito.verify(storyRepository).loginUser(dummyUser[0].email,dummyUser[0].password)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(dummyLogin.loginResult.userId, (actualResponse as Result.Success).data.loginResult.userId)
    }

    @Test
    fun `when Login Should Not Success and Return Error`() {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Error("Error")

        `when`(storyRepository.loginUser(dummyUser[0].email,dummyUser[0].password)).thenReturn(expectedLogin)

        val actualResponse = loginViewModel.loginUser(dummyUser[0].email,dummyUser[0].password).getOrAwaitValue()
        Mockito.verify(storyRepository).loginUser(dummyUser[0].email,dummyUser[0].password)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }

}
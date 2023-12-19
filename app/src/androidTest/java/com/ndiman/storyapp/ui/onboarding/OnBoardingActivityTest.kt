package com.ndiman.storyapp.ui.onboarding

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ndiman.storyapp.R
import com.ndiman.storyapp.ui.home.MainActivity
import com.ndiman.storyapp.ui.login.LoginActivity
import com.ndiman.storyapp.ui.profile.ProfileActivity
import com.ndiman.storyapp.utils.DataDummy
import com.ndiman.storyapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class OnBoardingActivityTest{

    private val emailDummy = DataDummy.generateDummyUser()[0].email
    private val passwordDummy = DataDummy.generateDummyUser()[0].password

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(OnBoardingActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun fromlLoginUntilLogout(){

        // cek onboarding swipe left
        onView(withId(R.id.on_boarding_activity)).perform(swipeLeft(), swipeLeft(), swipeLeft())

        // cek button gabung
        onView(withId(R.id.btn_gabung)).check(matches(isDisplayed()))

        // klik button gabung
        onView(withId(R.id.btn_gabung)).perform(click())

        // cek sudah berada di login activity
        Intents.intended(hasComponent(LoginActivity::class.java.name))

        // mengisi form login
        onView(withId(R.id.input_email)).perform(typeText(emailDummy), closeSoftKeyboard())
        onView(withId(R.id.input_password)).perform(typeText(passwordDummy), closeSoftKeyboard())

        // menekan button login
        onView(withId(R.id.loginButton)).perform(click())

        onView(withText(R.string.success_login))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))

        onView(withText(R.string.ok))
            .inRoot(RootMatchers.isDialog())
            .perform(click())

        // memastikan bahwa intent menuju Main Activity
        Intents.intended(hasComponent(MainActivity::class.java.name))

        // mengecek apakah sudah tampil recyclerview card story
        onView(withId(R.id.rv_card_story)).check(matches(isDisplayed()))

    }

    @Test
    fun fromMainUntilLogout(){

        onView(withId(R.id.rv_card_story)).check(matches(isDisplayed()))

        onView(withId(R.id.menuProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.menuProfile)).perform(click())

        Intents.intended(hasComponent(ProfileActivity::class.java.name))

        onView(withId(R.id.btn_logout)).check(matches(isDisplayed()))

        onView(withId(R.id.btn_logout)).perform(click())

        onView(withText(R.string.logout))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))

        onView(withText(R.string.yes))
            .inRoot(RootMatchers.isDialog())
            .perform(click())

        Intents.intended(hasComponent(OnBoardingActivity::class.java.name))
    }
}
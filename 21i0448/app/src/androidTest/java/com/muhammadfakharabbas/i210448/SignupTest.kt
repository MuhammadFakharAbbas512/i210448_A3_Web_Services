package com.muhammadfakharabbas.i210448

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Before
import org.junit.Test


class SignupTest{

    private lateinit var scene: ActivityScenario<Signup>
    @Before
    fun setup(){
        scene = ActivityScenario.launch(Signup::class.java)
        scene.moveToState(Lifecycle.State.RESUMED)
    }
    @Test
    fun testSignupButton(){
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.signup_btn)).perform(ViewActions.click())
    }
}
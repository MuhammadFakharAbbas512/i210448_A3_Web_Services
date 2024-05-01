package com.muhammadfakharabbas.i210448

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.core.app.launchActivity
import org.junit.Before
import org.junit.Test


class LoginTest{

    private lateinit var scene: ActivityScenario<Login>
    @Before
    fun setup(){
        scene = ActivityScenario.launch(Login::class.java)
        scene.moveToState(Lifecycle.State.RESUMED)
    }
    @Test
    fun testLoginButton(){
        Thread.sleep(1000)
        onView(withId(R.id.login_btn)).perform(click())
    }
}
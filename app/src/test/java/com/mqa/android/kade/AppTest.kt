package com.mqa.android.kade

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import com.mqa.android.kade.modules.activities.HomeActivity
import com.mqa.android.kade.R.id.*
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test

class AppTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testAppView() {
        onView(ViewMatchers.isRoot()).perform(waitFor(3000))
        onView(ViewMatchers.withId(navigation))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(navigation_dashboard)).perform(ViewActions.click())
        onView(ViewMatchers.isRoot()).perform(waitFor(3000))
        onView(ViewMatchers.withText("Arsenal"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("Arsenal")).perform(ViewActions.click())
        onView(ViewMatchers.isRoot()).perform(waitFor(2000))
        onView(ViewMatchers.withId(add_to_favorite))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(add_to_favorite)).perform(ViewActions.click())
        onView(ViewMatchers.isRoot()).perform(waitFor(1000))
        pressBack()


    }

    fun waitFor(millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(millis)
            }
        }
    }
}
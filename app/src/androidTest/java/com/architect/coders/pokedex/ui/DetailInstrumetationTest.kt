package com.architect.coders.pokedex.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.data.MockWebServerRule
import com.architect.coders.pokedex.data.OkHttp3IdlingResource
import com.architect.coders.pokedex.util.DrawableMatcher
import com.architect.coders.pokedex.util.RecyclerViewItemCountMatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DetailInstrumetationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setup() {
        hiltRule.inject()

        val resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun change_icon_favorite_button_when_click_it() {
        Espresso.onView(ViewMatchers.withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.btnFavorite))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.btnFavorite))
            .check(ViewAssertions.matches(DrawableMatcher(R.drawable.ic_favorite_bold)))
    }

    @Test
    fun show_gallery_view_when_collection_button_click_it() {
        Espresso.onView(ViewMatchers.withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.btnMyCollection))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.collectionRecycler))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.collectionRecycler))
            .check(ViewAssertions.matches(RecyclerViewItemCountMatcher(0)))
    }
}
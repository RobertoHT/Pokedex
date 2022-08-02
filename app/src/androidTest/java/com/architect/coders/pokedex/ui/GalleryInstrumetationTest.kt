package com.architect.coders.pokedex.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.MediaStore
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.architect.coders.pokedex.R
import com.architect.coders.pokedex.data.MockWebServerRule
import com.architect.coders.pokedex.data.OkHttp3IdlingResource
import com.architect.coders.pokedex.util.RecyclerViewItemCountMatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class GalleryInstrumetationTest {

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
        Intents.init()

        val resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun take_2_photos_with_different_type_shows_in_the_gallery() {
        val intent = Intent()
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(intentResult)

        Espresso.onView(ViewMatchers.withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.btnMyCollection))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabExpandable))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabAmiibo))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.collectionRecycler))
            .check(ViewAssertions.matches(RecyclerViewItemCountMatcher(1)))

        Espresso.onView(ViewMatchers.withId(R.id.fabExpandable))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabPlush))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.collectionRecycler))
            .check(ViewAssertions.matches(RecyclerViewItemCountMatcher(2)))
    }

    @Test
    fun cancel_a_take_photo_no_shows_items_in_gallery() {
        val intent = Intent()
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, intent)
        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(intentResult)

        Espresso.onView(ViewMatchers.withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.btnMyCollection))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabExpandable))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabAmiibo))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.collectionRecycler))
            .check(ViewAssertions.matches(RecyclerViewItemCountMatcher(0)))
    }

    @Test
    fun take_2_photos_with_same_type_shows_in_the_gallery() {
        val intent = Intent()
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(intentResult)

        Espresso.onView(ViewMatchers.withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.btnMyCollection))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabExpandable))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabAmiibo))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabExpandable))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabAmiibo))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.collectionRecycler))
            .check(ViewAssertions.matches(RecyclerViewItemCountMatcher(1)))

        Espresso.onView(ViewMatchers.withId(R.id.galleryRecycler))
            .check(ViewAssertions.matches(RecyclerViewItemCountMatcher(2)))
    }

    @Test
    fun click_a_photo_navigates_to_fullview() {
        val intent = Intent()
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(intentResult)

        Espresso.onView(ViewMatchers.withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.btnMyCollection))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabExpandable))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.fabAmiibo))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.galleryRecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.imvCollection))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
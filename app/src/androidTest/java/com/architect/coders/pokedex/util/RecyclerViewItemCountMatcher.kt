package com.architect.coders.pokedex.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class RecyclerViewItemCountMatcher(private val expectedCount: Int) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("RecyclerView should have $expectedCount items")
    }

    override fun matchesSafely(view: View): Boolean {
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        adapter?.let {
            return it.itemCount == expectedCount
        }

        return false
    }
}
package com.architect.coders.pokedex.util

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class DrawableMatcher(@DrawableRes val id: Int) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("ImageView with drawable same as drawable with id $id")
    }

    override fun matchesSafely(view: View): Boolean {
        val context = view.context
        val expectedBitmap = context.getDrawable(id)?.toBitmap()

        return view is FloatingActionButton && view.drawable.toBitmap().sameAs(expectedBitmap)
    }
}
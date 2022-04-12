package com.architect.coders.pokedex.ui.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.architect.coders.pokedex.databinding.ActivityCollectionBinding
import com.architect.coders.pokedex.util.loadWithPathWithoutPlaceHolder

class CollectionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE = "pokemon:imagePath"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imagePath = intent.getStringExtra(EXTRA_IMAGE)
        imagePath?.let { binding.imvCollection.loadWithPathWithoutPlaceHolder(it) }
    }
}
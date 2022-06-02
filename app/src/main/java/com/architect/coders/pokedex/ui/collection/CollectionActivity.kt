package com.architect.coders.pokedex.ui.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.architect.coders.pokedex.databinding.ActivityCollectionBinding
import com.architect.coders.pokedex.common.loadWithPathWithoutPlaceHolder

class CollectionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE = "pokemon:imagePath"
    }

    private val viewModel: CollectionViewModel by viewModels {
        CollectionViewModelFactory(requireNotNull(intent.getStringExtra(EXTRA_IMAGE)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this) { photo ->
            photo.image?.let { binding.imvCollection.loadWithPathWithoutPlaceHolder(it) }
        }
    }
}
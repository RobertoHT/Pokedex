package com.architect.coders.pokedex.ui.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.architect.coders.pokedex.databinding.ActivityCollectionBinding
import com.architect.coders.pokedex.common.loadWithPathWithoutPlaceHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { photo ->
                    photo.image?.let { binding.imvCollection.loadWithPathWithoutPlaceHolder(it) }
                }
            }
        }
    }
}
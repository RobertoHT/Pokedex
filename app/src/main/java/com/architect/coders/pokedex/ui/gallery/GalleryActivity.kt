package com.architect.coders.pokedex.ui.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.architect.coders.pokedex.common.PokeCollec
import com.architect.coders.pokedex.databinding.ActivityGalleryBinding
import com.architect.coders.pokedex.model.GalleryItem
import com.architect.coders.pokedex.ui.collection.CollectionActivity
import com.architect.coders.pokedex.ui.detail.DetailActivity
import java.io.File

class GalleryActivity : AppCompatActivity() {

    private var imagePath: String = ""
    private var imageName: String  = ""
    private var pokemonID: Int = 0
    private lateinit var pokeType: PokeCollec
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter : GalleryAdapter

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            adapter.addImage(pokeType, imagePath)
        } else {
            val fileTemp = File(imagePath)
            fileTemp.delete()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokemonID = intent.getIntExtra(DetailActivity.EXTRA_ID, 0)

        adapter = GalleryAdapter(getItems()) { image -> navigateTo(image) }
        binding.amiiboRecycler.adapter = adapter

        binding.btnAmiibo.setOnClickListener { dispatchTakePictureIntent(PokeCollec.AMIIBO) }
        binding.btnPlush.setOnClickListener { dispatchTakePictureIntent(PokeCollec.PLUSH) }
        binding.btnOther.setOnClickListener { dispatchTakePictureIntent(PokeCollec.OTHER) }
    }

    private fun dispatchTakePictureIntent(collectionType: PokeCollec) {
        pokeType = collectionType
        val uri = FileProvider.getUriForFile(this, "com.architect.coders.pokedex.fileprovider", createImageFile(collectionType.id))
        getCameraImage.launch(uri)
    }

    private fun createImageFile(collectionType: Int): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "Poke_${pokemonID}_${collectionType}_",
            ".jpg",
            storageDir
        ).apply {
            imagePath = absolutePath
            imageName = name
        }
    }

    private fun getList(): MutableList<String> = mutableListOf(
        "https://placekitten.com/200/200?image=1",
        "https://placekitten.com/200/200?image=2",
        "https://placekitten.com/200/200?image=3",
        "https://placekitten.com/200/200?image=4",
        "https://placekitten.com/200/200?image=5",
        "https://placekitten.com/200/200?image=6",
        "https://placekitten.com/200/200?image=7",
        "https://placekitten.com/200/200?image=8",
        "https://placekitten.com/200/200?image=9",
        "https://placekitten.com/200/200?image=10"
    )

    private fun getItems(): List<GalleryItem> = listOf(
        GalleryItem(PokeCollec.AMIIBO, getList()),
        GalleryItem(PokeCollec.PLUSH, getList()),
        GalleryItem(PokeCollec.OTHER, getList()),
    )

    private fun navigateTo(imageSrc: String) {
        val intent = Intent(this, CollectionActivity::class.java)
        intent.putExtra(CollectionActivity.EXTRA_IMAGE, imageSrc)
        startActivity(intent)
    }
}
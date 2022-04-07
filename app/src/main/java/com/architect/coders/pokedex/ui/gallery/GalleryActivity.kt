package com.architect.coders.pokedex.ui.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.architect.coders.pokedex.util.Collection
import com.architect.coders.pokedex.databinding.ActivityGalleryBinding
import com.architect.coders.pokedex.ui.collection.CollectionActivity
import com.architect.coders.pokedex.ui.detail.DetailActivity
import java.io.File

class GalleryActivity : AppCompatActivity() {

    private var imagePath: String = ""
    private var imageName: String  = ""
    private var pokemonID: Int = 0
    private lateinit var binding: ActivityGalleryBinding

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            //Agregar imagen al adapter correspondiente
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

        val adapter = GalleryAdapter(getItems()) { navigateTo() }
        binding.amiiboRecycler.adapter = adapter

        binding.plushRecycler.adapter = adapter

        binding.otherRecycler.adapter = adapter

        binding.btnAmiibo.setOnClickListener { dispatchTakePictureIntent(Collection.AMIIBO.id) }
        binding.btnPlush.setOnClickListener { dispatchTakePictureIntent(Collection.PLUSH.id) }
        binding.btnOther.setOnClickListener { dispatchTakePictureIntent(Collection.OTHER.id) }
    }

    private fun dispatchTakePictureIntent(collectionType: Int) {
        val uri = FileProvider.getUriForFile(this, "com.architect.coders.pokedex.fileprovider", createImageFile(collectionType))
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

    private fun getItems(): List<String> = listOf(
        "https://placekitten.com/200/200?image=1",
        "https://placekitten.com/200/200?image=2",
        "https://placekitten.com/200/200?image=3"
    )

    private fun navigateTo() {
        val intent = Intent(this, CollectionActivity::class.java)
        intent.putExtra(CollectionActivity.EXTRA_IMAGE, imagePath)
        startActivity(intent)
    }
}
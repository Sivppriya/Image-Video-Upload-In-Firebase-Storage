package com.example.imagevideo


import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.imagevideo.databinding.FragmentImageBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class ImageFragment : Fragment(R.layout.fragment_image) {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
        val imageView = view?.findViewById<ImageView>(R.id.imageView)
        imageView?.setImageURI(selectedImageUri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentImageBinding.bind(view)

        binding.chooseImg.setOnClickListener {
            chooseImage()
        }

        binding.saveImg.setOnClickListener {
            saveImage()
        }
    }

    private fun chooseImage() {
        getContent.launch("image/*")
    }

    private fun saveImage() {
        if (selectedImageUri != null) {
            val imageRef = storageReference.child("images/${UUID.randomUUID()}.jpg")

            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }
}

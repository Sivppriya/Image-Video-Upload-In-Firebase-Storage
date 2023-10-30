package com.example.imagevideo.db

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.imagevideo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class UploadImageFirebase :  Fragment() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private lateinit var imageUploadButton: ImageButton
    private lateinit var uploadButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload_image_firebase, container, false)
        imageUploadButton = view.findViewById(R.id.imageUploadButton)
        uploadButton = view.findViewById(R.id.uploadButtonImage)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        imageUploadButton.setOnClickListener {
            chooseImage()
        }

        uploadButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data

            // Load and display the image using Glide with centerCrop
            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.centerCropTransform())
                .into(imageUploadButton)
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            try {
                val storageReference = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}.jpg")
                storageReference.putFile(filePath!!)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            saveImageUrlToDatabase(imageUrl)

                            showToast("Successfully Uploaded!")
                        }
                    }
                    .addOnFailureListener {

                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)
            databaseReference.child("imageURL").setValue(imageUrl)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

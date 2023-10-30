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

class UploadVideoFirebase :  Fragment() {
    private val PICK_VIDEO_REQUEST = 72
    private var videoPath: Uri? = null
    private lateinit var videoUploadButton: ImageButton
    private lateinit var uploadButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload_video_firebase, container, false)
        videoUploadButton = view.findViewById(R.id.videoUploadButton)
        uploadButton = view.findViewById(R.id.uploadButtonVideo)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set a click listener for the video upload button
        videoUploadButton.setOnClickListener {
            chooseVideo()
        }

        // Set a click listener for the upload button
        uploadButton.setOnClickListener {
            uploadVideo()
        }
    }

    private fun chooseVideo() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            videoPath = data.data

            // Load and display the video thumbnail using Glide with centerCrop
            Glide.with(this)
                .load(videoPath)
                .apply(RequestOptions.centerCropTransform())
                .into(videoUploadButton)
        }
    }

    private fun uploadVideo() {
        if (videoPath != null) {
            try {
                val storageReference = FirebaseStorage.getInstance().reference.child("videos/${System.currentTimeMillis()}.mp4")
                storageReference.putFile(videoPath!!)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            val videoUrl = uri.toString()
                            saveVideoUrlToDatabase(videoUrl)
                            // Show a "Successfully Uploaded" toast
                            showToast("Successfully Uploaded!")
                        }
                    }
                    .addOnFailureListener { e ->
                        // Handle the upload failure
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveVideoUrlToDatabase(videoUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)
            databaseReference.child("videoURL").setValue(videoUrl)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
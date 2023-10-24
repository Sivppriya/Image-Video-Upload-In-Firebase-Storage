package com.example.imagevideo

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.imagevideo.databinding.FragmentVideoBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class VideoFragment : Fragment(R.layout.fragment_video) {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var selectedVideoUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedVideoUri = uri
        val videoView = view?.findViewById<VideoView>(R.id.video)
        videoView?.setVideoURI(selectedVideoUri)
        videoView?.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentVideoBinding.bind(view)

        binding.chooseVideo.setOnClickListener {
            chooseVideo()
        }

        binding.saveVideo.setOnClickListener {
            saveVideo()
        }
    }

    private fun chooseVideo() {
        getContent.launch("video/*")
    }

    private fun saveVideo() {
        if (selectedVideoUri != null) {
            val videoRef = storageReference.child("videos/${UUID.randomUUID()}.mp4")

            videoRef.putFile(selectedVideoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Video uploaded successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Video upload failed", Toast.LENGTH_SHORT).show()
                }
        } else {

            Toast.makeText(requireContext(), "Please select a video", Toast.LENGTH_SHORT).show()
        }
    }
}

package com.example.imagevideo.db

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imagevideo.R

class Firebase : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_firebase, container, false)

        // Create and add the "Image" and "Video" fragments to their respective containers
        val imageFragment = UploadImageFirebase()
        val videoFragment = UploadVideoFirebase()

        childFragmentManager.beginTransaction()
            .replace(R.id.imageFragmentContainer, imageFragment)
            .commit()

        childFragmentManager.beginTransaction()
            .replace(R.id.videoFragmentContainer, videoFragment)
            .commit()

        return view
    }
}

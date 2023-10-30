package com.example.imagevideo.db

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel(private val imageDao: ImageDao, private val videoDao: VideoDao) : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun insertImagesAndVideos() {
        val imageList = listOf(
            ImageEntity(imageUrl = "image_url_1"),
            ImageEntity(imageUrl = "image_url_2")
        )

        val videoList = listOf(
            VideoEntity(videoUrl = "video_url_1"),
            VideoEntity(videoUrl = "video_url_2")
        )

        coroutineScope.launch {
            // Perform database operations on a background thread for both images and videos
            imageDao.insertImage(imageList)

            // Insert videos one by one
            for (video in videoList) {
                videoDao.insertVideo(video)
            }
        }
    }
}

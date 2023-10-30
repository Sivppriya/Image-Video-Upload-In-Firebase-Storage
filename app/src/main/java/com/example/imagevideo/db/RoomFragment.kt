package com.example.imagevideo.db

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imagevideo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class RoomFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job
    private lateinit var appDatabase: AppDatabase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        appDatabase = AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_firebase, container, false)

        // Set the initial fragments to be displayed
        childFragmentManager.beginTransaction()
            .replace(R.id.imageFragmentContainer,  UploadImageFirebase())
            .replace(R.id.videoFragmentContainer, UploadVideoFirebase())
            .commit()

        // Call the upload functions when needed
        uploadImage()
        uploadVideo()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun uploadImage() {

        val imageId = UUID.randomUUID().toString()
        val imageUrl = "https://example.com/images/$imageId.jpg"
        val image = ImageEntity(imageUrl = imageUrl)

        GlobalScope.launch {
            appDatabase.imageDao().insertImage(listOf(image))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun uploadVideo() {

        val videoId = UUID.randomUUID().toString()
        val videoUrl = "https://example.com/videos/$videoId.mp4"
        val video = VideoEntity(videoUrl = videoUrl)
        GlobalScope.launch {
            appDatabase.videoDao().insertVideo(video)
        }
    }
}

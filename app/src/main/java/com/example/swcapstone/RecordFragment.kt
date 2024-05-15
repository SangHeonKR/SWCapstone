package com.example.swcapstone

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.swcapstone.databinding.ActivityCameraBinding
import com.example.swcapstone.models.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RecordFragment : Fragment() {

    private var _binding: ActivityCameraBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.imageCaptureButton.setOnClickListener { takePhoto() }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            getOutputDirectory(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Toast.makeText(context, "Photo Capture Succeeded: $savedUri", Toast.LENGTH_SHORT).show()
                    uploadImage(photoFile)
                }
            })
    }

    private fun uploadImage(file: File) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("images/${file.name}")
        val uploadTask = fileRef.putFile(Uri.fromFile(file))

        uploadTask.addOnSuccessListener {
            Log.d(TAG, "Image upload successful")
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                saveImageInfoToDatabase(imageUrl, file.name)
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Image upload failed", exception)
            Toast.makeText(requireContext(), "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageInfoToDatabase(imageUrl: String, fileName: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("images")

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        if (user != null) {
            val userId = user.uid
            val userImagesRef = databaseRef.child(userId)
            val imageId = userImagesRef.push().key

            if (imageId != null) {
                val imageInfo = Photo(
                    imageUrl = imageUrl,
                    fileName = fileName,
                    timestamp = System.currentTimeMillis()
                )

                userImagesRef.child(imageId).setValue(imageInfo)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Image data saved to database", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to save data to database", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Log.e(TAG, "User not authenticated")
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserPhotos() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val databaseRef = FirebaseDatabase.getInstance().getReference("images")

        if (user != null) {
            val userId = user.uid
            val userImageRef = databaseRef.child(userId)

            userImageRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val photos = mutableListOf<Photo>()
                        for (child in snapshot.children) {
                            val photo = child.getValue(Photo::class.java)
                            if (photo != null) {
                                photos.add(photo)
                            }
                        }
                        displayPhotos(photos)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Failed to fetch user photos", error.toException())
                    }

                })
        } else {
            Log.e(TAG, "User not authenticated")
        }
    }

    private fun displayPhotos(photos: List<Photo>) {
        // Implement this function to handle the photos, e.g., display them in a RecyclerView
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: activity?.filesDir!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    companion object {
        private const val TAG = "RecordFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}


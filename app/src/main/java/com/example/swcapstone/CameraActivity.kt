package com.example.swcapstone

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.swcapstone.databinding.ActivityCameraBinding
import com.example.swcapstone.models.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.imageCaptureButton.setOnClickListener { takePhoto() }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
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
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Show shutter effect
        binding.shutterView.visibility = View.VISIBLE
        binding.shutterView.postDelayed({
            binding.shutterView.visibility = View.GONE
        }, 100)

        val photoFile = File(
            getOutputDirectory(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    // Display the photo taken
                    binding.capturedImageView.setImageURI(savedUri)
                    binding.capturedImageView.visibility = View.VISIBLE
                    binding.viewFinder.visibility = View.GONE
                    binding.imageCaptureButton.isEnabled = false

                    // Delay closing the camera for 1 second
                    binding.viewFinder.postDelayed({
                        // Close the camera
                        finish()
                    }, 1000)

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
            Toast.makeText(this@CameraActivity, "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@CameraActivity, "Image data saved to database", Toast.LENGTH_SHORT).show()
                        // Set result and finish activity
                        setResult(RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@CameraActivity, "Failed to save data to database", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Log.e(TAG, "User not authenticated")
            Toast.makeText(this@CameraActivity, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs?.firstOrNull()?.let {
            File(it, getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

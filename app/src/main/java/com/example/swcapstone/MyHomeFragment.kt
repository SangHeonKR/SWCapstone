package com.example.swcapstone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swcapstone.models.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyHomeFragment : Fragment() {

    private lateinit var imageViewKcal: ImageView
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private lateinit var photoAdapter: FoodItemAdapter
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var displayTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_mypage, container, false)

        imageViewKcal = view.findViewById(R.id.imageViewKcal)
        imageViewKcal.setOnClickListener {
            openNumberSettingActivity()
        }

        // 길게 누르면 갤러리에서 이미지 선택
        imageViewKcal.setOnLongClickListener {
            openGallery()
            true // 이벤트 처리 완료
        }

        // 갤러리에서 이미지를 선택한 후의 처리를 위한 ActivityResultLauncher 초기화
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    updateProfileImage(uri)
                }
            }
        }

        // Initialize RecyclerView and adapter
        photoRecyclerView = view.findViewById(R.id.foodRecyclerView)
        displayTextView = view.findViewById(R.id.goalCaloriesDisplay)
        photoAdapter = FoodItemAdapter(ArrayList(), R.layout.food_record_item)

        // Set up RecyclerView with LinearLayoutManager
        photoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = photoAdapter
        }

        // Fetch and display user photos
        fetchUserPhotos()

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun updateProfileImage(uri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        imageViewKcal.setImageBitmap(bitmap) // 선택된 이미지로 ImageView 업데이트
    }

    private fun openNumberSettingActivity() {
        val intent = Intent(activity, NumberSettingActivity::class.java)
        startActivity(intent)
    }

    private fun fetchUserPhotos() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val databaseRef = FirebaseDatabase.getInstance().getReference("images")

        user?.let { currentUser ->
            val userId = currentUser.uid
            val userImageRef = databaseRef.child(userId)

            userImageRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val photos = mutableListOf<Photo>()
                    for (child in snapshot.children) {
                        val photo = child.getValue(Photo::class.java)
                        photo?.let {
                            photos.add(photo)
                        }
                    }
                    // Update RecyclerView with fetched photos
                    photoAdapter.updateData(photos)
                    updateTotalCalories(photos)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun updateTotalCalories(photos: List<Photo>) {
        val totalCalories = photos.sumOf { it.calories }
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val goalCalories = prefs.getInt("goalCalories", 2000)
        displayTextView.text = "오늘의 섭취 칼로리: $totalCalories / $goalCalories"

        // Save totalCalories to Shared Preferences
        with(prefs.edit()) {
            putInt("totalCalories", totalCalories)
            apply()
        }
    }

    private fun updateCaloriesDisplay() {
        // 여기에서 칼로리 정보를 업데이트하는 로직을 구현
    }
}

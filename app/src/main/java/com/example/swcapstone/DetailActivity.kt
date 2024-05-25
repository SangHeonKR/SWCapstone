package com.example.swcapstone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.swcapstone.models.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var selectedDate: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val year = intent.getIntExtra("YEAR", -1)
        val month = intent.getIntExtra("MONTH", -1)
        val dayOfMonth = intent.getIntExtra("DAY_OF_MONTH", -1)

        selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, dayOfMonth)

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        // Locale 설정에 따라 날짜 형식화
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        dateTextView.text = dateFormat.format(calendar.time)

        // Set up the button click listener to navigate to CameraActivity
        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        // Initialize RecyclerView and adapter
        photoRecyclerView = findViewById(R.id.foodRecyclerView)
        photoAdapter = PhotoAdapter()

        // Set up RecyclerView with LinearLayoutManager
        photoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = photoAdapter
        }

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            fetchUserPhotos(selectedDate)
        }

        // Fetch and display user photos
        fetchUserPhotos(selectedDate)
    }

    private fun fetchUserPhotos(selectedDate: Calendar) {
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
                            // Convert photo timestamp to Calendar instance
                            val photoCalendar = Calendar.getInstance()
                            photoCalendar.timeInMillis = photo.timestamp

                            // Compare photo date with selected date
                            if (isSameDay(selectedDate, photoCalendar)) {
                                photos.add(photo)
                            }
                        }
                    }
                    // Update RecyclerView with fetched photos
                    photoAdapter.submitList(photos)
                    // Hide the refresh indicator
                    swipeRefreshLayout.isRefreshing = false
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    // Hide the refresh indicator
                    swipeRefreshLayout.isRefreshing = false
                }
            })
        }
    }

    private fun isSameDay (cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}

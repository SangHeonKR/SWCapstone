package com.example.swcapstone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RecordFragment : Fragment() {

    private lateinit var nicknameTextView: TextView
    private lateinit var nicknameEditText: EditText
    private lateinit var changeProfileImageView: ImageView
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val PREFS_NAME = "com.example.swcapstone.prefs"
        private const val KEY_NICKNAME = "nickname"
        private const val PROFILE_IMAGE_FILE_NAME = "profile_image.png"
        private const val MAX_IMAGE_SIZE = 300
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_record, container, false)

        nicknameTextView = view.findViewById(R.id.nickname)
        nicknameEditText = view.findViewById(R.id.nickname_edit)
        changeProfileImageView = view.findViewById(R.id.changeprofile)

        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedNickname = sharedPreferences.getString(KEY_NICKNAME, "사용자님")
        nicknameTextView.text = savedNickname
        nicknameEditText.setText(savedNickname)

        nicknameTextView.setOnClickListener {
            nicknameTextView.visibility = View.GONE
            nicknameEditText.visibility = View.VISIBLE
            nicknameEditText.requestFocus()
        }

        nicknameEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val newNickname = nicknameEditText.text.toString()
                if (TextUtils.isEmpty(newNickname) || newNickname.length < 3) {
                    Toast.makeText(requireContext(), "닉네임은 최소 3글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    nicknameTextView.text = newNickname
                    nicknameEditText.visibility = View.GONE
                    nicknameTextView.visibility = View.VISIBLE

                    sharedPreferences.edit().putString(KEY_NICKNAME, newNickname).apply()

                    Toast.makeText(requireContext(), "닉네임이 변경되었습니다!", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

        // Load the profile image if it exists
        loadProfileImage()

        // Initialize the ActivityResultLauncher for image selection
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    val resizedBitmap = resizeBitmap(bitmap, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
                    val roundedBitmap = getRoundedCroppedBitmap(resizedBitmap)
                    val drawable: Drawable = BitmapDrawable(resources, roundedBitmap)
                    changeProfileImageView.setImageDrawable(null) // 기본 이미지를 숨깁니다
                    changeProfileImageView.background = drawable
                    saveProfileImage(roundedBitmap)
                    Toast.makeText(requireContext(), "프로필이 변경되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        changeProfileImageView.setOnClickListener {
            openGallery()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun resizeBitmap(source: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val aspectRatio = source.width.toFloat() / source.height.toFloat()
        val width: Int
        val height: Int

        if (source.width > source.height) {
            width = maxWidth
            height = (maxWidth / aspectRatio).toInt()
        } else {
            height = maxHeight
            width = (maxHeight * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(source, width, height, true)
    }

    private fun getRoundedCroppedBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val radius = Math.min(width, height) / 2
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = "#BAB399"
        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        paint.color = Color.parseColor(color)
        paint.style = Paint.Style.FILL

        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun saveProfileImage(bitmap: Bitmap) {
        val file = File(requireContext().filesDir, PROFILE_IMAGE_FILE_NAME)
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadProfileImage() {
        val file = File(requireContext().filesDir, PROFILE_IMAGE_FILE_NAME)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val drawable: Drawable = BitmapDrawable(resources, bitmap)
            changeProfileImageView.setImageDrawable(null) // 기본 이미지를 숨깁니다
            changeProfileImageView.background = drawable
        }
    }
}
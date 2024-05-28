package com.example.swcapstone

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swcapstone.models.Photo
import com.squareup.picasso.Picasso

class PhotoAdapter : ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.photoImageView)
        private val foodNameTextView: TextView = itemView.findViewById(R.id.foodNameTextView)
        private val caloriesTextView: TextView = itemView.findViewById(R.id.caloriesTextView)
        private val carbohydratesTextView: TextView = itemView.findViewById(R.id.carbohydratesTextView)
        private val fatTextView: TextView = itemView.findViewById(R.id.fatTextView)
        private val proteinTextView: TextView = itemView.findViewById(R.id.proteinTextView)
        private val sugarTextView: TextView = itemView.findViewById(R.id.sugarTextView)
        private val placeholderImage = R.drawable.placeholder_image // Placeholder image resource
        private val consumptionDateTextView: TextView = itemView.findViewById(R.id.consumptionDateTextView)

        fun bind(photo: Photo) {
            // Load image using Picasso
            Picasso.get()
                .load(photo.imageUrl)
                .placeholder(placeholderImage)
                .error(placeholderImage)
                .into(imageView)

            // Log and bind other data
            Log.d("PhotoAdapter", "Binding photo: $photo")

            // Bind the additional details
            foodNameTextView.text = if (photo.name.isNullOrEmpty()) "Error: Name not available" else photo.name
            caloriesTextView.text = "열량: ${photo.calories}kcal"
            carbohydratesTextView.text = "탄수화물: ${photo.carbohydrate}g"
            proteinTextView.text = "단백질: ${photo.protein}g"
            fatTextView.text = "지방: ${photo.fat}g"
            sugarTextView.text = "당류: ${photo.sugar}g"
            val datePart = extractDateFromFilename(photo.fileName)
            consumptionDateTextView.text = "섭취일: $datePart"
        }
    }

    private fun extractDateFromFilename(fileName: String): String {
        return fileName.substring(0, 10)
    }

    class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.fileName == newItem.fileName
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
}
package com.example.swcapstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swcapstone.models.Photo
import com.squareup.picasso.Picasso

class FoodItemAdapter(private val foodItems: MutableList<Photo>, private val layoutResId: Int) :
    RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_record_item, parent, false)
        return FoodItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val currentItem = foodItems[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = foodItems.size

    fun updateData(newPhotos: List<Photo>) {
        foodItems.clear()
        foodItems.addAll(newPhotos)
        notifyDataSetChanged()
    }

    class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.foodImageView)
        private val foodNameTextView: TextView = itemView.findViewById(R.id.foodNameTextView)
        private val caloriesTextView: TextView = itemView.findViewById(R.id.caloriesTextView)
        private val proteinTextView: TextView = itemView.findViewById(R.id.proteinTextView)
        private val carbohydratesTextView: TextView = itemView.findViewById(R.id.carbohydratesTextView)
        private val fatTextView: TextView = itemView.findViewById(R.id.fatTextView)
        private val sugarTextView: TextView = itemView.findViewById(R.id.sugarTextView)
        private val consumptionDateTextView: TextView = itemView.findViewById(R.id.consumptionDateTextView)
        private val placeholderImage = R.drawable.placeholder_image

        fun bind(currentItem: Photo) {
            Picasso.get()
                .load(currentItem.imageUrl)
                .placeholder(placeholderImage)
                .error(placeholderImage)
                .into(imageView)

            foodNameTextView.text = if (currentItem.name.isNullOrEmpty()) "Error: Name not available" else currentItem.name
            caloriesTextView.text = "열량: ${currentItem.calories}kcal"
            proteinTextView.text = "단백질: ${currentItem.protein}g"
            carbohydratesTextView.text = "탄수화물: ${currentItem.carbohydrate}g"
            fatTextView.text = "지방: ${currentItem.fat}g"
            sugarTextView.text = "당류: ${currentItem.sugar}g"
            val datePart = extractDateFromFilename(currentItem.fileName)
            consumptionDateTextView.text = "섭취일: $datePart"
        }

        private fun extractDateFromFilename(fileName: String): String {
            return fileName.substring(0, 10)
        }
    }
}

package com.example.randompet   // <-- match your package

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randompet.R   // <-- add if R is red

class PetAdapter(private val petList: List<String>) :
    RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petImage: ImageView = view.findViewById(R.id.pet_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pet_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = petList[position]

        Glide.with(holder.itemView)
            .load(url)
            .centerCrop()
            .into(holder.petImage)

        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Doggo at position ${position + 1} clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    } // <-- this was missing

    override fun getItemCount(): Int = petList.size
} // <-- and this closes the class

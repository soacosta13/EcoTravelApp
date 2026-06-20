package com.example.ecotravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotravel.R
import com.example.ecotravel.databinding.ItemTravelBinding
import com.example.ecotravel.model.TravelRecord
import com.example.ecotravel.viewmodel.TravelViewModel

/**
 * adapter para el historial, usa listadapter para actualizar a traves de diffutil
 */
class TravelAdapter (
    private val onLongClick: (TravelRecord) -> Unit
) : ListAdapter<TravelRecord, TravelAdapter.TravelViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val binding = ItemTravelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        holder.bind(getItem(position), onLongClick)
    }

    class TravelViewHolder(
        private val binding: ItemTravelBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: TravelRecord, onLongClick: (TravelRecord) -> Unit) {

            // Texts
            binding.tvDestination.text = record.destination
            binding.tvDistance.text = "${record.distanceKm} km · ${record.transport}"
            binding.tvCo2.text = "${"%.2f".format(record.co2Kg)} kg CO₂"

            // Transport icon and color
            val (iconRes, colorRes) = when (record.transport) {
                TravelViewModel.TRANSPORT_PLANE -> Pair(
                    android.R.drawable.ic_menu_upload,
                    R.color.color_plane
                )
                TravelViewModel.TRANSPORT_CAR -> Pair(
                    android.R.drawable.ic_menu_directions,
                    R.color.color_car
                )
                TravelViewModel.TRANSPORT_TRAIN -> Pair(
                    android.R.drawable.ic_menu_sort_by_size,
                    R.color.color_train
                )
                else -> Pair(android.R.drawable.ic_menu_directions, R.color.text_secondary)
            }

            binding.ivTransportIcon.setImageResource(iconRes)
            binding.ivTransportIcon.setColorFilter(
                binding.root.context.getColor(colorRes)
            )

            // Long click to delete
            binding.root.setOnLongClickListener {
                onLongClick(record)
                true
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TravelRecord>() {
        override fun areItemsTheSame(oldItem: TravelRecord, newItem: TravelRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TravelRecord, newItem: TravelRecord): Boolean {
            return oldItem == newItem
        }
    }
}
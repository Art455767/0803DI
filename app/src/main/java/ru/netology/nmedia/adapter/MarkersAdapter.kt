package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardMarkerBinding
import ru.netology.nmedia.entity.MarkerPoint

interface OnMarkerInteractionListener {
    fun onEdit(marker: MarkerPoint)
    fun onRemove(marker: MarkerPoint)
}

class MarkersAdapter(
    private val onMarkerInteractionListener: OnMarkerInteractionListener,
) : PagingDataAdapter<MarkerPoint, MarkersAdapter.MarkerViewHolder>(MarkerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val binding = CardMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkerViewHolder(binding, onMarkerInteractionListener)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun submitData(lifecycle: Lifecycle, pagingData: List<MarkerPoint>?) {

    }

    class MarkerViewHolder(
        private val binding: CardMarkerBinding,
        private val onMarkerInteractionListener: OnMarkerInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: MarkerPoint) {
            binding.apply {
                title.text = marker.description

                editButton.setOnClickListener {
                    onMarkerInteractionListener.onEdit(marker)
                }

                removeButton.setOnClickListener {
                    onMarkerInteractionListener.onRemove(marker)
                }
            }
        }
    }
}

class MarkerDiffCallback : DiffUtil.ItemCallback<MarkerPoint>() {
    override fun areItemsTheSame(oldItem: MarkerPoint, newItem: MarkerPoint): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MarkerPoint, newItem: MarkerPoint): Boolean {
        return oldItem == newItem
    }
}
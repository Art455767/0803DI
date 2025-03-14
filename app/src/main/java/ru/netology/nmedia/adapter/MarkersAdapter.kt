package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardMarkerBinding
import ru.netology.nmedia.entity.MarkerPoint

interface OnMarkerInteractionListener {
    fun onEdit(marker: MarkerPoint) // Метод для редактирования маркера
    fun onRemove(marker: MarkerPoint) // Метод для удаления маркера
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

    class MarkerViewHolder(
        private val binding: CardMarkerBinding,
        private val onMarkerInteractionListener: OnMarkerInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: MarkerPoint) {
            binding.apply {
                title.text = marker.description // Привязка данных маркера
                // Другие привязки данных, если есть

                editButton.setOnClickListener {
                    onMarkerInteractionListener.onEdit(marker) // Вызов метода редактирования
                }

                removeButton.setOnClickListener {
                    onMarkerInteractionListener.onRemove(marker) // Вызов метода удаления
                }
            }
        }
    }
}

class MarkerDiffCallback : DiffUtil.ItemCallback<MarkerPoint>() {
    override fun areItemsTheSame(oldItem: MarkerPoint, newItem: MarkerPoint): Boolean {
        return oldItem.id == newItem.id // Предполагается, что у MarkerPoint есть уникальный идентификатор
    }

    override fun areContentsTheSame(oldItem: MarkerPoint, newItem: MarkerPoint): Boolean {
        return oldItem == newItem
    }
}
package com.max.adapter.toolbar.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.max.adapter.databinding.ItemToolbarBinding
import com.max.adapter.toolbar.controller.IOptionController
import com.max.adapter.toolbar.controller.IToolbarController

class DataToolbarAdapter<T>(
    private val controller: IToolbarController<T>,
    val data: List<IOptionController<T>>
) : RecyclerView.Adapter<DataToolbarAdapter.VH>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val binding = ItemToolbarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding).apply {
            binding.root.setOnClickListener {
                data[adapterPosition].onAction(parent.context, controller, adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int
    ) {
        val item = data[position]
        item.onBindOption(holder)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class VH(val binding: ItemToolbarBinding) : RecyclerView.ViewHolder(binding.root)
}
package com.max.adapter.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.adapter.databinding.LayoutToolBarBinding
import com.max.adapter.toolbar.adpater.DataToolbarAdapter
import com.max.adapter.toolbar.controller.IOptionController

class CommonDataToolbar<T>(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val binding = LayoutToolBarBinding.inflate(LayoutInflater.from(context), this, true)

    fun setOptions(options: List<IOptionController<T>>) {
        binding.rvTools.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTools.adapter = DataToolbarAdapter<T>(options)
    }

    fun bindRecyclerView(recyclerView: RecyclerView) {

    }
}
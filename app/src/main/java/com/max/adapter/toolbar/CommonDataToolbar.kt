package com.max.adapter.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.adapter.databinding.LayoutToolBarBinding
import com.max.adapter.toolbar.adpater.DataToolbarAdapter
import com.max.adapter.toolbar.controller.IOptionController
import com.max.adapter.toolbar.controller.IToolbarController
import com.max.adapter.toolbar.listener.IDataSourceListener

class CommonDataToolbar<T>(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val binding = LayoutToolBarBinding.inflate(LayoutInflater.from(context), this, true)
    private val controller = object : IToolbarController<T> {
        override fun updateOption(optionIndex: Int) {
            binding.rvTools.adapter?.notifyItemChanged(optionIndex)
        }

        override fun updateDataSource(dataSource: List<T>) {
            onDataSourceListener?.onDataSourceChanged(dataSource)
        }

        override fun getDataSource(): List<T> {
            return onDataSourceListener?.onProvideDataSource() ?: emptyList()
        }
    }
    var onDataSourceListener: IDataSourceListener<T>? = null

    fun setOptions(options: List<IOptionController<T>>) {
        binding.rvTools.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTools.adapter = DataToolbarAdapter(controller, options)
    }
}
package com.max.adapter.toolbar.controller

import android.content.Context
import com.max.adapter.toolbar.adpater.DataToolbarAdapter

interface IOptionController<T> {
    /**
     * 用户点击
     */
    fun onAction(context: Context, parentController: IToolbarController<T>, optionIndex: Int)

    /**
     * 绑定视图
     */
    fun onBindOption(holder: DataToolbarAdapter.VH)
}
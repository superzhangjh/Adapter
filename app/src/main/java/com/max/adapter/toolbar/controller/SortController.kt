package com.max.adapter.toolbar.controller

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.max.adapter.toolbar.adpater.DataToolbarAdapter
import com.max.adapter.toolbar.controller.sort.ISortStrategy

class SortController<T>(val strategies: List<ISortStrategy<T>>) : IOptionController<T> {
    private val items = strategies.map { it.getName() }.toTypedArray()
    // 默认选中的下标（-1=不选中）
    private var checkedItem = 0

    override fun onAction(
        context: Context,
        parentController: IToolbarController<T>,
        optionIndex: Int
    ) {
        AlertDialog.Builder(context)
            .setTitle("请选择")
            // 单选列表：参数（选项数组，默认选中下标，点击监听）
            .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                // which = 选中的下标（0,1,2...）
                checkedItem = which
            }
            .setPositiveButton("确定") { dialog, _ ->
                // 点击确定后获取选中结果
                if (checkedItem != -1) {
                    val selectedText = items[checkedItem]
                    val sortStrategy = strategies[checkedItem]
                    val sortResult = sortStrategy.onSort(parentController.getDataSource())
                    parentController.updateDataSource(sortResult)
                    parentController.updateOption(optionIndex)

                    // 你要的结果：selectedText 或下标 checkedItem
                    Toast.makeText(context, "选中：$selectedText", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun onBindOption(holder: DataToolbarAdapter.VH) {
        holder.binding.tvLabel.text = items[checkedItem]
    }
}
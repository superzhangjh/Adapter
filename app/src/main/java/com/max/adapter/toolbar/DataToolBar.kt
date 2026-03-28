package com.max.adapter.toolbar

import com.max.adapter.toolbar.adpater.DataToolbarAdapter

/**
 * 排序、筛选、搜索工具栏，用于操作管理列表数据
 */
class DataToolBarHelper<T> {

    /**
     * 设置工具栏选项
     */
    fun setToolOptions() {

    }

    /**
     * 更新数据源
     */
    fun setDataSource(dataSource: List<T>) {

    }
}

sealed class ToolOption() {

    data class Sort(val sortControllers: List<Any>) : ToolOption()

    data object Filter : ToolOption()

    data object Search : ToolOption()

    data object ClearFilter : ToolOption()

}


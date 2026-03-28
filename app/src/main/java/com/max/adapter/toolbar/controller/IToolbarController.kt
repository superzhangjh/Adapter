package com.max.adapter.toolbar.controller

/**
 * 用于控制Toolbar
 */
interface IToolbarController<T> {

    /**
     * 更新某个选项的UI
     */
    fun updateOption(optionIndex: Int)

    /**
     * 更新数据源
     */
    fun updateDataSource(dataSource: List<T>)

    /**
     * 获取数据源
     */
    fun getDataSource(): List<T>
}
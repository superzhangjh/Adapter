package com.max.adapter.toolbar.controller.sort

/**
 * 排序策略
 */
interface ISortStrategy<T> {
    /**
     * @return 策略名称
     */
    fun getName(): String

    /**
     * 执行排序的操作，可能是异步操作
     * @param dataSource 数据源
     * @return 排序结果
     */
    fun onSort(dataSource: List<T>): List<T>
}
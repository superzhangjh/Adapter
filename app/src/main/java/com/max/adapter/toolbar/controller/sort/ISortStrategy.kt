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
     * todo: 加一个参数,用于排序完成后应用排序成功,也可以让排序策略自动调用接口获取新的数据,不用本地排序的方式
     * @param dataSource 数据源
     * @return 排序结果
     */
    fun onSort(dataSource: List<T>): List<T>
}
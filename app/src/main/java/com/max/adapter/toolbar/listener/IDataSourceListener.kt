package com.max.adapter.toolbar.listener

interface IDataSourceListener<T> {
    /**
     * 提供数据源
     */
    fun onProvideDataSource(): List<T>

    /**
     * 数据源发生改变
     */
    fun onDataSourceChanged(dataSource: List<T>)
}
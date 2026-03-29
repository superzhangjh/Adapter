package com.max.adapter.toolbar.controller.sort

open class CustomSortStrategy<T>(val name: String) : ISortStrategy<T> {
    override fun getName(): String {
        return name
    }

    override fun onSort(dataSource: List<T>): List<T> {
        return dataSource
    }
}
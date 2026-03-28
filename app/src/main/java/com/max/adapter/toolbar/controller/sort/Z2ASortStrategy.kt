package com.max.adapter.toolbar.controller.sort

class Z2ASortStrategy : ISortStrategy<String> {

    override fun getName(): String {
        return "Z-A"
    }

    override fun onSort(dataSource: List<String>): List<String> {
        return dataSource
    }
}
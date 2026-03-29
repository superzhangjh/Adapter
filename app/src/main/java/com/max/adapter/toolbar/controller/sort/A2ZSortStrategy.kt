package com.max.adapter.toolbar.controller.sort

class A2ZSortStrategy : ISortStrategy<String> {

    override fun getName(): String {
        return "A-Z"
    }

    override fun onSort(dataSource: List<String>): List<String> {
        return dataSource.sortedBy { it }
    }
}
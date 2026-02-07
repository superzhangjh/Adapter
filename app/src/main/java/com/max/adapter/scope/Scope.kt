package com.max.adapter.scope

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/*********************** ListScope  **********************/
/**
 * 可编辑的列表作用域
 */
interface MutableListScope {
    /**
     * 通过ViewBinding添加item
     */
    fun <VB : ViewBinding> addItem(block: ViewBindingItemScope<VB>.() -> Unit): MutableListScope

//    /**
//     * 通过资源文件添加item
//     */
//    fun addItem(layoutResId: Int, block: ItemScope<T, ResourceHolderScope<T>>.() -> Unit): MutableListScope<T>
//
//    /**
//     * 通过view的方式添加item
//     */
//    fun <V : View> addItem1(block: ItemScope<T, CustomViewHolderScope<T, V>>.() -> Unit): MutableListScope<T>
//
//    /**
//     * 构建多个item
//     */
//    fun <T : Any> addItems(data: List<T>, block: FixListScope<T>.() -> Unit): MutableListScope<T>
}

/**
 * 固定数量的列表作用域
 */
interface FixListScope<T> {
    /**
     * 通过ViewBinding构建item
     */
    fun <VB : ViewBinding> itemBuilder(block: TypeItemScope<T, ViewBindingTypeHolderScope<T, VB>>.() -> Unit): FixListScope<T>

    /**
     * 通过资源文件构建item
     */
    fun itemBuilder(layoutResId: Int, block: TypeItemScope<T, ResourceTypeHolderScope<T>>.() -> Unit): FixListScope<T>

    /**
     * 通过view的方式构建item
     */
    fun <V : View> itemBuilder1(block: TypeItemScope<T, CustomViewTypeHolderScope<T, V>>.() -> Unit): FixListScope<T>
}

/******************  ItemScope  ******************/
/**
 * 默认的item作用域
 */
interface ItemScope<H : HolderScope> {
    /**
     * 订阅init事件，只会在createView的时候触发，相当于onCreateViewHolder
     */
    fun onInit(block: H.() -> Unit)

    /**
     * 订阅绑定事件，每次更新视图都会触发，相当于onBindViewHolder
     */
    fun onBind(block: H.() -> Unit)
}

/**
 * ViewBinding item作用域
 */
interface ViewBindingItemScope<VB>: ItemScope<ViewBindingHolderScope<VB>> {

    fun onCreateViewBinding(block: (LayoutInflater) -> VB)

}

/**
 * 构建Item的作用域，可实现多布局的样式
 */
interface TypeItemScope<T, H : HolderScope> : ItemScope<H> {
    /**
     * 订阅数据判断，如果不订阅则为默认的构建item函数
     */
    fun onItemType(block: (T) -> Boolean)
}

/******************  HolderScope  ******************/
/**
 * ViewHolder作用域
 */
interface HolderScope {
    /**
     * 获取当前的ViewHolder
     */
    fun getHolder(): RecyclerView.ViewHolder
}

interface TypeHolderScope<T> : HolderScope {
    /**
     * 数据坐标
     */
    val dataPosition: Int

    /**
     * 当前的item数据
     */
    val itemData: T
}

/******************  HolderScopeExt  ******************/
interface VBHolderScopeExt<VB> {
    //当前的binding
    val binding: VB
}

interface ViewHolderScopeExt<V : View> {
    //当前的视图
    val view: V
}

/******************  HolderScope Impl  ******************/
/**
 * 默认的ViewBidingHolder作用域
 */
interface ViewBindingHolderScope<VB> : HolderScope, VBHolderScopeExt<VB>
interface ViewBindingTypeHolderScope<T, VB> : TypeHolderScope<T>, VBHolderScopeExt<VB>

/**
 * 资源文件的ViewBindingHolder作用域
 */
interface ResourceHolderScope<T> : HolderScope, ViewHolderScopeExt<View>
interface ResourceTypeHolderScope<T> : TypeHolderScope<T>, ViewHolderScopeExt<View>

/**
 * 指定View的ViewBindingHolder作用域
 */
interface CustomViewHolderScope<T, V : View> : HolderScope, ViewHolderScopeExt<View>
interface CustomViewTypeHolderScope<T, V : View> : TypeHolderScope<T>,
    ViewHolderScopeExt<View>
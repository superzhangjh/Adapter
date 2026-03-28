@file:Suppress("UNCHECKED_CAST")

package com.max.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.max.adapter.scope.MutableListScope
import com.max.adapter.scope.ViewBindingHolderScope
import com.max.adapter.scope.ViewBindingItemScope

fun RecyclerView.linear(@RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL): RecyclerView {
    layoutManager = LinearLayoutManager(context, orientation, false)
    return this
}

fun RecyclerView.mutableItems(block: MutableListScope.() -> Unit) {
    val scopeImpl = MutableListScopeImpl()
    block(scopeImpl)
    adapter = RvAdapter(scopeImpl)
}

class RvAdapter(private val scopeImpl: MutableListScopeImpl) : RecyclerView.Adapter<HolderScopeImpl<*>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderScopeImpl<*> {
        scopeImpl.itemTypeMap[viewType]!!.apply {
            val binding = onCreateViewBindingBlock.invoke(LayoutInflater.from(parent.context))
            return HolderScopeImpl(binding).apply {
                (oInitBlock as? (ViewBindingHolderScope<*>.() -> Unit))?.invoke(this)
            }
        }
    }

    override fun onBindViewHolder(holder: HolderScopeImpl<*>, position: Int) {
        val viewType = getItemViewType(position)
        scopeImpl.itemTypeMap[viewType]!!.apply {
            (onBindBlock as? (ViewBindingHolderScope<*>.() -> Unit))?.invoke(holder)
        }
    }

    override fun getItemCount(): Int {
        return scopeImpl.mItemScopes.size
    }

    override fun getItemViewType(position: Int): Int {
        return scopeImpl.mItemScopes[position].itemType
    }
}

var grobalitemType = 0

class MutableListScopeImpl() : MutableListScope {
    val mItemScopes = mutableListOf<ItemScopeImpl<*>>()
    val itemTypeMap = mutableMapOf<Int, ItemScopeImpl<*>>()

    override fun <VB : ViewBinding> addItem(block: ViewBindingItemScope<VB>.() -> Unit): MutableListScope {
        val itemScope = ItemScopeImpl<VB>(grobalitemType++)
        //todo:itemType动态生成,如果遇到相同的类型的itemType,则在原有的基础上加1
        itemTypeMap[itemScope.itemType] = itemScope
        mItemScopes.add(itemScope)
        block(itemScope)
        return this
    }
}

class ItemScopeImpl<VB: ViewBinding>(val itemType: Int) : ViewBindingItemScope<VB> {

    lateinit var onCreateViewBindingBlock: (LayoutInflater) -> VB
    var oInitBlock: (ViewBindingHolderScope<VB>.() -> Unit)? = null
    var onBindBlock: (ViewBindingHolderScope<VB>.() -> Unit)? = null

    override fun onCreateViewBinding(block: (LayoutInflater) -> VB) {
        onCreateViewBindingBlock = block
    }

    override fun onInit(block: ViewBindingHolderScope<VB>.() -> Unit) {
        oInitBlock = block
    }

    override fun onBind(block: ViewBindingHolderScope<VB>.() -> Unit) {
        onBindBlock = block
    }
}

class HolderScopeImpl<VB : ViewBinding>(override val binding: VB) : RecyclerView.ViewHolder(binding.root), ViewBindingHolderScope<VB>  {

    override fun getHolder(): RecyclerView.ViewHolder {
        return this
    }
}
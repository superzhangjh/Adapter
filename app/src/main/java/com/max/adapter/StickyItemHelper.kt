package com.max.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class StickyItemHelper(
    private val recyclerView: RecyclerView,
    private val isSticky: (position: Int) -> Boolean
) {

    @Px
    var elevationWhenSticky: Float = recyclerView.resources.displayMetrics.density * 4

    private var overlayContainer: FrameLayout? = null
    private var overlayHeight: Int = 0
    private var stickyVH: RecyclerView.ViewHolder? = null
    private var stickyViewType: Int = RecyclerView.INVALID_TYPE

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
            updateStickyState()
        }
    }

    private val onLayoutChangeListener =
        View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateStickyState() }

    private var currentStickyView: View? = null
    private var currentStickyPosition: Int = RecyclerView.NO_POSITION

    fun attach() {
        recyclerView.addOnScrollListener(scrollListener)
        recyclerView.addOnLayoutChangeListener(onLayoutChangeListener)
        updateStickyState()
    }

    fun detach() {
        recyclerView.removeOnScrollListener(scrollListener)
        recyclerView.removeOnLayoutChangeListener(onLayoutChangeListener)
        clearCurrentSticky()
        removeOverlay()
    }

    private fun updateStickyState() {
        if (recyclerView.layoutManager == null || recyclerView.adapter == null) {
            if (currentStickyPosition != RecyclerView.NO_POSITION) {
                ensureOverlay()
                applyOverlayTranslation(pushOffDistanceForCurrent())
            } else {
                clearCurrentSticky()
                removeOverlay()
            }
            return
        }

        resetNonStickyChildren()

        var candidate: View? = null
        var candidatePos = RecyclerView.NO_POSITION

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.viewAdapterPosition
            if (position != RecyclerView.NO_POSITION && isSticky(position)) {
                val top = child.top - recyclerView.paddingTop
                if (top <= 0) {
                    if (candidate == null || top > (candidate!!.top - recyclerView.paddingTop)) {
                        candidate = child

                        candidatePos = position
                    }
                }
            }
        }

        if (candidate != null && candidatePos != RecyclerView.NO_POSITION) {
            ensureOverlay()
            ensureStickyViewHolderFor(candidatePos)
            bindStickyViewHolder(candidatePos)
            measureAndLayoutSticky(candidate)

            val targetTranslation = computeTranslationY(candidate, nextStickyTopAbove(candidatePos))
            applySticky(candidate, candidatePos)
            applyOverlayTranslation(targetTranslation)
            // 避免视觉重影：当吸顶时让原始视图透明
            candidate.alpha = 0f
        } else {
            if (currentStickyPosition != RecyclerView.NO_POSITION) {
                ensureOverlay()
                applyOverlayTranslation(pushOffDistanceForCurrent())
            } else {
                clearCurrentSticky()
                removeOverlay()
            }
        }

        if (currentStickyPosition != RecyclerView.NO_POSITION) {
            val view = findVisibleChildByAdapterPos(currentStickyPosition)
            if (view != null) {
                val top = view.top - recyclerView.paddingTop
                if (top >= 0) {
                    view.alpha = 1f
                    clearCurrentSticky()
                    removeOverlay()
                }
            }
        }
    }

    private fun ensureStickyViewHolderFor(position: Int) {
        val adapter = recyclerView.adapter ?: return
        val type = adapter.getItemViewType(position)
        if (stickyVH == null || type != stickyViewType) {
            stickyViewType = type
            stickyVH = adapter.createViewHolder(overlayContainer!!, type)
            overlayContainer!!.removeAllViews()
            overlayContainer!!.addView(stickyVH!!.itemView)
        }
    }

    private fun bindStickyViewHolder(position: Int) {
        val adapter = recyclerView.adapter ?: return
        val holder = stickyVH ?: return
        // 调用 Adapter 正常绑定逻辑
        adapter.onBindViewHolder(holder, position)
    }

    private fun measureAndLayoutSticky(sourceCandidate: View) {
        val holder = stickyVH ?: return
        val container = overlayContainer ?: return
        val lpSrc = sourceCandidate.layoutParams
        val marginLp = (lpSrc as? ViewGroup.MarginLayoutParams)
        val leftMargin = marginLp?.leftMargin ?: 0
        val rightMargin = marginLp?.rightMargin ?: 0
        val topMargin = marginLp?.topMargin ?: 0
        val bottomMargin = marginLp?.bottomMargin ?: 0

        container.setPadding(leftMargin, topMargin, rightMargin, bottomMargin)

        val availableWidth =
            recyclerView.width - recyclerView.paddingLeft - recyclerView.paddingRight - leftMargin - rightMargin
        val wSpec = View.MeasureSpec.makeMeasureSpec(availableWidth.coerceAtLeast(0), View.MeasureSpec.EXACTLY)
        val hSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        holder.itemView.measure(wSpec, hSpec)
        overlayHeight = holder.itemView.measuredHeight
        holder.itemView.layout(0, 0, availableWidth, overlayHeight)
        val totalHeight = overlayHeight + topMargin + bottomMargin
        container.layoutParams = container.layoutParams.apply {
            height = totalHeight
        }
        container.requestLayout()
    }

    private fun resetNonStickyChildren() {
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            if (child !== currentStickyView) {
                resetChild(child)
            }
        }
    }

    private fun findVisibleChildByAdapterPos(position: Int): View? {
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val pos = (child.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
            if (pos == position) return child
        }
        return null
    }

    private fun nextStickyTopAbove(currentPos: Int): Int? {
        var nextStickyTop: Int? = null
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val pos = (child.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
            if (pos != RecyclerView.NO_POSITION && isSticky(pos) && pos > currentPos) {
                val childTop = child.top - recyclerView.paddingTop
                if (childTop >= 0) {
                    if (nextStickyTop == null || childTop < nextStickyTop!!) {
                        nextStickyTop = childTop
                    }
                }
            }
        }
        return nextStickyTop
    }

    private fun pushOffDistanceForCurrent(): Float {
        val nextTop = nextStickyTopAbove(currentStickyPosition)
        val total = (overlayContainer?.height ?: (overlayHeight))
        return if (nextTop != null) min(0f, (nextTop - total).toFloat()) else 0f
    }

    private fun computeTranslationY(candidate: View, nextStickyTop: Int?): Float {
        val top = candidate.top - recyclerView.paddingTop
        var translation = (-top).toFloat()
        if (nextStickyTop != null) {
            translation = min(translation, (nextStickyTop - candidate.height).toFloat())
        }
        return translation
    }

    private fun applySticky(view: View, position: Int) {
        currentStickyView = view
        currentStickyPosition = position
        // 不再移动原始视图位置，避免高度/位置抖动，仅做 alpha 隐藏
        view.translationY = 0f
        view.elevation = 0f
        recyclerView.invalidate()
    }

    private fun ensureOverlay() {
        if (overlayContainer != null) return
        val parent = recyclerView.parent as? ViewGroup ?: return
        overlayContainer = FrameLayout(recyclerView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                recyclerView.width,
                recyclerView.height
            )
            elevation = elevationWhenSticky + 1f
        }
        parent.addView(overlayContainer)
        // 将容器放置在 RecyclerView 顶部内边距处
        val rvTop = recyclerView.top + recyclerView.paddingTop
        overlayContainer!!.translationY = rvTop.toFloat()
        overlayContainer!!.bringToFront()
    }

    private fun removeOverlay() {
        stickyVH = null
        stickyViewType = RecyclerView.INVALID_TYPE
        val parent = recyclerView.parent as? ViewGroup
        overlayContainer?.let { parent?.removeView(it) }
        overlayContainer = null
        overlayHeight = 0
    }

    private fun applyOverlayTranslation(translationY: Float) {
        overlayContainer?.let {
            val base = recyclerView.top + recyclerView.paddingTop
            it.translationY = base + translationY
            it.elevation = elevationWhenSticky + 1f
        }
    }

    private fun clearCurrentSticky() {
        currentStickyView?.let { resetChild(it) }
        currentStickyView = null
        currentStickyPosition = RecyclerView.NO_POSITION
        recyclerView.invalidate()
    }

    private fun resetChild(child: View) {
        if (child.translationY != 0f) child.translationY = 0f
        if (child.elevation != 0f) child.elevation = 0f
        if (child.alpha != 1f) child.alpha = 1f
    }
}

fun RecyclerView.enableSticky(
    elevationDp: Float = 4f,
    isSticky: (position: Int) -> Boolean
): StickyItemHelper {
    val helper = StickyItemHelper(this, isSticky)
    helper.elevationWhenSticky = resources.displayMetrics.density * elevationDp
    helper.attach()
    return helper
}

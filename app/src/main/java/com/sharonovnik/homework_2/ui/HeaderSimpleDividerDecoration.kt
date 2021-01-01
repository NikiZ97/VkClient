package com.sharonovnik.homework_2.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.sharonovnik.homework_2.dpToPx
import com.sharonovnik.homework_2.ui.posts.PostType


class HeaderSimpleDividerDecoration(
    context: Context,
    private val dividerDrawable: Drawable?
) : ItemDecoration() {

    private val space: Int = context.dpToPx(10)

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (RecyclerView.NO_POSITION >= position) return
        val viewType = parent.adapter?.getItemViewType(position)
        if (viewType != PostType.HEADER.ordinal) {
            outRect.bottom = space * 2
        } else {
            if (position != 0) {
                outRect.top = space
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount - 1
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val itemCount = parent.adapter?.itemCount ?: 0
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (position == RecyclerView.NO_POSITION) {
                continue
            }
            val viewType = parent.adapter?.getItemViewType(position)
            if (viewType != PostType.HEADER.ordinal) {
                val nextItem = position + 1
                if (nextItem < itemCount) {
                    val nextViewType = parent.adapter?.getItemViewType(nextItem)
                    if (nextViewType == PostType.HEADER.ordinal) {
                        continue
                    }
                }
                val topDraw = view.bottom
                val bottomDraw = view.bottom + space
                dividerDrawable?.bounds = Rect(left, topDraw, right, bottomDraw)
                dividerDrawable?.draw(c)
            }
        }
    }
}
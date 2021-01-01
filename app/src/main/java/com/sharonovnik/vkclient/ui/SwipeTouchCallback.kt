package com.sharonovnik.vkclient.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sharonovnik.vkclient.ui.posts.PostType


class SwipeTouchCallback(private val adapter: ItemTouchHelperAdapter)
    : ItemTouchHelper.SimpleCallback(0, 0) {

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        return if (viewHolder.itemViewType != PostType.HEADER.ordinal) {
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        } else {
            super.getSwipeDirs(recyclerView, viewHolder)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        vibrate(viewHolder.itemView.context)
        adapter.onItemLikeStateChanged(viewHolder.adapterPosition, direction == ItemTouchHelper.RIGHT)
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        return 0.1f
    }

    private fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }

    interface ItemTouchHelperAdapter {
        fun onItemLikeStateChanged(position: Int, liked: Boolean)
    }
}


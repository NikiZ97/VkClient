package com.sharonovnik.homework_2.ui.custom

import android.content.Context
import android.view.MotionEvent
import com.github.chrisbanes.photoview.OnSingleFlingListener
import com.sharonovnik.homework_2.dpToPx
import kotlin.math.abs

abstract class CloseOnFlingListener(context: Context) : OnSingleFlingListener {
    private var maxXPx: Int
    private var minYPx: Int

    companion object {
        private const val MIN_Y_DP = 80
        private const val MAX_X_DP = 100
        private const val THRESHOLD_VELOCITY = 200
    }

    init {
        maxXPx = context.dpToPx(MAX_X_DP)
        minYPx = context.dpToPx(MIN_Y_DP)
    }

    override fun onFling(
        motionEvent: MotionEvent, motionEvent2: MotionEvent, velocityX: Float, velocityY: Float
    ): Boolean {
        val distanceByY: Float = motionEvent.y - motionEvent2.y

        if (abs(motionEvent.x - motionEvent.x) > maxXPx) {
            return false
        }
        if (abs(velocityY) < THRESHOLD_VELOCITY) {
            return false
        }
        return if (abs(distanceByY) < minYPx) {
            false
        } else onVerticalFling(distanceByY)
    }

    abstract fun onVerticalFling(distanceByY: Float): Boolean
}
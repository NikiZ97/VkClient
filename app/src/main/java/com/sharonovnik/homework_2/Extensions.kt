package com.sharonovnik.homework_2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sharonovnik.homework_2.data.local.entity.PostEntity
import com.sharonovnik.homework_2.ui.posts.PostRow
import kotlinx.android.synthetic.main.post_options_popup_window.view.*
import kotlinx.android.synthetic.main.view_post.view.*
import java.text.SimpleDateFormat
import java.util.*

fun Context.dpToPx(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.convertTimeStampToString(timestamp: Int): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val date = Date(timestamp.toLong() * 1000)
    val currentCalendar = Calendar.getInstance()
    val calendar = Calendar.getInstance().apply {
        time = date
    }

    fun isSameDay(calendar: Calendar, calendar2: Calendar): Boolean {
        return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    if (isSameDay(calendar, currentCalendar)) {
        return getString(R.string.today_label)
    }
    currentCalendar.apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
    if (isSameDay(calendar, currentCalendar)) {
        return getString(R.string.yesterday_label)
    }
    return dateFormat.format(date)
}

fun Context.openActivity(
    clazz: Class<*>, options: ActivityOptionsCompat? = null, extras: Bundle.() -> Unit
) {
    val intent = Intent(this, clazz).apply {
        putExtras(Bundle().apply(extras))
    }
    startActivity(intent, options?.toBundle())
}

fun List<PostEntity>.groupByDate(context: Context): List<PostRow> {
    forEach {
        it.displayDate = context.convertTimeStampToString(it.date.toInt())
    }
    return groupBy { it.displayDate }
        .flatMap {
            listOf(PostRow.Header(it.key, null), *(it.value.map { post ->
                if (post.attachments == null || post.attachments[0]?.photo == null) {
                    return@map (PostRow.TextBody(post))
                } else {
                    return@map (PostRow.Body(post))
                }
            }).sortedByDescending { post -> post.post?.date }
                .toTypedArray())
        }
}

fun List<PostEntity>.convertToPostRow(): List<PostRow> {
    val posts = mutableListOf<PostRow>()
    forEach { post ->
        if (post.attachments == null || post.attachments[0]?.photo == null) {
            posts += (PostRow.TextBody(post))
        } else {
            posts += (PostRow.Body(post))
        }
    }
    return posts.sortedByDescending { it.post?.date }
}

fun PostEntity.convertToPostRow(): PostRow {
    return if (attachments == null || attachments[0]?.photo == null) {
        (PostRow.TextBody(this))
    } else {
        (PostRow.Body(this))
    }
}

fun View.showOptionsPopupWindow(doOnSave: () -> Unit, doOnShare: () -> Unit) {
    val popupView = LayoutInflater.from(context)
        .inflate(R.layout.post_options_popup_window, null)
    val popupWindow = PopupWindow(
        popupView,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        true
    )
    with(popupWindow) {
        elevation = 10f
        showAsDropDown(this@showOptionsPopupWindow, options.measuredWidth /* not working :(*/, 0)
        popupView.downloadImage.setOnClickListener {
            doOnSave()
            dismiss()
        }
        popupView.shareImage.setOnClickListener {
            doOnShare()
            dismiss()
        }
    }
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment?.doIfVisible(action: () -> Unit) {
    if (this != null && isVisible) {
        action()
    }
}

fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun Context.getDisplayHeight(): Int {
    val display = (this as Activity).windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    return outMetrics.heightPixels
}

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}
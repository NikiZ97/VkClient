package com.sharonovnik.homework_2.ui

import java.text.SimpleDateFormat
import java.util.*

object DateTimeConverter {
    private val TIME = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val FULL_DATE = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    private val DATE = Date()
    private val CALENDAR = Calendar.getInstance()

    fun getDateFromUnixTime(unixTime: Long): String {
        CALENDAR.timeInMillis = System.currentTimeMillis()
        DATE.time = unixTime * 1000
        CALENDAR.set(CALENDAR.get(Calendar.YEAR), CALENDAR.get(Calendar.MONTH), CALENDAR.get(Calendar.DATE), 0, 0, 0)
        return if (unixTime * 1000 > CALENDAR.timeInMillis) TIME.format(DATE) else FULL_DATE.format(DATE)
    }
}
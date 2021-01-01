package com.sharonovnik.homework_2.ui

interface DecorationTypeProvider {
    fun getHeaderPositionForItem(position: Int): Int
    fun isHeader(position: Int): Boolean
}
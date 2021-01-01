package com.sharonovnik.vkclient.ui

interface DecorationTypeProvider {
    fun getHeaderPositionForItem(position: Int): Int
    fun isHeader(position: Int): Boolean
}
package com.sharonovnik.vkclient.data.mapper

interface Mapper<T, U> {
    fun map(value: T): U
}

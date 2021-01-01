package com.sharonovnik.homework_2.data.mapper

interface Mapper<T, U> {
    fun map(value: T): U
}

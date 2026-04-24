package ru.bogatyreva.class_schedule.utils

sealed class ResultState<out T>() {
    class Error(val msg: String) : ResultState<Nothing>()
    class Success<out T>(val data: T) : ResultState<T>()
    object Default: ResultState<Nothing>()
}
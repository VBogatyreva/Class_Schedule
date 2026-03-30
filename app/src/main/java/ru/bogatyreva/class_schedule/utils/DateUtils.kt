package ru.bogatyreva.class_schedule.utils

import java.time.Instant
import java.time.ZoneId

//Вспомогательная функция: Показать кнопку «Сегодня», подсветить выбранную дату в календаре, проверить, есть ли уроки на эту дату
fun isSameDay(date1: Instant, date2: Instant): Boolean {
    val localDate1 = date1.atZone(ZoneId.systemDefault()).toLocalDate()
    val localDate2 = date2.atZone(ZoneId.systemDefault()).toLocalDate()
    return localDate1 == localDate2
}
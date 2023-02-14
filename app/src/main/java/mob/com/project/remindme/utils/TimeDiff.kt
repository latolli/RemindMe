package mob.com.project.remindme.utils

import java.time.LocalDateTime

//very advanced method to calculate time between two dates
fun calculateTimeBetween(startTime: LocalDateTime, endTime: LocalDateTime): Int {
    var daysInStart = startTime.year * 365 + startTime.dayOfMonth
    if (startTime.monthValue <= 2) {daysInStart+=31}
    if (startTime.monthValue <= 3) {daysInStart+=28}
    if (startTime.monthValue <= 4) {daysInStart+=31}
    if (startTime.monthValue <= 5) {daysInStart+=30}
    if (startTime.monthValue <= 6) {daysInStart+=31}
    if (startTime.monthValue <= 7) {daysInStart+=30}
    if (startTime.monthValue <= 8) {daysInStart+=31}
    if (startTime.monthValue <= 9) {daysInStart+=31}
    if (startTime.monthValue <= 10) {daysInStart+=30}
    if (startTime.monthValue <= 11) {daysInStart+=31}
    if (startTime.monthValue <= 12) {daysInStart+=30}

    var daysInEnd = endTime.year * 365 + endTime.dayOfMonth
    if (endTime.monthValue <= 2) {daysInEnd+=31}
    if (endTime.monthValue <= 3) {daysInEnd+=28}
    if (endTime.monthValue <= 4) {daysInEnd+=31}
    if (endTime.monthValue <= 5) {daysInEnd+=30}
    if (endTime.monthValue <= 6) {daysInEnd+=31}
    if (endTime.monthValue <= 7) {daysInEnd+=30}
    if (endTime.monthValue <= 8) {daysInEnd+=31}
    if (endTime.monthValue <= 9) {daysInEnd+=31}
    if (endTime.monthValue <= 10) {daysInEnd+=30}
    if (endTime.monthValue <= 11) {daysInEnd+=31}
    if (endTime.monthValue <= 12) {daysInEnd+=30}

    val totalEnd = daysInEnd*24*60 + endTime.hour*60 + endTime.minute
    val totalStart = daysInStart*24*60 + startTime.hour*60 + startTime.minute

    return totalEnd - totalStart
}
package com.rml.maiappapi.domain

import com.rml.maiappapi.responses.LessonResponse
import java.text.SimpleDateFormat
import java.util.*

data class Lesson(
    val timeStart: Date,
    val timeEnd: Date,
    val type: String,
    val teacher: Teacher,
    val discipline: String,
    val location: String
) {
    fun toResponse(): LessonResponse {
        val timeFormat = SimpleDateFormat("HH:mm")
        return LessonResponse(
            timeFormat.format(timeStart),
            timeFormat.format(timeEnd),
            type,
            teacher,
            discipline,
            location
        )
    }
}
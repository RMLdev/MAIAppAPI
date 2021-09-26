package com.rml.maiappapi.domain

import com.rml.maiappapi.responses.DayResponse
import com.rml.maiappapi.responses.LessonResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Day(
    val date: Date,
    val lessons: ArrayList<Lesson>
) {
    fun toResponse(): DayResponse {
        val dateFormat = SimpleDateFormat("dd.MM")
        val weekFormat = SimpleDateFormat("EEE")
        val date = dateFormat.format(this.date)
        val dayOfWeek = weekFormat.format(this.date)
        return DayResponse(date, dayOfWeek, lessons.map (Lesson::toResponse) as ArrayList<LessonResponse>)
    }
}
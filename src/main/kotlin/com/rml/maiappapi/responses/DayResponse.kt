package com.rml.maiappapi.responses

data class DayResponse(
    val date: String,
    val dayOfWeek: String,
    val lessons: ArrayList<LessonResponse>
)
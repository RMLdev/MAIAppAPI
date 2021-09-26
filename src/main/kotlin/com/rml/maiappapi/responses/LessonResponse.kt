package com.rml.maiappapi.responses

import com.rml.maiappapi.domain.Teacher

data class LessonResponse(
    val timeStart: String,
    val timeEnd: String,
    val type: String,
    val teacher: Teacher,
    val discipline: String,
    val location: String
)
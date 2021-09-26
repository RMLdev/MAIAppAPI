package com.rml.maiappapi.responses

import com.rml.maiappapi.domain.Group
import com.rml.maiappapi.domain.Teacher

sealed class LessonResponse

data class StudentLessonResponse(
    val timeStart: String,
    val timeEnd: String,
    val type: String,
    val teacher: Teacher,
    val discipline: String,
    val location: String
): LessonResponse()

data class TeacherLessonResponse(
    val timeStart: String,
    val timeEnd: String,
    val type: String,
    val groups: List<Group>,
    val discipline: String,
    val location: String
): LessonResponse()
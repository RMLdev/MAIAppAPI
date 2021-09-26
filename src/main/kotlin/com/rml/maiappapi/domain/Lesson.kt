package com.rml.maiappapi.domain

import com.rml.maiappapi.responses.LessonResponse
import com.rml.maiappapi.responses.StudentLessonResponse
import com.rml.maiappapi.responses.TeacherLessonResponse
import java.text.SimpleDateFormat
import java.util.*


sealed class Lesson {
    abstract fun toResponse() : LessonResponse
}

data class StudentLesson(
    val timeStart: Date,
    val timeEnd: Date,
    val type: String,
    val teacher: Teacher,
    val discipline: String,
    val location: String
) : Lesson() {
    override fun toResponse(): StudentLessonResponse {
        val timeFormat = SimpleDateFormat("HH:mm")
        return StudentLessonResponse(
            timeFormat.format(timeStart),
            timeFormat.format(timeEnd),
            type,
            teacher,
            discipline,
            location
        )
    }
}

data class TeacherLesson(
    val timeStart: Date,
    val timeEnd: Date,
    val type: String,
    val groups: List<Group>,
    val discipline: String,
    val location: String
) : Lesson() {
    override fun toResponse(): TeacherLessonResponse {
        val timeFormat = SimpleDateFormat("HH:mm")
        return TeacherLessonResponse(
            timeFormat.format(timeStart),
            timeFormat.format(timeEnd),
            type,
            groups,
            discipline,
            location
        )
    }
}
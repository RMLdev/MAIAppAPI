package com.rml.maiappapi.repository

import com.rml.maiappapi.html_service.ScheduleHtmlService

class ScheduleRepository {
    private val service = ScheduleHtmlService()

    fun getSchedule(groupName: String, week: Int) = service.getSchedule(groupName, week)

    fun getTestTeacherSchedule(guid: String, week: Int) = service.getTeacherSchedule(guid, week)
}
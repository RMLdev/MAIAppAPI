package com.rml.maiappapi.repository

import com.rml.maiappapi.html_service.ScheduleHtmlService

class ScheduleRepository {
    private val service = ScheduleHtmlService()

    fun getSchedule(groupName: String, week: Int) = service.getSchedule(groupName, week)
}
package com.rml.maiappapi.repository

import com.rml.maiappapi.domain.Course
import com.rml.maiappapi.html_service.GroupsHtmlService

class GroupsRepository {
    private val htmlService = GroupsHtmlService()

    fun getAllCourses(): List<Course> = htmlService.getAllCourses()
}
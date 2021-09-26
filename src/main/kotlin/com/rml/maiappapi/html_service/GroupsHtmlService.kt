package com.rml.maiappapi.html_service

import com.rml.maiappapi.constants.HTMLConstants.CONTAINER
import com.rml.maiappapi.constants.HTMLConstants.DIV_SCHEDULE_CONTENT
import com.rml.maiappapi.constants.HTMLConstants.HTML_BASE_URL
import com.rml.maiappapi.constants.HTMLConstants.MAIN_BODY
import com.rml.maiappapi.constants.HTMLConstants.MOBI
import com.rml.maiappapi.constants.HTMLConstants.SCHEDULE
import com.rml.maiappapi.constants.HTMLConstants.SC_CONTAINER
import com.rml.maiappapi.domain.Course
import com.rml.maiappapi.html_service.html_mapper.GroupsMapper
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class GroupsHtmlService {
    private val htmlURL = "$HTML_BASE_URL/$SCHEDULE"
    private val mapper = GroupsMapper()

    fun getAllCourses(): List<Course> = loadHTMLCourses().map(mapper::htmlCourseToCourse)

    private fun loadHTMLCourses(): Elements {
        val html = Jsoup.connect(htmlURL).get()
        val body = html.body()
        val mainBody = body.select(MAIN_BODY)
        val mobi = mainBody
            .select(MOBI)
        val container = mobi
            .select(CONTAINER)
        val courses = container.select(DIV_SCHEDULE_CONTENT).select(SC_CONTAINER)
        return courses
    }

}
package com.rml.maiappapi.html_service

import com.rml.maiappapi.constants.HTMLConstants.CONTAINER
import com.rml.maiappapi.constants.HTMLConstants.DIV_FIRST_CHILD
import com.rml.maiappapi.constants.HTMLConstants.FIRST_CHILD
import com.rml.maiappapi.constants.HTMLConstants.HTML_BASE_URL
import com.rml.maiappapi.constants.HTMLConstants.MAIN_BODY
import com.rml.maiappapi.constants.HTMLConstants.MOBI
import com.rml.maiappapi.constants.HTMLConstants.SCHEDULE
import com.rml.maiappapi.constants.HTMLConstants.SCHEDULE_CONTENT
import com.rml.maiappapi.constants.HTMLConstants.SC_CONTAINER
import com.rml.maiappapi.domain.Day
import com.rml.maiappapi.html_service.html_mapper.ScheduleMapper
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class ScheduleHtmlService {

    private val mapper = ScheduleMapper()

    fun getSchedule(groupName: String, week: Int): List<Day> =
        loadHTMLSchedule(groupName, week).map(mapper::htmlDayToDay)

    private fun loadHTMLSchedule(groupName: String, week: Int): List<Elements> {
        val html = Jsoup.connect("$HTML_BASE_URL/$SCHEDULE/detail.php?group=$groupName&week=$week").get()
        val body = html.body()
        val mainBody = body.select(MAIN_BODY)
        val mobi = mainBody
            .select(MOBI)
        val container = mobi
            .select(CONTAINER)
        val container2 = container
            .select(FIRST_CHILD)
        val content = container2.select(FIRST_CHILD)
        val days = mutableListOf<Elements>()
        content
            .select(SC_CONTAINER).forEach {
                days.add(it.select(DIV_FIRST_CHILD).select(DIV_FIRST_CHILD))
            }
        return days
    }
}
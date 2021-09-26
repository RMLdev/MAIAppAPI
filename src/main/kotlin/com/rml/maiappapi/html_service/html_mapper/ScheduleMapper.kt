package com.rml.maiappapi.html_service.html_mapper

import com.rml.maiappapi.domain.Day
import com.rml.maiappapi.domain.Lesson
import com.rml.maiappapi.domain.Teacher
import org.jsoup.select.Elements
import java.text.SimpleDateFormat
import java.util.*

class ScheduleMapper {

    fun htmlDayToDay(elements: Elements): Day {
        val date = getDateFromHtmlDay(elements)
        val lessons = getLessonsFromHtmlDay(elements)
        return Day(date, lessons)
    }

    private fun getDateFromHtmlDay(day: Elements): Date {
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy EEE")
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val dayOfWeekString = day.select("span.sc-day").text()
        return dateFormatter.parse(
            day.select("div.sc-day-header").textNodes()[0].text() + "." + year + " " + dayOfWeekString
        )
    }

    private fun getLessonsFromHtmlDay(day: Elements): ArrayList<Lesson> {
        val lessonsHtmlInfo =
            day.select("div.sc-table-detail-container").select("div.sc-table-detail").select("div.sc-table-row")
        val lessons = arrayListOf<Lesson>()
        lessonsHtmlInfo.forEach {
            val timeString = it.select("div.sc-item-time").text()
            val startTimeString = timeString.substring(0, 5)
            val endTimeString = timeString.substring(7, timeString.length)
            val timeFormat = SimpleDateFormat("HH:mm")
            val startTime = timeFormat.parse(startTimeString)
            val endTime = timeFormat.parse(endTimeString)
            val type = it.select("div.sc-item-type").text()
            val discipline = it.select("div.sc-item-title-body").select("span.sc-title").text()
            val teacherName = it.select("a[href]").select("span").text()
            val teacherGuid = it.select("a[href]").attr("href").removePrefix("/education/schedule/ppc.php?guid=")
            val teacher = Teacher(teacherName, teacherGuid)
            val location = it.select("div.sc-item-location").text()
            lessons.add(Lesson(startTime, endTime, type, teacher, discipline, location))

        }
        return lessons
    }
}
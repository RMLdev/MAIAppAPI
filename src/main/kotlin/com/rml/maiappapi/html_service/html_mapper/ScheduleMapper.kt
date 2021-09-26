package com.rml.maiappapi.html_service.html_mapper

import com.rml.maiappapi.domain.*
import org.jsoup.select.Elements
import java.text.SimpleDateFormat
import java.util.*

class ScheduleMapper {

    fun htmlDayToDay(elements: Elements): Day {
        val date = getDateFromHtmlDay(elements)
        val lessons = getLessonsFromHtmlDay(elements)
        return Day(date, lessons as ArrayList<Lesson>)
    }

    fun htmlTeacherDayToTeacherDay(elements: Elements): Day {
        val date = getDateFromHtmlDay(elements)
        val lessons = getTeacherLessonsFromHtmlDay(elements)
        return Day(date, lessons as ArrayList<Lesson>)
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

    private fun getLessonsFromHtmlDay(day: Elements): ArrayList<StudentLesson> {
        val lessonsHtmlInfo =
            day.select("div.sc-table-detail-container").select("div.sc-table-detail").select("div.sc-table-row")
        val lessons = arrayListOf<StudentLesson>()
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
            lessons.add(StudentLesson(startTime, endTime, type, teacher, discipline, location))
        }
        return lessons
    }

    private fun getTeacherLessonsFromHtmlDay(day: Elements): ArrayList<TeacherLesson> {
        val lessonsHtmlInfo =
            day.select("div.sc-table-detail-container").select("div.sc-table-detail").select("div.sc-table-row")
        val lessons = arrayListOf<TeacherLesson>()
        lessonsHtmlInfo.forEach { it ->
            val timeString = it.select("div.sc-item-time").text()
            val startTimeString = timeString.substring(0, 5)
            val endTimeString = timeString.substring(7, timeString.length)
            val timeFormat = SimpleDateFormat("HH:mm")
            val startTime = timeFormat.parse(startTimeString)
            val endTime = timeFormat.parse(endTimeString)
            val type = it.select("div.sc-item-type").text()
            val discipline = it.select("div.sc-item-title-body").select("span.sc-title").text()
            val groupsString = it.select("a[href]").select("span").text()
            val groupsList = mutableListOf<Group>()
            var groupName = ""
            for (charIndex in groupsString.indices) {
                val char = groupsString[charIndex]
                if (char != ' ') {
                    groupName += char
                } else {
                    groupsList.add(Group(groupName))
                    groupName = ""
                }
                if (charIndex == groupsString.lastIndex) {
                    groupsList.add(Group(groupName))
                    groupName = ""
                }
            }
            val location = it.select("div.sc-item-location").text()
            lessons.add(TeacherLesson(startTime, endTime, type, groupsList, discipline, location))
        }
        return lessons
    }
}
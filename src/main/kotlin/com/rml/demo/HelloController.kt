package com.rml.demo

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/mobileApi")
class HelloController {

    @RequestMapping(value = ["/schedule/getSchedule"], method = [(RequestMethod.GET)])
    fun getSchedule(
        @RequestParam ("groupName" )groupName: String,
        @RequestParam ("week" ) week: Int
    ): ResponseEntity<Any> {
        val html = Jsoup.connect("https://mai.ru/education/schedule/detail.php?group=$groupName&week=$week").get()
        val body = html.body()
        val days = getHtmlDays(body)
        val response = getDays(days)
        return ResponseEntity.ok(response.map { it.toResponse() })
    }

    @RequestMapping(value = ["/getGroupsList"], method = [(RequestMethod.GET)])
    fun getGroupsList(): ResponseEntity<Any> {
        val html = Jsoup.connect("https://mai.ru/education/schedule/").get()
        val body = html.body()
        val mainBody = body.select("#mainbody")
        val mobi = mainBody
            .select("div.j-bg-mobi")
        val container = mobi
            .select("div.container")
        val coursesList = mutableListOf<Course>()
        val courses = container.select("div#schedule-content").select("div.sc-container")
        courses.forEach {
            val courseName = it.select("h5")
            val courseNumber = courseName.text().removeSuffix(" курс").toInt()
            val departments = mutableListOf<Department>()
            val departmentsHtml = it.select("div.sc-table-row")
            departmentsHtml.forEach {
                val departmentName = it.select("a.sc-table-col").text()
                val departmentNumber = departmentName.removePrefix("Институт №").toInt()
                val groupsHtml = it.select("div.sc-table-col").select("div.sc-groups")
                val groupsList = mutableListOf<Group>()
                groupsHtml.forEach {
                    val studyProgram = it.select("div.sc-program").text()
                    it.select("a.sc-group-item").forEach {
                        groupsList.add(Group(studyProgram, it.text()))
                    }
                }
                departments.add(Department(departmentNumber, "", groupsList))
            }
            coursesList.add(Course(courseNumber, departments))
        }
        return ResponseEntity.ok(coursesList)
    }

    private fun getDays(htmlDays: List<Elements>): List<Day> {
        val days = mutableListOf<Day>()
        htmlDays.forEach {
            val date = getDateFromHtmlDay(it)
            val lessons = getLessonsFromHtmlDay(it)
            days.add(Day(date, lessons))
        }
        return days
    }

    private fun getHtmlDays(body: Element): List<Elements> {
        val mainBody = body.select("#mainbody")
        val mobi = mainBody
            .select("div.j-bg-mobi")
        val container = mobi
            .select("div.container")
        val container2 = container
            .select(":first-child")
        val content = container2
            .select("#schedule-content").select(":first-child")
        val days = mutableListOf<Elements>()
        content
            .select("div.sc-container").forEach {

                days.add(it.select("div:first-child").select("div:first-child"))
            }
        return days
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

data class Day(
    val date: Date,
    val lessons: ArrayList<Lesson>
) {
    fun toResponse(): DayResponse {
        val dateFormat = SimpleDateFormat("dd.MM")
        val weekFormat = SimpleDateFormat("EEE")
        val date = dateFormat.format(this.date)
        val dayOfWeek = weekFormat.format(this.date)
        return DayResponse(date, dayOfWeek, lessons.map { it.toResponse() } as ArrayList<LessonResponse>)
    }
}

data class DayResponse(
    val date: String,
    val dayOfWeek: String,
    val lessons: ArrayList<LessonResponse>
)

data class LessonResponse(
    val timeStart: String,
    val timeEnd: String,
    val type: String,
    val teacher: Teacher,
    val discipline: String,
    val location: String
)

data class Lesson(
    val timeStart: Date,
    val timeEnd: Date,
    val type: String,
    val teacher: Teacher,
    val discipline: String,
    val location: String
) {
    fun toResponse(): LessonResponse {
        val timeFormat = SimpleDateFormat("HH:mm")
        return LessonResponse(
            timeFormat.format(timeStart),
            timeFormat.format(timeEnd),
            type,
            teacher,
            discipline,
            location
        )
    }
}

data class Teacher(val fullName: String, val guid: String)

data class Course(val number: Int, val departments: List<Department>)
data class Department(val number: Int, val logoUri: String, val list: List<Group>)
data class Group(val studyProgram: String, val name: String)

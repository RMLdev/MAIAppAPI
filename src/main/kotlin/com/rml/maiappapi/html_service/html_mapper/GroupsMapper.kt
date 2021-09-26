package com.rml.maiappapi.html_service.html_mapper

import com.rml.maiappapi.domain.Course
import com.rml.maiappapi.domain.Department
import com.rml.maiappapi.domain.Group
import com.rml.maiappapi.domain.StudyProgram
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class GroupsMapper {

    fun htmlCourseToCourse(element: Element): Course {
        val courseName = element.select("h5")
        val courseNumber = courseName.text().removeSuffix(" курс").toInt()
        val departmentsList = element.select("div.sc-table-row").map(this::htmlDepartmentToDepartment)
        return Course(courseNumber, departmentsList)
    }

    private fun htmlDepartmentToDepartment(element: Element): Department {
        val departmentName = element.select("a.sc-table-col").text()
        val departmentNumber = departmentName.removePrefix("Институт №").toInt()
        val groupsMap = htmlGroupsToGroupsMap(element.select("div.sc-table-col").select("div.sc-groups"))
        return Department(departmentNumber, departmentName, groupsMap)
    }

    private fun htmlGroupsToGroupsMap(elements: Elements): Map<StudyProgram, List<Group>> {
        val postgradGroups = mutableListOf<Group>()
        val bachelorGroups = mutableListOf<Group>()
        val magistracyGroups = mutableListOf<Group>()
        val specialtyGroups = mutableListOf<Group>()
        val unsortedGroups = mutableListOf<Group>()
        elements.forEach {
            val studyProgramName = it.select("div.sc-program").text()
            val studyProgram = StudyProgram.getInstance(studyProgramName)
            it.select("a.sc-group-item").forEach {
                val group = Group(it.text())
                when (studyProgram) {
                    StudyProgram.POSTGRAD -> postgradGroups.add(group)
                    StudyProgram.BACHELOR -> bachelorGroups.add(group)
                    StudyProgram.MAGISTRACY -> magistracyGroups.add(group)
                    StudyProgram.SPECIALITY -> specialtyGroups.add(group)
                    StudyProgram.UNKNOWN -> unsortedGroups.add(group)
                }
            }
        }
        val map: MutableMap<StudyProgram, List<Group>> = mutableMapOf()
        map.apply {
            if (postgradGroups.isNotEmpty()) this[StudyProgram.POSTGRAD] = postgradGroups
            if (bachelorGroups.isNotEmpty()) this[StudyProgram.BACHELOR] = bachelorGroups
            if (magistracyGroups.isNotEmpty()) this[StudyProgram.MAGISTRACY] = magistracyGroups
            if (specialtyGroups.isNotEmpty()) this[StudyProgram.SPECIALITY] = specialtyGroups
            if (postgradGroups.isNotEmpty()) this[StudyProgram.POSTGRAD] = postgradGroups
        }

        return map
    }
}
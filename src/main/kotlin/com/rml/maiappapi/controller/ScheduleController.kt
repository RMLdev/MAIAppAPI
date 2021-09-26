package com.rml.maiappapi.controller

import com.rml.maiappapi.constants.APIConstants.BASE_URL_V1
import com.rml.maiappapi.constants.APIConstants.SCHEDULE
import com.rml.maiappapi.repository.ScheduleRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("${BASE_URL_V1}/${SCHEDULE}")
class ScheduleController {

    private val repository: ScheduleRepository = ScheduleRepository()

    @RequestMapping(value = ["/getSchedule"], method = [(RequestMethod.GET)])
    fun getSchedule(
        @RequestParam("groupName") groupName: String,
        @RequestParam("week") week: Int
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(repository.getSchedule(groupName, week).map { it.toResponse() })
    }

    @RequestMapping(value = ["/getTeacherSchedule"], method = [(RequestMethod.GET)])
    fun getTeacherSchedule(
        @RequestParam("guid") guid: String,
        @RequestParam("week") week: Int
    ):ResponseEntity<Any> {
        return ResponseEntity.ok(repository.getTestTeacherSchedule(guid, week).map { it.toResponse() })
    }
}
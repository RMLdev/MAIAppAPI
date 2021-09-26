package com.rml.maiappapi.controller

import com.rml.maiappapi.constants.APIConstants.BASE_URL_V1
import com.rml.maiappapi.constants.APIConstants.GROUPS
import com.rml.maiappapi.domain.Course
import com.rml.maiappapi.repository.GroupsRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BASE_URL_V1/$GROUPS")
class GroupsController {

    private val groupsRepository = GroupsRepository()

    @RequestMapping(value = ["/getGroupsList"], method = [(RequestMethod.GET)])
    fun getGroupsList(): ResponseEntity<List<Course>> {
        return ResponseEntity.ok(groupsRepository.getAllCourses())
    }
}
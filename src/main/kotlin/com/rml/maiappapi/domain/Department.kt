package com.rml.maiappapi.domain

data class Department(val number: Int, val logoUri: String, val map: Map<StudyProgram, List<Group>>)
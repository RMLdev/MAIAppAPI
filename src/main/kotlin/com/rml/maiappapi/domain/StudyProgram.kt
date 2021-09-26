package com.rml.maiappapi.domain

enum class StudyProgram {
    POSTGRAD,
    BACHELOR,
    MAGISTRACY,
    SPECIALITY,
    UNKNOWN;

    companion object {
        fun getInstance(name: String): StudyProgram =
            when (name) {
                "Аспирантура" -> POSTGRAD
                "Бакалавриат" -> BACHELOR
                "Магистратура" -> MAGISTRACY
                "Специалитет" -> SPECIALITY
                else -> UNKNOWN
            }
    }
}
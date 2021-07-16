package com.rml.demo

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class HtmlGroupsParser (doc: Document) : HtmlElementsParser(doc) {
    fun getHtmlCoursesList(): List<Elements> {
        val courses = mutableListOf<Elements>()
        content.select("div.sc-container").forEach {

        }
        return listOf()
    }
}
package com.rml.demo

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class HtmlScheduleParser(doc: Document) : HtmlElementsParser(doc) {
    fun getHtmlStudyDays(): List<Elements> {
        val days = mutableListOf<Elements>()
        content
            .select("div.sc-container").forEach {
                days.add(it.select("div:first-child").select("div:first-child"))
            }
        return days
    }
}
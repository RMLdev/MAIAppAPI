package com.rml.demo

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

abstract class HtmlElementsParser(private val htmlDoc: Document) {
    protected val content : Elements get() = htmlDoc
        .body()
        .select("#schedule-content")
        .select(":first-child")
}
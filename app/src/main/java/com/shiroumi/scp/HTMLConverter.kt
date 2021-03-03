package com.shiroumi.scp

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object HTMLConverter {
    fun byAttr(html: String, attribute: String, name: String): Elements {
        val document = Jsoup.parse(html)
        return document.html(html).getElementsByAttributeValue(attribute, name)
    }

    fun byClass(html: String, className: String, name: String): Elements {
        val document = Jsoup.parse(html)
        return document.html(html).getElementsByClass(className)
    }

    fun byTag(html: String, tagName: String): Elements {
        val document = Jsoup.parse(html)
        return document.html(html).getElementsByTag(tagName)
    }

    fun byId(html: String, id: String): Element {
        val document = Jsoup.parse(html)
        return document.html(html).getElementById(id)
    }
}
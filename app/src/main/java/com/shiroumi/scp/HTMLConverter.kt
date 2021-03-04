package com.shiroumi.scp

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

fun String.elementById(id: String): Element {
    return Jsoup.parse(this).getElementById(id)
}

fun String.elementByAttr(attribute: String, name: String): Elements {
    return Jsoup.parse(this).getElementsByAttributeValue(attribute, name)
}

fun String.elementByClass(clazz: String): Elements {
    return Jsoup.parse(this).getElementsByClass(clazz)
}

fun String.elementByTag(tag: String, name: String): Elements {
    return Jsoup.parse(this).getElementsByTag(tag)
}

fun String.getBody(): Document {
    return Jsoup.parse(this)
}
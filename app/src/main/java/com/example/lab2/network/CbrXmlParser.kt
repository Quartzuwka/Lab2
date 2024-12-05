package com.example.lab2.network

import com.example.lab2.network.CurrencyItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.FileInputStream

private const val TAG = "FeedParser"

class CbrXmlParser {
    private val pullParserFactory = XmlPullParserFactory.newInstance()
    private val parser = pullParserFactory.newPullParser()

    fun parsing(xml: String): List<CurrencyItem> {

        parser.setInput(xml.byteInputStream(), "UTF-8")

        val CurrencyList = mutableListOf<CurrencyItem>()

        var feedTitle = ""

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {

             if (parser.eventType == XmlPullParser.START_TAG && parser.name == "Valute") {
                val CurItem = readFeedItem(parser)
                CurrencyList.add(CurItem)
            }

            parser.next()
        }

        return CurrencyList
    }

    private fun readFeedItem(parser: XmlPullParser): CurrencyItem {
        var value = 0F
        var nominal = 0
        var name = ""
        var numToFloat = ""
        var charCode = ""

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "Value") {
                numToFloat = readText(parser)
                val numberWithDot = numToFloat.replace(',', '.')
                value = numberWithDot.toFloat()
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "Nominal") {
                nominal = readText(parser).toInt()
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "Name") {
                name = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "CharCode") {
                charCode = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG) {
                skipTag(parser)
            }
        }

        return CurrencyItem(value, nominal, name, charCode)
    }

    private fun readText(parser: XmlPullParser): String {
        var text = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType == XmlPullParser.TEXT) {
                text = parser.text
            }
        }
        return text
    }

    @Suppress("ControlFlowWithEmptyBody")
    private fun skipTag(parser: XmlPullParser) {
        while (parser.next() != XmlPullParser.END_TAG) {
        }
    }
}

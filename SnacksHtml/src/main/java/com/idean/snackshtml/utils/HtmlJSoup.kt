package com.idean.snackshtml.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.text.style.*
import android.view.ViewGroup
import com.idean.snackshtml.utils.css.ParserCSS
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.jsoup.select.Elements

/**
 * Created by Mickael Calatraba on 3/19/21.
 */

/**
 * This class processes HTML strings into displayable styled text.
 * Not all HTML tags are supported.
 */
class HtmlJSoup(private var context: Context?) {
    private var textHandler: TextGetter? = null


    fun fromHtml(source: String?, textHandler: TextGetter, imageGetter: ImageGetter) {
        this.textHandler = textHandler
        val parser = Jsoup.parse(source, "", Parser.xmlParser())
        val converter = HtmlParsingJsoup(source, imageGetter, textHandler, parser)
        converter.convert(context!!)
        this.context = null
    }

    /**
     * Retrieves images from HTML img; tags.
     */
    interface ImageGetter {
        /**
         * This method is called when the HTML parser encounters an
         * img; tag.
         */
        fun getDrawable(source: String?, info: ImageInfo)
    }

    /**
     * Retrieves text from HTML.
     */
    interface TextGetter {
        fun setText(text: Spannable, info: CSSData?)
    }
}

data class ImageInfo(
    val imageUrl: String,
    var width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    var description: String? = null,
    val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
)

data class CSSData(
    var fontFace: Typeface? = null,
    var textSize: Float = 12f,
    var lineHeight: Float = 27f,
    var textColor: Int = Color.RED,
    var spanStyle: StyleSpan? = null,
    var marginTop: Int = 0,
    var marginBottom: Int = 0,
    var marginStart: Int = 0,
    var marginEnd: Int = 0,
    var paddingTop: Int = 0,
    var paddingBottom: Int = 0,
    var paddingStart: Int = 0,
    var paddingEnd: Int = 0,
)


internal class HtmlParsingJsoup(
    private var source: String?,
    private val imageGetter: HtmlJSoup.ImageGetter?,
    private val textHandler: HtmlJSoup.TextGetter?,
    private val parser: Document
) {

    companion object {
        private const val TAG = "HtmlParsingJsoup"
    }

    private var parserCss: ParserCSS? = null
    private val cssStyle = mutableMapOf(
        "body" to CSSData(textSize = 16f, lineHeight = 28f, marginBottom = 8, marginEnd = 8, marginStart = 8, marginTop = 8),
        "h1" to CSSData(textSize = 32f, lineHeight = 30f, marginBottom = 65, spanStyle = StyleSpan(Typeface.BOLD)),
        "h2" to CSSData(textSize = 24f, lineHeight = 27f, marginBottom = 80, spanStyle = StyleSpan(Typeface.BOLD)),
        "h3" to CSSData(textSize = 18.72f, lineHeight = 27f, marginBottom = 100, spanStyle = StyleSpan(Typeface.BOLD)),
        "h4" to CSSData(textSize = 16f, lineHeight = 18f, marginBottom = 100, spanStyle = StyleSpan(Typeface.BOLD)),
        "h5" to CSSData(textSize = 13.28f, lineHeight = 10f, marginBottom = 120, spanStyle = StyleSpan(Typeface.BOLD)),
        "h6" to CSSData(textSize = 10.72f, lineHeight = 12f, marginBottom = 120, spanStyle = StyleSpan(Typeface.BOLD)),
        "p" to CSSData(textSize = 16f, lineHeight = 27f, marginBottom = 90),
    )

    fun convert(context: Context) {
        this.parserCss = ParserCSS(context)

        cleanHtml(this.source)
        getHeaderCSS()

        this.parser.body().select("*").forEach { element ->
            when (val tag = element.normalName()) {
                "body" -> setBodyGlobalStyle()
                "h1", "h2", "h3", "h4", "h5", "h6" -> convertText(element.getElementsByTag(tag))
                "p" -> convertText(element.getElementsByTag(tag))
                "img" -> convertImage(element)
                else -> { // Custom class

                }
            }
        }
    }

    fun setBodyGlobalStyle() {
        if (this.cssStyle.containsKey("body")) {
            this.cssStyle.keys.forEach { key ->
                if (this.cssStyle[key]?.fontFace == null) {
                    this.cssStyle[key]?.fontFace = this.cssStyle["body"]?.fontFace
                }
            }
        }
    }

    fun cleanHtml(source: String?) {
        this.source = source?.replace("</br>", "\n")
    }

    fun getHeaderCSS() {
        this.parser.head()?.let { head ->
            this.parserCss?.parseHeaderCss(head, this.cssStyle)
        }
    }

    private fun convertImage(element: Element) {
        val info = this.parserCss?.getImageProperties(element)!!
        this.imageGetter?.getDrawable(info.imageUrl, info)
    }

    private fun convertText(elements: Elements) {
        val text = elements.eachText().joinToString()
        val span = SpannableStringBuilder(text)

        elements.select("*").forEach { element ->
            this.parserCss?.findInlineTextStyle(span, element, element.normalName())

            parseStyleTextTag(span, element)
        }
        val tag = elements.first().normalName()
        val info = this.cssStyle[tag]
        this.textHandler?.setText(span, info)
    }


    private fun parseStyleTextTag(span: Spannable, element: Element) {
        val tag = element.normalName()
        val src = element.getElementsByTag(tag).text()

        when (tag) {
            "b", "strong" -> span.setSpan(src, StyleSpan(Typeface.BOLD))
            "i" -> span.setSpan(src, StyleSpan(Typeface.ITALIC))
            "u" -> span.setSpan(src, UnderlineSpan())
            else -> {
                // Not a Style text
            }
        }
    }
}

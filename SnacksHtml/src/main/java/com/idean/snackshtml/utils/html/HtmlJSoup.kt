package com.idean.snackshtml.utils.html

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.text.style.*
import android.view.ViewGroup
import com.idean.snackshtml.utils.css.ParserCSS
import com.idean.snackshtml.utils.text.setSpan
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
        fun getDrawable(source: String?, info: ImageStyle)
    }

    /**
     * Retrieves text from HTML.
     */
    interface TextGetter {
        fun setText(spanText: Spannable, info: CSSStyle?)
    }
}

data class ImageStyle(
    val imageUrl: String,
    var width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    var description: String? = null,
    val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
)

data class CSSStyle(
    var fontFace: Typeface? = null,
    var textSize: Float = 12f,
    var lineHeight: Float = 27f,
    var textColor: Int = Color.BLACK,
    var spanStyle: StyleSpan? = null,
    var linkStyle: LinkStyle? = null,
    var marginTop: Int = 0,
    var marginBottom: Int = 0,
    var marginStart: Int = 0,
    var marginEnd: Int = 0,
    var paddingTop: Int = 0,
    var paddingBottom: Int = 0,
    var paddingStart: Int = 0,
    var paddingEnd: Int = 0,
)

data class LinkStyle(
    var href: String? = null,
    var linkColor: Int = Color.parseColor("#0000EE"),
    var visitedLinkColor: Int = Color.parseColor("#551A8B")
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
        "body" to CSSStyle(textSize = 16f, lineHeight = 28f, marginBottom = 8, marginEnd = 8, marginStart = 8, marginTop = 8),
        "h1" to CSSStyle(textSize = 32f, lineHeight = 30f, marginBottom = 65, spanStyle = StyleSpan(Typeface.BOLD)),
        "h2" to CSSStyle(textSize = 24f, lineHeight = 27f, marginBottom = 80, spanStyle = StyleSpan(Typeface.BOLD)),
        "h3" to CSSStyle(textSize = 18.72f, lineHeight = 27f, marginBottom = 100, spanStyle = StyleSpan(Typeface.BOLD)),
        "h4" to CSSStyle(textSize = 16f, lineHeight = 18f, marginBottom = 100, spanStyle = StyleSpan(Typeface.BOLD)),
        "h5" to CSSStyle(textSize = 13.28f, lineHeight = 10f, marginBottom = 120, spanStyle = StyleSpan(Typeface.BOLD)),
        "h6" to CSSStyle(textSize = 10.72f, lineHeight = 12f, marginBottom = 120, spanStyle = StyleSpan(Typeface.BOLD)),
        "p" to CSSStyle(textSize = 16f, lineHeight = 27f, marginBottom = 90),
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
        val tag = elements.first().normalName()
        val info = this.cssStyle[tag]

        elements.select("*").forEach { element ->
            this.parserCss?.findInlineTextStyle(span, element, element.normalName())
            parseStyleTextTag(span, element)?.let { linkStyle ->
                info?.linkStyle = linkStyle
            }
        }
        this.textHandler?.setText(span, info)
    }


    private fun parseStyleTextTag(span: Spannable, element: Element): LinkStyle? {
        val tag = element.normalName()
        val src = element.getElementsByTag(tag).text()

        when (tag) {
            "b", "strong" -> span.setSpan(src, StyleSpan(Typeface.BOLD))
            "i", "cite", "em", "var", "address" -> span.setSpan(src, StyleSpan(Typeface.ITALIC))
            "u", "ins" -> span.setSpan(src, UnderlineSpan())
            "s", "strike", "del" -> span.setSpan(src, StrikethroughSpan())
            "a" -> {
                val linkStyle = this.parserCss?.parseHyperLink(this.cssStyle["a"], element)
                span.setSpan(src, URLSpan(linkStyle?.href))
                return linkStyle
            }
            else -> {
                // Not a Style text supported
            }
        }
        return null
    }
}

package com.idean.snackshtml.utils.css

import android.content.Context
import android.text.Layout
import android.text.Spannable
import android.text.style.AlignmentSpan
import android.util.Log
import com.idean.snackshtml.errors.MalformedCSSException
import com.idean.snackshtml.errors.MissingCSSMandatoryFieldException
import com.idean.snackshtml.errors.UnsupportedFieldException
import com.idean.snackshtml.utils.html.CSSStyle
import com.idean.snackshtml.utils.html.ImageStyle
import com.idean.snackshtml.utils.css.colors.ColorParser
import com.idean.snackshtml.utils.html.LinkStyle
import com.idean.snackshtml.utils.text.setSpan
import org.jsoup.nodes.Element
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringWriter
import java.util.regex.Pattern


/**
 * Created by Mickael Calatraba on 3/24/21.
 */

class ParserCSS(private val context: Context) : AbstractCSSParser() {
    companion object {
        private const val TAG = "ParserCSS"
    }

    private val parser = ParserCSSFontFace()

    //TODO TU
    fun parseHeaderCss(head: Element, cssStyle: MutableMap<String, CSSStyle>) {
        getCSSStyleFile(head)?.let { cssFile ->
            val result = getStringFromInputStream(cssFile)
            val cssWithoutFont = this.parser.findFonts(this.context, result)
            findCssClasses(cssWithoutFont, cssStyle)
        }
    }

    //TODO TU
    fun findCssClasses(css: String, cssStyle: MutableMap<String, CSSStyle>) {
        val m = Pattern.compile("(.*?)+(\\s?)+(\\{(?:\\{?[^\\[]*?\\}))").matcher(css)
        while (m.find()) {
            try {
                val section = m.group(0)
                val rawClassName = getClassName(section)
                val className = getClassName(section)
                cssStyle[className] = parseCssClass(className, cssStyle, getAttributes(section))
            } catch (e: UnsupportedFieldException) {
                Log.e(TAG, Log.getStackTraceString(e))
            } catch (e: MissingCSSMandatoryFieldException) {
                Log.e(TAG, Log.getStackTraceString(e))
            } catch (e: MalformedCSSException) {
                Log.e(TAG, Log.getStackTraceString(e))
            }
        }
    }

    /**
     * Get the class name in the CSS.
     * Example:
     * h1 {
     *   font-family: 'ubuntu_light';
     *   font-size: 30px;
     *   line-height: 32px;
     * }
     * The result is "h1" trimmed.
     * @param section: This is the block of the css class (see example)
     *
     * @return Return the class name trimmed
     */
    //TODO TU
    @Throws(MalformedCSSException::class)
    fun getClassName(section: String?): String {
        try {
            val end = section!!.indexOf('{')
            return section.substring(0, end).trim()
        } catch (e: Exception) {
            throw MalformedCSSException(section ?: "class cannot be null")
        }
    }

    @Throws(UnsupportedFieldException::class)
    fun parseCssClass(className: String, cssStyle: MutableMap<String, CSSStyle>, attributes: List<String>): CSSStyle {
        val result = cssStyle[className] ?: CSSStyle()

        attributes.forEach {
            val value = getValue(it)
            when (val key = getKey(it)) {
                "font-family" -> result.fontFace = this.parser.fontFaces[getFontFamilyName(value)]
                "line-height" -> result.lineHeight = getValueWithoutUnit(value)
                "font-size" -> result.textSize = getValueWithoutUnit(value)
                // Margins
                "margin" -> {
                    result.marginStart = getValueWithoutUnit(value).toInt()
                    result.marginEnd = getValueWithoutUnit(value).toInt()
                    result.marginTop = getValueWithoutUnit(value).toInt()
                    result.marginBottom = getValueWithoutUnit(value).toInt()
                }
                "margin-left" -> result.marginStart = getValueWithoutUnit(value).toInt()
                "margin-right" -> result.marginEnd = getValueWithoutUnit(value).toInt()
                "margin-top" -> result.marginTop = getValueWithoutUnit(value).toInt()
                "margin-bottom" -> result.marginBottom = getValueWithoutUnit(value).toInt()
                // Padding
                "padding" -> {
                    result.paddingStart = getValueWithoutUnit(value).toInt()
                    result.paddingEnd = getValueWithoutUnit(value).toInt()
                    result.paddingTop = getValueWithoutUnit(value).toInt()
                    result.paddingBottom = getValueWithoutUnit(value).toInt()
                }
                "padding-left" -> result.paddingStart = getValueWithoutUnit(value).toInt()
                "padding-right" -> result.paddingEnd = getValueWithoutUnit(value).toInt()
                "padding-top" -> result.paddingTop = getValueWithoutUnit(value).toInt()
                "padding-bottom" -> result.paddingBottom = getValueWithoutUnit(value).toInt()
                "color" -> {
                    if (className == "a" || className.contains("a:")) {
                        // Hyperlink colors
                        result.linkStyle = parseHyperlinkCSS(className, value)
                    } else {
                        // Simple text color
                        result.textColor = ColorParser().parseColor(value)
                    }
                }

                else -> throw UnsupportedFieldException(key)
            }
        }
        return result
    }

    fun parseHyperlinkCSS(className: String, color: String): LinkStyle {
        val linkStyle = LinkStyle()

        if (className == "a") {
            linkStyle.linkColor = ColorParser().parseColor(color)
        } else {
            when (className.split(":")[1]) {
                "link" -> linkStyle.linkColor = ColorParser().parseColor(color)
                "visited" -> linkStyle.visitedLinkColor = ColorParser().parseColor(color)
                else -> {
                    // hover and active are not usable in android
                }
            }
        }
        return linkStyle
    }

    //TODO TU
    fun parseHyperLink(cssStyle: CSSStyle?, element: Element): LinkStyle {
        val href = element.attr("abs:href")

        return (cssStyle?.linkStyle ?: LinkStyle()).apply {
            this.href = href
        }
    }

    /**
     * Remove the unit and return the value cast to float
     *
     * @param value: this is the property value, ex: 20px
     *
     * @return Return the value without the unit.
     * todo: get the unit and convert it in pixel
     */
    //TODO TU
    fun getValueWithoutUnit(value: String): Float {
        return value.replace(Regex("[^0-9]"), "").trim().toFloat()
    }

    private fun getCSSStyleFile(head: Element): InputStream? {
        val headLink = head.select("link")

        if (headLink.hasAttr("type") && headLink.attr("type") == "text/css") {
            return headLink.attr("href")?.let { path ->
                val assetPos = path.indexOf("asset/") + "asset/".length
                getFileFromAssets(path.substring(assetPos))
            }
        }
        return null
    }

    @Throws(IOException::class)
    fun getStringFromInputStream(stream: InputStream?): String {
        var n = 0
        val buffer = CharArray(1024 * 4)
        val reader = InputStreamReader(stream, "UTF8")
        val writer = StringWriter()
        while (-1 != reader.read(buffer).also { n = it }) {
            writer.write(buffer, 0, n)
        }
        return writer.toString()
    }

    @Throws(IOException::class)
    fun getFileFromAssets(fileName: String): InputStream {
        return this.context.assets.open(fileName)
    }

    fun findInlineTextStyle(span: Spannable, element: Element, className: String) {
        val item = element.select(className)
        val tag = element.normalName()
        val src = element.getElementsByTag(tag).text()
        if (item != null) {
            try {
                val styles = parseStyles(item.attr("style"))
                styles?.forEach {
                    when (it.first) {
                        "text-align" -> setTextAlignment(span, src, it.second)
                        else -> {
                            // Unknown style :(
                        }
                    }
                }
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
    }

    fun getImageProperties(element: Element): ImageStyle {
        val src = element.attr("src")
        val result = ImageStyle(src)
        element.attr("height").takeIf { it.isNotEmpty() }?.let {
            result.height = it.toInt()
        }
        element.attr("width").takeIf { it.isNotEmpty() }?.let {
            result.width = it.toInt()
        }
        return result
    }

    private fun setTextAlignment(span: Spannable, src: String, param: String) {
        when (param) {
            "center" -> span.setSpan(src, AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER))
            "end" -> span.setSpan(src, AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE))
            else -> span.setSpan(src, AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL))
        }
    }

    private fun parseStyles(styles: String?): List<Pair<String, String>>? {
        return styles?.split(";")?.mapNotNull { item ->
            item.split(":").takeIf { it.size == 2 }?.let { style ->
                val key = style[0].trim()
                val value = style[1].trim()
                Pair(key, value)
            } ?: kotlin.run { null }
        }
    }
}
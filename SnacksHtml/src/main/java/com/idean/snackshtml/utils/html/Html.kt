package com.idean.snackshtml.utils.html
//
//import android.graphics.Typeface
//import android.text.*
//import android.graphics.Color;
//import android.text.style.AbsoluteSizeSpan;
//import android.text.style.AlignmentSpan;
//import android.text.style.BackgroundColorSpan;
//import android.text.style.BulletSpan;
//import android.text.style.CharacterStyle;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.ImageSpan;
//import android.text.style.ParagraphStyle;
//import android.text.style.QuoteSpan;
//import android.text.style.RelativeSizeSpan;
//import android.text.style.StrikethroughSpan;
//import android.text.style.StyleSpan;
//import android.text.style.SubscriptSpan;
//import android.text.style.SuperscriptSpan;
//import android.text.style.TypefaceSpan;
//import android.text.style.URLSpan;
//import android.text.style.UnderlineSpan;
//import android.util.Log
//import androidx.core.text.toSpannable
//import org.ccil.cowan.tagsoup.HTMLSchema
//import org.ccil.cowan.tagsoup.Parser.schemaProperty
//import org.xml.sax.*
//import java.io.IOException
//import java.io.StringReader
//import java.util.*
//import java.util.regex.Matcher
//import java.util.regex.Pattern
//
///**
// * Created by Mickael Calatraba on 3/19/21.
// * Copyright (c) 2021 Idean. All rights reserved.
// */
//
///**
// * This class processes HTML strings into displayable styled text.
// * Not all HTML tags are supported.
// */
//object Html {
//    /**
//     * Option for [.toHtml]: Wrap consecutive lines of text delimited by '\n'
//     * inside &lt;p&gt; elements. [BulletSpan]s are ignored.
//     */
//    const val TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0x00000000
//
//    /**
//     * Option for [.toHtml]: Wrap each line of text delimited by '\n' inside a
//     * &lt;p&gt; or a &lt;li&gt; element. This allows [ParagraphStyle]s attached to be
//     * encoded as CSS styles within the corresponding &lt;p&gt; or &lt;li&gt; element.
//     */
//    const val TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 0x00000001
//
//    /**
//     * Flag indicating that texts inside &lt;p&gt; elements will be separated from other texts with
//     * one newline character by default.
//     */
//    const val FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 0x00000001
//
//    /**
//     * Flag indicating that texts inside &lt;h1&gt;~&lt;h6&gt; elements will be separated from
//     * other texts with one newline character by default.
//     */
//    const val FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 0x00000002
//
//    /**
//     * Flag indicating that texts inside &lt;li&gt; elements will be separated from other texts
//     * with one newline character by default.
//     */
//    const val FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 0x00000004
//
//    /**
//     * Flag indicating that texts inside &lt;ul&gt; elements will be separated from other texts
//     * with one newline character by default.
//     */
//    const val FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 0x00000008
//
//    /**
//     * Flag indicating that texts inside &lt;div&gt; elements will be separated from other texts
//     * with one newline character by default.
//     */
//    const val FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 0x00000010
//
//    /**
//     * Flag indicating that texts inside &lt;blockquote&gt; elements will be separated from other
//     * texts with one newline character by default.
//     */
//    const val FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 0x00000020
//
//    /**
//     * Flag indicating that CSS color values should be used instead of those defined in
//     * [Color].
//     */
//    const val FROM_HTML_OPTION_USE_CSS_COLORS = 0x00000100
//
//    /**
//     * Flags for [.fromHtml]: Separate block-level
//     * elements with blank lines (two newline characters) in between. This is the legacy behavior
//     * prior to N.
//     */
//    const val FROM_HTML_MODE_LEGACY = 0x00000000
//
//    /**
//     * Flags for [.fromHtml]: Separate block-level
//     * elements with line breaks (single newline character) in between. This inverts the
//     * [Spanned] to HTML string conversion done with the option
//     * [.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL].
//     */
//    const val FROM_HTML_MODE_COMPACT = (FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
//            or FROM_HTML_SEPARATOR_LINE_BREAK_HEADING
//            or FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
//            or FROM_HTML_SEPARATOR_LINE_BREAK_LIST
//            or FROM_HTML_SEPARATOR_LINE_BREAK_DIV
//            or FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
//
//    /**
//     * The bit which indicates if lines delimited by '\n' will be grouped into &lt;p&gt; elements.
//     */
//    private const val TO_HTML_PARAGRAPH_FLAG = 0x00000001
//
//    private var textHandler: TextGetter? = null
//    /**
//     * Returns displayable styled text from the provided HTML string. Any &lt;img&gt; tags in the
//     * HTML will use the specified ImageGetter to request a representation of the image (use null
//     * if you don't want this) and the specified TagHandler to handle unknown tags (specify null if
//     * you don't want this).
//     *
//     *
//     * This uses TagSoup to handle real HTML, including all of the brokenness found in the wild.
//     */
//    /**
//     * Returns displayable styled text from the provided HTML string. Any &lt;img&gt; tags in the
//     * HTML will display as a generic replacement image which your program can then go through and
//     * replace with real images.
//     *
//     *
//     * This uses TagSoup to handle real HTML, including all of the brokenness found in the wild.
//     */
////    @JvmOverloads
////    fun fromHtml(
////        source: String?, flags: Int, textHandler: TextGetter, imageGetter: ImageGetter,
////        tagHandler: TagHandler? = null
////    ): Spanned {
////        this.textHandler = textHandler
//////        val parser = Parser()
////        try {
//////            parser.setProperty(Parser.schemaProperty, HtmlParser.schema)
////        } catch (e: SAXNotRecognizedException) {
////            // Should not happen.
////            throw RuntimeException(e)
////        } catch (e: SAXNotSupportedException) {
////            // Should not happen.
////            throw RuntimeException(e)
////        }
//////        val converter =
//////            HtmlToSpannedConverter(source, imageGetter, textHandler, tagHandler, parser, flags)
//////        return converter.convert()
////    }
//
//    private fun withinDiv(
//        out: StringBuilder, text: Spanned, start: Int, end: Int,
//        option: Int
//    ) {
//        var next: Int
//        var i = start
//        while (i < end) {
//            next = text.nextSpanTransition(i, end, QuoteSpan::class.java)
//            val quotes = text.getSpans(i, next, QuoteSpan::class.java)
//            for (quote in quotes) {
//                out.append("<blockquote>")
//            }
//            withinBlockquote(out, text, i, next, option)
//            for (quote in quotes) {
//                out.append("</blockquote>\n")
//            }
//            i = next
//        }
//    }
//
//    private fun getTextDirection(text: Spanned, start: Int, end: Int): String {
//        return if (TextDirectionHeuristics.FIRSTSTRONG_LTR.isRtl(text, start, end - start)) {
//            " dir=\"rtl\""
//        } else {
//            " dir=\"ltr\""
//        }
//    }
//
//    private fun getTextStyles(
//        text: Spanned, start: Int, end: Int,
//        forceNoVerticalMargin: Boolean, includeTextAlign: Boolean
//    ): String {
//        var margin: String? = null
//        var textAlign: String? = null
//        if (forceNoVerticalMargin) {
//            margin = "margin-top:0; margin-bottom:0;"
//        }
//        if (includeTextAlign) {
//            val alignmentSpans = text.getSpans(
//                start, end,
//                AlignmentSpan::class.java
//            )
//            // Only use the last AlignmentSpan with flag SPAN_PARAGRAPH
//            for (i in alignmentSpans.indices.reversed()) {
//                val s = alignmentSpans[i]
//                if (text.getSpanFlags(s) and Spanned.SPAN_PARAGRAPH == Spanned.SPAN_PARAGRAPH) {
//                    val alignment: Layout.Alignment = s.alignment
//                    if (alignment === Layout.Alignment.ALIGN_NORMAL) {
//                        textAlign = "text-align:start;"
//                    } else if (alignment === Layout.Alignment.ALIGN_CENTER) {
//                        textAlign = "text-align:center;"
//                    } else if (alignment === Layout.Alignment.ALIGN_OPPOSITE) {
//                        textAlign = "text-align:end;"
//                    }
//                    break
//                }
//            }
//        }
//        if (margin == null && textAlign == null) {
//            return ""
//        }
//        val style = StringBuilder(" style=\"")
//        if (margin != null && textAlign != null) {
//            style.append(margin).append(" ").append(textAlign)
//        } else if (margin != null) {
//            style.append(margin)
//        } else if (textAlign != null) {
//            style.append(textAlign)
//        }
//        return style.append("\"").toString()
//    }
//
//    private fun withinBlockquote(
//        out: StringBuilder, text: Spanned, start: Int, end: Int,
//        option: Int
//    ) {
//        if (option and TO_HTML_PARAGRAPH_FLAG == TO_HTML_PARAGRAPH_LINES_CONSECUTIVE) {
//            withinBlockquoteConsecutive(out, text, start, end)
//        } else {
//            withinBlockquoteIndividual(out, text, start, end)
//        }
//    }
//
//    private fun withinBlockquoteIndividual(
//        out: StringBuilder, text: Spanned, start: Int,
//        end: Int
//    ) {
//        var isInList = false
//        var next: Int
//        var i = start
//        while (i <= end) {
//            next = TextUtils.indexOf(text, '\n', i, end)
//            if (next < 0) {
//                next = end
//            }
//            if (next == i) {
//                if (isInList) {
//                    // Current paragraph is no longer a list item; close the previously opened list
//                    isInList = false
//                    out.append("</ul>\n")
//                }
//                out.append("<br>\n")
//            } else {
//                var isListItem = false
//                val paragraphStyles = text.getSpans(
//                    i, next,
//                    ParagraphStyle::class.java
//                )
//                for (paragraphStyle in paragraphStyles) {
//                    val spanFlags = text.getSpanFlags(paragraphStyle)
//                    if (spanFlags and Spanned.SPAN_PARAGRAPH == Spanned.SPAN_PARAGRAPH
//                        && paragraphStyle is BulletSpan
//                    ) {
//                        isListItem = true
//                        break
//                    }
//                }
//                if (isListItem && !isInList) {
//                    // Current paragraph is the first item in a list
//                    isInList = true
//                    out.append("<ul")
//                        .append(getTextStyles(text, i, next, true, false))
//                        .append(">\n")
//                }
//                if (isInList && !isListItem) {
//                    // Current paragraph is no longer a list item; close the previously opened list
//                    isInList = false
//                    out.append("</ul>\n")
//                }
//                val tagType = if (isListItem) "li" else "p"
//                out.append("<").append(tagType)
//                    .append(getTextDirection(text, i, next))
//                    .append(getTextStyles(text, i, next, !isListItem, true))
//                    .append(">")
//                withinParagraph(out, text, i, next)
//                out.append("</")
//                out.append(tagType)
//                out.append(">\n")
//                if (next == end && isInList) {
//                    isInList = false
//                    out.append("</ul>\n")
//                }
//            }
//            next++
//            i = next
//        }
//    }
//
//    private fun withinBlockquoteConsecutive(
//        out: StringBuilder, text: Spanned, start: Int,
//        end: Int
//    ) {
//        out.append("<p").append(getTextDirection(text, start, end)).append(">")
//        var next: Int
//        var i = start
//        while (i < end) {
//            next = TextUtils.indexOf(text, '\n', i, end)
//            if (next < 0) {
//                next = end
//            }
//            var nl = 0
//            while (next < end && text[next] == '\n') {
//                nl++
//                next++
//            }
//            withinParagraph(out, text, i, next - nl)
//            if (nl == 1) {
//                out.append("<br>\n")
//            } else {
//                for (j in 2 until nl) {
//                    out.append("<br>")
//                }
//                if (next != end) {
//                    /* Paragraph should be closed and reopened */
//                    out.append("</p>\n")
//                    out.append("<p").append(getTextDirection(text, start, end)).append(">")
//                }
//            }
//            i = next
//        }
//        out.append("</p>\n")
//    }
//
//    private fun withinParagraph(out: StringBuilder, text: Spanned, start: Int, end: Int) {
//        var next: Int
//        var i = start
//        while (i < end) {
//            next = text.nextSpanTransition(i, end, CharacterStyle::class.java)
//            val style = text.getSpans(
//                i, next,
//                CharacterStyle::class.java
//            )
//            for (j in style.indices) {
//                if (style[j] is StyleSpan) {
//                    val s = (style[j] as StyleSpan).style
//                    if (s and Typeface.BOLD != 0) {
//                        out.append("<b>")
//                    }
//                    if (s and Typeface.ITALIC != 0) {
//                        out.append("<i>")
//                    }
//                }
//                if (style[j] is TypefaceSpan) {
//                    val s = (style[j] as TypefaceSpan).family
//                    if ("monospace" == s) {
//                        out.append("<tt>")
//                    }
//                }
//                if (style[j] is SuperscriptSpan) {
//                    out.append("<sup>")
//                }
//                if (style[j] is SubscriptSpan) {
//                    out.append("<sub>")
//                }
//                if (style[j] is UnderlineSpan) {
//                    out.append("<u>")
//                }
//                if (style[j] is StrikethroughSpan) {
//                    out.append("<span style=\"text-decoration:line-through;\">")
//                }
//                if (style[j] is URLSpan) {
//                    out.append("<a href=\"")
//                    out.append((style[j] as URLSpan).url)
//                    out.append("\">")
//                }
//                if (style[j] is ImageSpan) {
//                    out.append("<img src=\"")
//                    out.append((style[j] as ImageSpan).source)
//                    out.append("\">")
//                    // Don't output the placeholder character underlying the image.
//                    i = next
//                }
//                if (style[j] is AbsoluteSizeSpan) {
//                    val s = style[j] as AbsoluteSizeSpan
//                    var sizeDip = s.size.toFloat()
//                    if (!s.dip) {
//                        // TODO
////                        val application: Application = ActivityThread.currentApplication()
////                        sizeDip /= application.resources.displayMetrics.density
//                    }
//                    // px in CSS is the equivalance of dip in Android
//                    out.append(String.format("<span style=\"font-size:%.0fpx\";>", sizeDip))
//                }
//                if (style[j] is RelativeSizeSpan) {
//                    val sizeEm = (style[j] as RelativeSizeSpan).sizeChange
//                    out.append(String.format("<span style=\"font-size:%.2fem;\">", sizeEm))
//                }
//                if (style[j] is ForegroundColorSpan) {
//                    val color = (style[j] as ForegroundColorSpan).foregroundColor
//                    out.append(String.format("<span style=\"color:#%06X;\">", 0xFFFFFF and color))
//                }
//                if (style[j] is BackgroundColorSpan) {
//                    val color = (style[j] as BackgroundColorSpan).backgroundColor
//                    out.append(
//                        String.format(
//                            "<span style=\"background-color:#%06X;\">",
//                            0xFFFFFF and color
//                        )
//                    )
//                }
//            }
//            withinStyle(out, text, i, next)
//            for (j in style.indices.reversed()) {
//                if (style[j] is BackgroundColorSpan) {
//                    out.append("</span>")
//                }
//                if (style[j] is ForegroundColorSpan) {
//                    out.append("</span>")
//                }
//                if (style[j] is RelativeSizeSpan) {
//                    out.append("</span>")
//                }
//                if (style[j] is AbsoluteSizeSpan) {
//                    out.append("</span>")
//                }
//                if (style[j] is URLSpan) {
//                    out.append("</a>")
//                }
//                if (style[j] is StrikethroughSpan) {
//                    out.append("</span>")
//                }
//                if (style[j] is UnderlineSpan) {
//                    out.append("</u>")
//                }
//                if (style[j] is SubscriptSpan) {
//                    out.append("</sub>")
//                }
//                if (style[j] is SuperscriptSpan) {
//                    out.append("</sup>")
//                }
//                if (style[j] is TypefaceSpan) {
//                    val s = (style[j] as TypefaceSpan).family
//                    if (s == "monospace") {
//                        out.append("</tt>")
//                    }
//                }
//                if (style[j] is StyleSpan) {
//                    val s = (style[j] as StyleSpan).style
//                    if (s and Typeface.BOLD != 0) {
//                        out.append("</b>")
//                    }
//                    if (s and Typeface.ITALIC != 0) {
//                        out.append("</i>")
//                    }
//                }
//            }
//            i = next
//        }
//    }
//
//    //    @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)
//    private fun withinStyle(
//        out: StringBuilder,
//        text: CharSequence,
//        start: Int,
//        end: Int
//    ) {
//        var i = start
//        while (i < end) {
//            val c = text[i]
//            if (c == '<') {
//                out.append("&lt;")
//            } else if (c == '>') {
//                out.append("&gt;")
//            } else if (c == '&') {
//                out.append("&amp;")
//            } else if (c.toInt() >= 0xD800 && c.toInt() <= 0xDFFF) {
//                if (c.toInt() < 0xDC00 && i + 1 < end) {
//                    val d = text[i + 1]
//                    if (d.toInt() >= 0xDC00 && d.toInt() <= 0xDFFF) {
//                        i++
//                        val codepoint =
//                            0x010000 or (c.toInt() - 0xD800 shl 10) or d.toInt() - 0xDC00
//                        out.append("&#").append(codepoint).append(";")
//                    }
//                }
//            } else if (c.toInt() > 0x7E || c < ' ') {
//                out.append("&#").append(c.toInt()).append(";")
//            } else if (c == ' ') {
//                while (i + 1 < end && text[i + 1] == ' ') {
//                    out.append("&nbsp;")
//                    i++
//                }
//                out.append(' ')
//            } else {
//                out.append(c)
//            }
//            i++
//        }
//    }
//
//    data class ImageInfo(
//        val width: Int = 0,
//        val height: Int = 0,
//        val description: String? = null,
//        val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
//    )
//
//    /**
//     * Retrieves images from HTML img; tags.
//     */
//    interface ImageGetter {
//        /**
//         * This method is called when the HTML parser encounters an
//         * img; tag.  The `source` argument is the
//         * string from the "src" attribute;
//         */
//        fun getDrawable(source: String?, info: ImageInfo)
//    }
//
//    interface TextGetter {
//        fun setText(text: Spannable)
//    }
//
//    /**
//     * Is notified when HTML tags are encountered that the parser does
//     * not know how to interpret.
//     */
//    interface TagHandler {
//        /**
//         * This method will be called whenn the HTML parser encounters
//         * a tag that it does not know how to interpret.
//         */
//        fun handleTag(
//            opening: Boolean, tag: String?,
//            output: Editable?, xmlReader: XMLReader?
//        )
//    }
//
//    /**
//     * Lazy initialization holder for HTML parser. This class will
//     * a) be preloaded by the zygote, or b) not loaded until absolutely
//     * necessary.
//     */
//    private object HtmlParser {
//        val schema: HTMLSchema = HTMLSchema()
//    }
//}
//
//internal class HtmlToSpannedConverter(
//    private val mSource: String?, imageGetter: Html.ImageGetter?, textHandler: Html.TextGetter?,
//    tagHandler: Html.TagHandler?, parser: Parser, flags: Int
//) :
//    ContentHandler {
////    private val mReader: XMLReader
//    private var mSpannableStringBuilder: SpannableStringBuilder = SpannableStringBuilder()
//    private val mImageGetter: Html.ImageGetter? = imageGetter
//    private val mTagHandler: Html.TagHandler? = tagHandler
//    private val textHandler: Html.TextGetter? = textHandler
//    private val mFlags: Int
//
//    companion object {
//        private val HEADING_SIZES = floatArrayOf(
//            1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f
//        )
//    }
//
//    private var sTextAlignPattern: Pattern? = null
//    private var sForegroundColorPattern: Pattern? = null
//    private var sBackgroundColorPattern: Pattern? = null
//    private var sTextDecorationPattern: Pattern? = null
//
//    /**
//     * Name-value mapping of HTML/CSS colors which have different values in [Color].
//     */
//    private val sColorMap: MutableMap<String, Int> = HashMap()
//    private val textAlignPattern: Pattern?
//        get() {
//            if (sTextAlignPattern == null) {
//                sTextAlignPattern = Pattern.compile("(?:\\s+|\\A)text-align\\s*:\\s*(\\S*)\\b")
//            }
//            return sTextAlignPattern
//        }
//    private val foregroundColorPattern: Pattern?
//        get() {
//            if (sForegroundColorPattern == null) {
//                sForegroundColorPattern = Pattern.compile(
//                    "(?:\\s+|\\A)color\\s*:\\s*(\\S*)\\b"
//                )
//            }
//            return sForegroundColorPattern
//        }
//    private val backgroundColorPattern: Pattern?
//        get() {
//            if (sBackgroundColorPattern == null) {
//                sBackgroundColorPattern = Pattern.compile(
//                    "(?:\\s+|\\A)background(?:-color)?\\s*:\\s*(\\S*)\\b"
//                )
//            }
//            return sBackgroundColorPattern
//        }
//    private val textDecorationPattern: Pattern?
//        get() {
//            if (sTextDecorationPattern == null) {
//                sTextDecorationPattern = Pattern.compile(
//                    "(?:\\s+|\\A)text-decoration\\s*:\\s*(\\S*)\\b"
//                )
//            }
//            return sTextDecorationPattern
//        }
//
//    private fun appendNewlines(text: Editable, minNewline: Int) {
//        val len = text.length
//        if (len == 0) {
//            return
//        }
//        var existingNewlines = 0
//        var i = len - 1
//        while (i >= 0 && text[i] == '\n') {
//            existingNewlines++
//            i--
//        }
//        for (j in existingNewlines until minNewline) {
////            text.append("\n")
//        }
//    }
//
//    private fun startBlockElement(text: Editable, attributes: Attributes, margin: Int) {
//        val len = text.length
//        if (margin > 0) {
//            appendNewlines(text, margin)
//            start(text, Newline(margin))
//        }
//        val style: String? = attributes.getValue("", "style")
//        if (style != null) {
//            val m: Matcher? = textAlignPattern?.matcher(style)
//            if (m?.find() == true) {
//                val alignment: String? = m.group(1)
//                if (alignment.equals("start", ignoreCase = true)) {
//                    start(text, Alignment(Layout.Alignment.ALIGN_NORMAL))
//                } else if (alignment.equals("center", ignoreCase = true)) {
//                    start(text, Alignment(Layout.Alignment.ALIGN_CENTER))
//                } else if (alignment.equals("end", ignoreCase = true)) {
//                    start(text, Alignment(Layout.Alignment.ALIGN_OPPOSITE))
//                }
//            }
//        }
//    }
//
//    private fun endBlockElement(text: Editable) {
//        getLast(text, Newline::class.java)?.let { newline ->
//            appendNewlines(text, newline.mNumNewlines)
//            text.removeSpan(newline)
//        }
//
//        getLast(text, Alignment::class.java)?.let { alignment->
//            setSpanFromMark(text, alignment, AlignmentSpan.Standard(alignment.mAlignment))
//        }
//    }
//
//    private fun handleBr(text: Editable) {
//        text.append('\n')
//    }
//
//    private fun endLi(text: Editable) {
//        endCssStyle(text)
//        endBlockElement(text)
//        end(text, Bullet::class.java, BulletSpan())
//    }
//
//    private fun endBlockquote(text: Editable) {
//        endBlockElement(text)
//        end(
//            text,
//            Blockquote::class.java, QuoteSpan()
//        )
//    }
//
//    private fun endHeading(text: Editable) {
//        // RelativeSizeSpan and StyleSpan are CharacterStyles
//        // Their ranges should not include the newlines at the end
//        getLast(text, Heading::class.java)?.let { heading ->
//            setSpanFromMark(
//                text, heading,
//                RelativeSizeSpan(HEADING_SIZES[heading.mLevel]), StyleSpan(Typeface.BOLD)
//            )
//        }
//        endBlockElement(text)
//    }
//
//    private fun <T> getLast(text: Spanned, kind: Class<T>): T? {
//        /*
//     * This knows that the last returned object from getSpans()
//     * will be the most recently added.
//     */
//        val objs = text.getSpans(0, text.length, kind)
//        return if (objs.isEmpty()) {
//            null
//        } else {
//            objs[objs.size - 1]
//        }
//    }
//
//    private fun setSpanFromMark(text: Spannable, mark: Any, vararg spans: Any) {
//        val where = text.getSpanStart(mark)
//        text.removeSpan(mark)
//        val len = text.length
//        if (where != len) {
//            for (span in spans) {
//                text.setSpan(span, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
//        }
//    }
//
//    private fun start(text: Editable, mark: Any) {
//        val len = text.length
//        text.setSpan(mark, len, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
//    }
//
//    private fun <T> end(text: Editable, kind: Class<T>, repl: Any) {
//        val len = text.length
//        val obj = getLast<T>(text, kind)
//        if (obj != null) {
//            setSpanFromMark(text, obj, repl)
//        }
//    }
//
//    private fun endCssStyle(text: Editable) {
//        getLast(text, Strikethrough::class.java)?.let { strikethrough ->
//            setSpanFromMark(text, strikethrough, StrikethroughSpan())
//        }
//
//        getLast(text, Background::class.java)?.let { background ->
//            setSpanFromMark(text, background, BackgroundColorSpan(background.mBackgroundColor))
//        }
//
//        getLast(text, Foreground::class.java)?.let { foreground ->
//            setSpanFromMark(text, foreground, ForegroundColorSpan(foreground.mForegroundColor))
//        }
//    }
//
//    private fun startImg(text: Editable, attributes: Attributes, img: Html.ImageGetter?) {
//        val src: String = attributes.getValue("", "src")
//        val width: Int = attributes.getValue("", "width").toInt()
//        val height = attributes.getValue("", "height").toInt()
//        val description = attributes.getValue("", "alt")
//        val info = Html.ImageInfo(width, height, description)
//        img?.getDrawable(src, info)
//    }
//
//    private fun endFont(text: Editable) {
//        getLast(text, Font::class.java)?.let { font ->
//            setSpanFromMark(text, font, TypefaceSpan(font.mFace))
//        }
//        getLast(text, Foreground::class.java)?.let { foreground ->
//            setSpanFromMark(text, foreground, ForegroundColorSpan(foreground.mForegroundColor))
//        }
//    }
//
//    private fun startA(text: Editable, attributes: Attributes) {
//        val href: String = attributes.getValue("", "href")
//        start(text, Href(href))
//    }
//
//    private fun endA(text: Editable) {
//        val h = getLast(
//            text,
//            Href::class.java
//        )
//        if (h != null) {
//            if (h.mHref != null) {
//                setSpanFromMark(text, h, URLSpan(h.mHref))
//            }
//        }
//    }
//
//    init {
//        sColorMap["darkgray"] = -0x565657
//        sColorMap["gray"] = -0x7f7f80
//        sColorMap["lightgray"] = -0x2c2c2d
//        sColorMap["darkgrey"] = -0x565657
//        sColorMap["grey"] = -0x7f7f80
//        sColorMap["lightgrey"] = -0x2c2c2d
//        sColorMap["green"] = -0xff8000
//    }
//
//
//    fun convert(): Spanned {
//        mReader.contentHandler = this
//        try {
//            mReader.parse(InputSource(StringReader(mSource)))
//        } catch (e: IOException) {
//            // We are reading from a string. There should not be IO problems.
//            throw RuntimeException(e)
//        } catch (e: SAXException) {
//            // TagSoup doesn't throw parse exceptions.
//            throw RuntimeException(e)
//        }
//        // Fix flags and range for paragraph-type markup.
//        val obj = mSpannableStringBuilder.getSpans(0, mSpannableStringBuilder.length, ParagraphStyle::class.java)
//        for (i in obj.indices) {
//            val start = mSpannableStringBuilder.getSpanStart(obj[i])
//            var end = mSpannableStringBuilder.getSpanEnd(obj[i])
//            // If the last line of the range is blank, back off by one.
//            if (end - 2 >= 0) {
//                if (mSpannableStringBuilder[end - 1] == '\n' &&
//                    mSpannableStringBuilder[end - 2] == '\n'
//                ) {
//                    end--
//                }
//            }
//            if (end == start) {
//                mSpannableStringBuilder.removeSpan(obj[i])
//            } else {
//                mSpannableStringBuilder.setSpan(obj[i], start, end, Spannable.SPAN_PARAGRAPH)
//            }
//        }
//        return mSpannableStringBuilder
//    }
//
//    private fun handleStartTag(tag: String, attributes: Attributes) {
//        if (tag.equals("br", ignoreCase = true)) {
//            // We don't need to handle this. TagSoup will ensure that there's a </br> for each <br>
//            // so we can safely emit the linebreaks when we handle the close tag.
//        } else if (tag.equals("p", ignoreCase = true)) {
//            startBlockElement(
//                mSpannableStringBuilder, attributes,
//                marginParagraph
//            )
//            startCssStyle(mSpannableStringBuilder, attributes)
//        } else if (tag.equals("ul", ignoreCase = true)) {
//            startBlockElement(
//                mSpannableStringBuilder, attributes,
//                marginList
//            )
//        } else if (tag.equals("li", ignoreCase = true)) {
//            startLi(mSpannableStringBuilder, attributes)
//        } else if (tag.equals("div", ignoreCase = true)) {
//            startBlockElement(
//                mSpannableStringBuilder, attributes,
//                marginDiv
//            )
//        } else if (tag.equals("span", ignoreCase = true)) {
//            startCssStyle(mSpannableStringBuilder, attributes)
//        } else if (tag.equals("strong", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Bold())
//        } else if (tag.equals("b", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Bold())
//        } else if (tag.equals("em", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Italic())
//        } else if (tag.equals("cite", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Italic())
//        } else if (tag.equals("dfn", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Italic())
//        } else if (tag.equals("i", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Italic())
//        } else if (tag.equals("big", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Big())
//        } else if (tag.equals("small", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Small())
//        } else if (tag.equals("font", ignoreCase = true)) {
//            startFont(mSpannableStringBuilder, attributes)
//        } else if (tag.equals("blockquote", ignoreCase = true)) {
//            startBlockquote(mSpannableStringBuilder, attributes)
//        } else if (tag.equals("tt", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Monospace())
//        } else if (tag.equals("a", ignoreCase = true)) {
//            startA(mSpannableStringBuilder, attributes)
//        } else if (tag.equals("u", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Underline())
//        } else if (tag.equals("del", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Strikethrough())
//        } else if (tag.equals("s", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Strikethrough())
//        } else if (tag.equals("strike", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Strikethrough())
//        } else if (tag.equals("sup", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Super())
//        } else if (tag.equals("sub", ignoreCase = true)) {
//            start(mSpannableStringBuilder, Sub())
//        } else if (tag.length == 2 && Character.toLowerCase(tag[0]) == 'h' && tag[1] >= '1' && tag[1] <= '6') {
//            startHeading(mSpannableStringBuilder, attributes, tag[1] - '1')
//        } else if (tag.equals("img", ignoreCase = true)) {
//            startImg(mSpannableStringBuilder, attributes, mImageGetter)
//        } else mTagHandler?.handleTag(true, tag, mSpannableStringBuilder, mReader)
//    }
//
//    private fun handleEndTag(tag: String) {
//        if (tag.equals("br", ignoreCase = true)) {
//            handleBr(mSpannableStringBuilder)
//        } else if (tag.equals("p", ignoreCase = true)) {
//            endCssStyle(mSpannableStringBuilder)
//            endBlockElement(mSpannableStringBuilder)
//        } else if (tag.equals("ul", ignoreCase = true)) {
//            endBlockElement(mSpannableStringBuilder)
//        } else if (tag.equals("li", ignoreCase = true)) {
//            endLi(mSpannableStringBuilder)
//        } else if (tag.equals("div", ignoreCase = true)) {
//            endBlockElement(mSpannableStringBuilder)
//        } else if (tag.equals("span", ignoreCase = true)) {
//            endCssStyle(mSpannableStringBuilder)
//        } else if (tag.equals("strong", ignoreCase = true)) {
//            end(
//                mSpannableStringBuilder,
//                Bold::class.java, StyleSpan(Typeface.BOLD)
//            )
//        } else if (tag.equals("b", ignoreCase = true)) {
//            end(
//                mSpannableStringBuilder,
//                Bold::class.java, StyleSpan(Typeface.BOLD)
//            )
//        } else if (tag.equals("em", ignoreCase = true)) {
//            end(
//                mSpannableStringBuilder,
//                Italic::class.java, StyleSpan(Typeface.ITALIC)
//            )
//        } else if (tag.equals("cite", ignoreCase = true)) {
//            end(
//                mSpannableStringBuilder,
//                Italic::class.java, StyleSpan(Typeface.ITALIC)
//            )
//        } else if (tag.equals("dfn", ignoreCase = true)) {
//            end(
//                mSpannableStringBuilder,
//                Italic::class.java, StyleSpan(Typeface.ITALIC)
//            )
//        } else if (tag.equals("i", ignoreCase = true)) {
//            end(
//                mSpannableStringBuilder,
//                Italic::class.java, StyleSpan(Typeface.ITALIC)
//            )
//        } else if (tag.equals("big", ignoreCase = true)) {
//            end(
//                mSpannableStringBuilder,
//                Big::class.java, RelativeSizeSpan(1.25f)
//            )
//        } else if (tag.equals("small", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Small::class.java, RelativeSizeSpan(0.8f))
//        } else if (tag.equals("font", ignoreCase = true)) {
//            endFont(mSpannableStringBuilder)
//        } else if (tag.equals("blockquote", ignoreCase = true)) {
//            endBlockquote(mSpannableStringBuilder)
//        } else if (tag.equals("tt", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Monospace::class.java, TypefaceSpan("monospace"))
//        } else if (tag.equals("a", ignoreCase = true)) {
//            endA(mSpannableStringBuilder)
//        } else if (tag.equals("u", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Underline::class.java, UnderlineSpan())
//        } else if (tag.equals("del", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Strikethrough::class.java, StrikethroughSpan())
//        } else if (tag.equals("s", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Strikethrough::class.java, StrikethroughSpan())
//        } else if (tag.equals("strike", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Strikethrough::class.java, StrikethroughSpan())
//        } else if (tag.equals("sup", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Super::class.java, SuperscriptSpan())
//        } else if (tag.equals("sub", ignoreCase = true)) {
//            end(mSpannableStringBuilder, Sub::class.java, SubscriptSpan())
//        } else if (tag.length == 2 && Character.toLowerCase(tag[0]) == 'h' && tag[1] >= '1' && tag[1] <= '6') {
//            endHeading(mSpannableStringBuilder)
//            textHandler?.setText(mSpannableStringBuilder.toSpannable())
//            mSpannableStringBuilder = SpannableStringBuilder()
//        } else mTagHandler?.handleTag(false, tag, mSpannableStringBuilder, mReader)
//    }
//
//    private val marginParagraph: Int
//        get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
//    private val marginHeading: Int
//        get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING)
//    private val marginListItem: Int
//        get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM)
//    private val marginList: Int
//        get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST)
//    private val marginDiv: Int
//        get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)
//    private val marginBlockquote: Int
//        get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
//
//    /**
//     * Returns the minimum number of newline characters needed before and after a given block-level
//     * element.
//     *
//     * @param flag the corresponding option flag defined in [Html] of a block-level element
//     */
//    private fun getMargin(flag: Int): Int {
//        return if (flag and mFlags != 0) {
//            1
//        } else 2
//    }
//
//    private fun startLi(text: Editable, attributes: Attributes) {
//        startBlockElement(text, attributes, marginListItem)
//        start(text, Bullet())
//        startCssStyle(text, attributes)
//    }
//
//    private fun startBlockquote(text: Editable, attributes: Attributes) {
//        startBlockElement(text, attributes, marginBlockquote)
//        start(text, Blockquote())
//    }
//
//    private fun startHeading(text: Editable, attributes: Attributes, level: Int) {
//        startBlockElement(text, attributes, marginHeading)
//        start(text, Heading(level))
//    }
//
//    private fun startCssStyle(text: Editable, attributes: Attributes) {
//        val style: String? = attributes.getValue("", "style")
//        Log.d("3615", "STYLE FOUND")
//        if (style != null) {
//            var m: Matcher? = foregroundColorPattern?.matcher(style)
//            if (m?.find() == true) {
//                val c = getHtmlColor(m.group(1))
//                if (c != -1) {
//                    start(text, Foreground(c or -0x1000000))
//                }
//            }
//            m = backgroundColorPattern?.matcher(style)
//            if (m?.find() == true) {
//                val c = getHtmlColor(m.group(1))
//                if (c != -1) {
//                    start(text, Background(c or -0x1000000))
//                }
//            }
//            m = textDecorationPattern?.matcher(style)
//            if (m?.find() == true) {
//                val textDecoration: String? = m.group(1)
//                if (textDecoration.equals("line-through", ignoreCase = true)) {
//                    start(text, Strikethrough())
//                }
//            }
//        }
//    }
//
//    private fun startFont(text: Editable, attributes: Attributes) {
//        val color: String = attributes.getValue("", "color")
//        val face: String = attributes.getValue("", "face")
//        if (!TextUtils.isEmpty(color)) {
//            val c = getHtmlColor(color)
//            if (c != -1) {
//                start(text, Foreground(c or -0x1000000))
//            }
//        }
//        if (!TextUtils.isEmpty(face)) {
//            start(text, Font(face))
//        }
//    }
//
//    private fun getHtmlColor(color: String?): Int {
//        if (mFlags and Html.FROM_HTML_OPTION_USE_CSS_COLORS
//            == Html.FROM_HTML_OPTION_USE_CSS_COLORS
//        ) {
//            val i = sColorMap[color?.toLowerCase(Locale.US)]
//            if (i != null) {
//                return i
//            }
//        }
//        return 0// todo Color.getHtmlColor(color)
//    }
//
//    override fun setDocumentLocator(locator: Locator?) {}
//
//    @Throws(SAXException::class)
//    override fun startDocument() {
//    }
//
//    @Throws(SAXException::class)
//    override fun endDocument() {
//    }
//
//    @Throws(SAXException::class)
//    override fun startPrefixMapping(prefix: String?, uri: String?) {
//    }
//
//    @Throws(SAXException::class)
//    override fun endPrefixMapping(prefix: String?) {
//    }
//
//    @Throws(SAXException::class)
//    override fun startElement(
//        uri: String?,
//        localName: String,
//        qName: String?,
//        attributes: Attributes
//    ) {
//        handleStartTag(localName, attributes)
//    }
//
//    @Throws(SAXException::class)
//    override fun endElement(uri: String?, localName: String, qName: String?) {
//        handleEndTag(localName)
//    }
//
//    @Throws(SAXException::class)
//    override fun characters(ch: CharArray, start: Int, length: Int) {
//        val sb = StringBuilder()
//        /*
//         * Ignore whitespace that immediately follows other whitespace;
//         * newlines count as spaces.
//         */for (i in 0 until length) {
//            val c = ch[i + start]
//            if (c == ' ' || c == '\n') {
//                var pred: Char
//                var len = sb.length
//                if (len == 0) {
//                    len = mSpannableStringBuilder.length
//                    pred = if (len == 0) {
//                        '\n'
//                    } else {
//                        mSpannableStringBuilder[len - 1]
//                    }
//                } else {
//                    pred = sb[len - 1]
//                }
//                if (pred != ' ' && pred != '\n') {
//                    sb.append(' ')
//                }
//            } else {
//                sb.append(c)
//            }
//        }
//        mSpannableStringBuilder.append(sb)
//    }
//
//    @Throws(SAXException::class)
//    override fun ignorableWhitespace(ch: CharArray?, start: Int, length: Int) {
//    }
//
//    @Throws(SAXException::class)
//    override fun processingInstruction(target: String?, data: String?) {
//    }
//
//    @Throws(SAXException::class)
//    override fun skippedEntity(name: String?) {
//    }
//
//    private class Bold
//    private class Italic
//    private class Underline
//    private class Strikethrough
//    private class Big
//    private class Small
//    private class Monospace
//    private class Blockquote
//    private class Super
//    private class Sub
//    private class Bullet
//    private class Font(var mFace: String)
//    private class Href(var mHref: String?)
//    private class Foreground(val mForegroundColor: Int)
//    private class Background(val mBackgroundColor: Int)
//    private class Heading(val mLevel: Int)
//    private class Newline(val mNumNewlines: Int)
//    private class Alignment(alignment: Layout.Alignment) {
//        val mAlignment: Layout.Alignment = alignment
//    }
//
//    init {
////        mReader = parser
//        mFlags = flags
//    }
//}
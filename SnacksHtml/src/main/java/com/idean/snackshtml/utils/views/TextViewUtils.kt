package com.idean.snackshtml.utils.views

import android.content.Context
import android.util.DisplayMetrics
import android.text.Spannable
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import com.idean.snackshtml.SnacksHtml
import com.idean.snackshtml.utils.html.CSSStyle
import com.idean.snackshtml.utils.html.LinkStyle

/**
 * Created by Mickael Calatraba on 4/9/21.
 * Copyright (c) 2021 Idean. All rights reserved.
 */

fun TextView.setPadding(info: CSSStyle) {
    this.setPadding(
        info.paddingStart.pixelsToDp(context),
        info.paddingTop.pixelsToDp(context),
        info.paddingEnd.pixelsToDp(context),
        info.paddingBottom.pixelsToDp(context)
    )
}

fun TextView.setMargin(info: CSSStyle) {
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    params.topMargin = info.marginTop.pixelsToDp(context)
    params.bottomMargin = info.marginBottom.pixelsToDp(context)
    params.marginStart = info.marginStart.pixelsToDp(context)
    params.marginEnd = info.marginEnd.pixelsToDp(context)
    this.layoutParams = params
}

fun TextView.setStyle(spanText: Spannable, info: CSSStyle) {
    info.spanStyle?.let { span ->
        this.text = spanText.apply { setSpan(span, 0, text.length, 0) }
    }
    this.typeface = info.fontFace
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, info.textSize)
    this.setTextColor(info.textColor)
    this.setLineSpacing(getLineMult(info.textSize, info.lineHeight), 1f)
}

fun TextView.setLink(linkStyle: LinkStyle?, callback: SnacksHtml.LinkCallback?) {
    linkStyle?.let {
        setLinkTextColor(linkStyle.linkColor)
        this.setOnClickListener {
            setLinkTextColor(linkStyle.visitedLinkColor)
            callback?.onLinkClicked(linkStyle.href)
        }
    }
}

private fun getLineMult(textSize: Float, lineHeight: Float): Float {
    return lineHeight - textSize
}

fun Int.pixelsToDp(context: Context): Int {
    return this / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}
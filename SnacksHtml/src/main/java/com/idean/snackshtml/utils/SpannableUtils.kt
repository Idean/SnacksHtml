package com.idean.snackshtml.utils

import android.text.Spannable
import android.text.Spanned

/**
 * Created by Mickael Calatraba on 3/24/21.
 * Copyright (c) 2021 Idean. All rights reserved.
 */

fun Spannable.setSpan(text: String, spanType: Any) {
    val start = this.indexOf(text, ignoreCase = true)
    val end = start + text.length
    this.setSpan(spanType, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}
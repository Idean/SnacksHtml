package com.idean.snackshtml.utils.css.colors

import android.graphics.Color
import com.idean.snackshtml.errors.MalformedColorCSSException

/**
 * Created by Mickael Calatraba on 4/7/21.
 * Copyright (c) 2021 Idean. All rights reserved.
 */
class ColorParser {
    private val colorNames = ColorNames()

    @Throws(MalformedColorCSSException::class)
    fun parseColor(value: String?): Int {
        return if (this.colorNames.colors.containsKey(value)) {
            Color.parseColor(this.colorNames.colors[value])
        } else {
            try {
                Color.parseColor(value)
            } catch (e: Exception) {
                throw MalformedColorCSSException(value)
            }
        }
    }
}
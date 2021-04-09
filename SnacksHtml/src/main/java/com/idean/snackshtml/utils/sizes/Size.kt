package com.idean.snackshtml.utils.sizes

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface

/**
 * Created by Mickael Calatraba on 4/9/21.
 * Copyright (c) 2021 Idean. All rights reserved.
 */
class Size(
    private val size: Float,
    private val unit: SizeUnit = SizeUnit.PX
) {
    companion object {
        // Inches is equal to 96px
        private const val INCHES_PX = 96
        // Inches is equal to 72pt
        private const val INCHES_PT = 72
    }
    constructor(entry: Int, unit: SizeUnit = SizeUnit.PX): this(entry.toFloat(), unit)
    constructor(entry: Float, unit: String): this(entry, SizeUnit.valueOf(unit))
    constructor(entry: Int, unit: String): this(entry.toFloat(), SizeUnit.valueOf(unit))

    fun sizeToPx(typeFace: Typeface): Int {
        val fontSize = getFontSize(typeFace)

        return when(this.unit) {
            SizeUnit.EM -> (fontSize * this.size).toInt()
            SizeUnit.PT -> (this.size * (INCHES_PX / INCHES_PT)).toInt()
            else -> this.size.toInt()
        }
    }

    fun sizeToDp(context: Context): Int {
        return 1
    }

    fun getFontSize(typeFace: Typeface): Float {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = 5f
        paint.strokeCap = Paint.Cap.ROUND
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)

        return  paint.measureText("m", 0, 1)
    }
}

enum class SizeUnit(val unit: String) {
    PX("px"),
    EM("em"),
    PT("pt");

    companion object {
        fun valueOf(unit: String?): SizeUnit? = values().find { it.unit == unit }
    }
}
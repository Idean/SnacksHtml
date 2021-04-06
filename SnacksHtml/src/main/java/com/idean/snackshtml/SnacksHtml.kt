package com.idean.snackshtml

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.idean.snackshtml.utils.CSSData
import com.idean.snackshtml.utils.HtmlJSoup
import com.idean.snackshtml.utils.ImageInfo


/**
 * Created by Mickael Calatraba on 3/18/21.
 */

class SnacksHtml @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), HtmlJSoup.ImageGetter, HtmlJSoup.TextGetter {

    companion object {
        private const val TAG = "SnacksHtml"
    }

    var imageGetter: ImageGetter? = null

    interface ImageGetter {
        fun getImageFromUlr(url: String?, imageView: ImageView, onImageLoaded: (() -> Unit))
    }

    init {
        orientation = VERTICAL
        setPadding(0, 0, 0, 0)
    }

    fun parseHtml(data: String?) {
        try {
            HtmlJSoup(context).fromHtml(data, this, this)
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        }
    }


    override fun getDrawable(source: String?, info: ImageInfo) {
        val imageView = ImageView(context)
        addView(imageView)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = LayoutParams(1080.pixelsToDp(), 1920.pixelsToDp())
        imageView.setBackgroundColor(Color.RED)
        this.imageGetter?.getImageFromUlr(source, imageView) {
            val width = getFinalSize(info.width)
            val height = getFinalSize(info.height)
            val params = LayoutParams(width, height)
            imageView.layoutParams = params
        }
    }

    fun getFinalSize(size: Int): Int {
        return when (size) {
            -1 -> LayoutParams.MATCH_PARENT
            -2 -> LayoutParams.WRAP_CONTENT
            else -> size.pixelsToDp()
        }
    }
    
    override fun setText(text: Spannable, info: CSSData?) {
        val textView = TextView(context)
        textView.text = text
        info?.let { textInfo ->
            info.spanStyle?.let { span ->
                textView.text = text.apply { setSpan(span, 0, text.length, 0) }
            }
            textView.typeface = info.fontFace
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textInfo.textSize)
            textView.setTextColor(textInfo.textColor)
            textView.setLineSpacing(getLineMult(textInfo.textSize, textInfo.lineHeight), 1f)

            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.topMargin = textInfo.marginTop.pixelsToDp()
            params.bottomMargin = textInfo.marginBottom.pixelsToDp()
            params.marginStart = textInfo.marginStart.pixelsToDp()
            params.marginEnd = textInfo.marginEnd.pixelsToDp()
            textView.layoutParams = params

            textView.setPadding(
                info.paddingStart.pixelsToDp(),
                info.paddingTop.pixelsToDp(),
                info.paddingEnd.pixelsToDp(),
                info.paddingBottom.pixelsToDp()
            )
        }
        addView(textView)
        invalidate()
    }

    private fun getLineMult(textSize: Float, lineHeight: Float): Float {
        return lineHeight - textSize
    }

    private fun Int.pixelsToDp(): Int {
        return this / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }
}
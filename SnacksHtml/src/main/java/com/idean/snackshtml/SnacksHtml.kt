package com.idean.snackshtml

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.idean.snackshtml.utils.html.CSSStyle
import com.idean.snackshtml.utils.html.HtmlJSoup
import com.idean.snackshtml.utils.html.ImageStyle
import com.idean.snackshtml.utils.views.*


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
    var linkCallback: LinkCallback? = null

    interface ImageGetter {
        fun getImageFromUlr(url: String?, imageView: ImageView, onImageLoaded: (() -> Unit))
    }

    interface LinkCallback {
        fun onLinkClicked(url: String?)
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


    override fun getDrawable(source: String?, info: ImageStyle) {
        val imageView = ImageView(context)
        addView(imageView)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = LayoutParams(1080.pixelsToDp(context), 1920.pixelsToDp(context))
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
            else -> size.pixelsToDp(context)
        }
    }
    
    override fun setText(spanText: Spannable, info: CSSStyle?) {
        val textView = AppCompatTextView(context)

        textView.text = spanText
        info?.let { textInfo ->
            textView.apply {
                setStyle(spanText, textInfo)
                setMargin(textInfo)
                setPadding(textInfo)
                setLink(textInfo.linkStyle, linkCallback)
            }
        }

        addView(textView)
        invalidate()
    }
}
package com.idean.snackshtml.utils.css

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import com.idean.snackshtml.errors.MalformedCSSException
import com.idean.snackshtml.errors.MissingCSSMandatoryFieldException
import com.idean.snackshtml.errors.MissingTypeFaceResourceException
import com.idean.snackshtml.errors.UnsupportedFieldException
import java.util.regex.Pattern


/**
 * Created by Mickael Calatraba on 3/24/21.
 */

class ParserCSSFontFace: AbstractCSSParser() {
    companion object {
        private const val TAG = "ParserCSSFontFace"
    }

    val fontFaces = mutableMapOf<String, Typeface>()

    /**
     * This function parse a CSS string to find all the @font-face and retrieve the font-family and
     * the src. the result will be stored in fontFaces map with the property font-family as key and
     * the Typeface loaded in the value.
     *
     * @param context: The app context is used to load the typeface
     * @param css: This the complete css file formatted to string.
     *
     * @return: return the CSS param with the all font-face removed
     */
    fun findFonts(context: Context, css: String): String {
        var result = css
        val m = Pattern.compile("@font-face(\\s?)+(\\{(?:\\{?[^\\[]*?\\}))").matcher(css)
        while (m.find()) {
            try {
                val section = m.group(0)
                var name = ""
                var path = ""
                getAttributes(section).forEach { attribute ->
                    when (val key = getKey(attribute)) {
                        "font-family" -> name = getFontFamilyName(getValue(attribute))
                        "src" -> path = getFontFamilyPath(getValue(attribute))
                        else -> { // Not supported
                            throw UnsupportedFieldException(key)
                        }
                    }
                }
                this.fontFaces[name] = getFontFaceFromFont(context, path)
                section?.let { result = result.replace(section, "") }
            } catch (e: UnsupportedFieldException) {
                Log.e(TAG, Log.getStackTraceString(e))
            } catch (e: MissingCSSMandatoryFieldException) {
                Log.e(TAG, Log.getStackTraceString(e))
            } catch (e: MissingTypeFaceResourceException) {
                Log.e(TAG, Log.getStackTraceString(e))
            } catch (e: MalformedCSSException) {
                Log.e(TAG, Log.getStackTraceString(e))
            }
        }
        return result
    }


    /**
     * Function used to get the property value url in the font-family property without quotes or double
     * quotes. In " url('file:///android_res/font/ubuntu_regular.ttf')" example, the function will
     * return "file:///android_res/font/ubuntu_regular.ttf" trimmed and without " and '.
     *
     * @param value: the font-face property value
     *
     * @throws MissingCSSMandatoryFieldException: If the value is not found throws
     * MissingCSSMandatoryFieldException.
     *
     * @return: return the property value trimmed and without quotes.
     */
    @Throws(MissingCSSMandatoryFieldException::class)
    fun getFontFamilyPath(value: String): String {
        try {
            val pattern = Pattern.compile("(\\'|\\\")(.*?)(\\'|\\\")").matcher(value)
            pattern.find()
            val path = pattern.group(0)?.trim()
            if (path.isNullOrEmpty()) {
                throw MissingCSSMandatoryFieldException(value)
            }
            return path.substring(1, path.lastIndex)
        } catch (e: Exception) {
            throw MissingCSSMandatoryFieldException(value)
        }
    }

    @Throws(MissingTypeFaceResourceException::class)
    private fun getFontFaceFromFont(context: Context, completePath: String): Typeface {
        try {
            val assetPos = completePath.indexOf("android_res/") + "android_res/".length
            return Typeface.createFromAsset(context.assets, completePath.substring(assetPos))
        } catch (e: Exception) {
            throw MissingTypeFaceResourceException(completePath)
        }
    }
}
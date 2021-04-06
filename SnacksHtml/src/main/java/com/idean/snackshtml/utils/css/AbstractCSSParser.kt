package com.idean.snackshtml.utils.css

import com.idean.snackshtml.errors.MalformedCSSException
import com.idean.snackshtml.errors.MissingCSSMandatoryFieldException
import java.util.regex.Pattern
import kotlin.jvm.Throws


/**
 * Created by Mickael Calatraba on 3/24/21.
 */

abstract class AbstractCSSParser() {

    /**
     * This function parse and return the all content in bracket. The result is
     * without de brackets and all lines are trimmed. The empty properties are removed.
     *
     * @param section: The css class
     *
     * @return: Return an array of String, this array is each line of properties (split by ;)
     */
    @Throws(MalformedCSSException::class)
    fun getAttributes(section: String?): List<String> {
        section?.trim()?.ifEmpty { null }?.let {
            try {
                val start = section.indexOf('{')
                val end = section.indexOf('}')
                val result = section.subSequence(start + 1, end)
                    .split(";")
                    .toMutableList()
                result.forEachIndexed { i, item ->
                    result[i] = item.trim()
                }
                result.removeAll { it.isEmpty() }
                return result
            } catch (e: StringIndexOutOfBoundsException) {
                throw MalformedCSSException(section)
            }
        }
        return emptyList()
    }


    /**
     * Function used to get the property name in the @font-face section.
     * in "font-family: 'ubuntu_light'" example, the function will return font-family trimmed.
     *
     * @param line: the line of the font-face section
     *
     * @return: return the property name trimmed
     */
    fun getKey(line: String): String {
        return line.split(":", limit = 2)[0].trim()
    }

    /**
     * Function used to get the property value in the @font-face section.
     * in "font-family: 'ubuntu_light'" example, the function will return "'ubuntu_light'" trimmed.
     *
     * @param line: the line of the font-face section
     *
     * @throws MissingCSSMandatoryFieldException: If the value is not found throws
     * MissingCSSMandatoryFieldException.
     * @return: return the property value trimmed
     */
    @Throws(MissingCSSMandatoryFieldException::class)
    fun getValue(line: String): String {
        val result = line.split(":", limit = 2)
        if (result.size <= 1 || result[1].trim().isEmpty()) {
            throw MissingCSSMandatoryFieldException(line)
        }
        return result[1].trim()
    }

    /**
     * Function used to get the property value in the font-family property without quotes or double
     * quotes. In "font-family: 'ubuntu_light'" example, the function will return "ubuntu_light"
     * trimmed and without " and '.
     *
     * @param value: the font-face property value
     *
     * @throws MissingCSSMandatoryFieldException: If the value is not found throws
     * MissingCSSMandatoryFieldException.
     *
     * @return: return the property value trimmed and without quotes.
     */
    @Throws(MissingCSSMandatoryFieldException::class)
    fun getFontFamilyName(value: String): String {
        try {
            val pattern = Pattern.compile("[^\\s\\'\\\"]+").matcher(value)
            pattern.find()
            val name = pattern.group(0)?.trim()
            if (name.isNullOrEmpty()) {
                throw MissingCSSMandatoryFieldException(value)
            }
            return name
        } catch (e: Exception) {
            throw MissingCSSMandatoryFieldException(value)
        }
    }

}
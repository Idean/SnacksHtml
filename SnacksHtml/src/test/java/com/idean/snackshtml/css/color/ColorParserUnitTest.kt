package com.idean.snackshtml.css.color

import android.graphics.Color
import android.os.Build
import com.google.common.truth.Truth.assertThat
import com.idean.snackshtml.errors.MalformedColorCSSException
import com.idean.snackshtml.utils.css.colors.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class ColorParserUnitTest {

    @Test
    fun test_ParseColorsName() {
        // Given
        val parser = ColorParser()
        val names = ColorNames()

        // When
        names.colors.forEach { key, value ->
            val result = parser.parseColor(key)
            // Then
            assertThat(result).isEqualTo(Color.parseColor(value))
        }
    }

    @Test
    fun test_ParseColorsCode_OK() {
        // Given
        val parser = ColorParser()
        val code1 = "#FF00FF"
        val code2 = "#38FFF0FF"

        // When
        val result1 = parser.parseColor(code1)
        val result2 = parser.parseColor(code2)

        // Then
        assertThat(result1).isEqualTo(Color.parseColor(code1))
        assertThat(result2).isEqualTo(Color.parseColor(code2))
    }

    @Test
    fun test_ParseColorsCode_KO() {
        // Given
        val parser = ColorParser()
        val code = "#lacouleurFail"

        // When
        try {
            parser.parseColor(code)
            throw Exception()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MalformedColorCSSException::class.java)
        }

        try {
            parser.parseColor(null)
            throw Exception()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MalformedColorCSSException::class.java)
        }
    }
}
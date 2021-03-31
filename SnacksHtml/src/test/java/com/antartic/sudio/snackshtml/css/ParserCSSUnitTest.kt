package com.antartic.sudio.snackshtml.css

import androidx.test.core.app.ApplicationProvider
import com.antartic.sudio.errors.MalformedCSSException
import com.antartic.sudio.errors.MissingCSSMandatoryFieldException
import com.antartic.sudio.utils.css.ParserCSS
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ParserCSSUnitTest {
    private lateinit var parser: ParserCSS

    @Before
    fun setup() {
        this.parser = ParserCSS(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun test_getClassName() {
        // Given
        val test1 = "h1 {\n" +
                "    font-family: 'ubuntu_light';\n" +
                "    font-size: 30px;\n" +
                "    line-height: 32px;\n" +
                "}"
        val test2 = "   body   {\n" +
                "    font-family: 'ubuntu_light';\n" +
                "    font-size: 30px;\n" +
                "    line-height: 32px;\n" +
                "}"
        val test3 = "h1 { font-family: 'ubuntu_light'; font-size: 30px; line-height: 32px; }"
        val testFail = " h1 \n" +
                "    font-family: 'ubuntu_light';\n" +
                "    font-size: 30px;\n" +
                "    line-height: 32px;\n" +
                "}   "


        // When
        val result1 = this.parser.getClassName(test1)
        val result2 = this.parser.getClassName(test2)
        val result3 = this.parser.getClassName(test3)

        try {
            this.parser.getClassName(testFail)
            throw Exception("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MalformedCSSException::class.java)
            assertThat(e.message).isEqualTo("CSS malformed at: $testFail")
        }

        try {
            this.parser.getClassName(null)
            throw Exception("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MalformedCSSException::class.java)
            assertThat(e.message).isEqualTo("CSS malformed at: class cannot be null")
        }

        // Then
        assertThat(result1).isEqualTo("h1")
        assertThat(result2).isEqualTo("body")
        assertThat(result3).isEqualTo("h1")
    }

    @Test
    fun test_getAttributes() {
        // Given
        val test1 = "@font-face {\n" +
                "    src: url('file:///android_res/font/ubuntu_light.ttf');\n" +
                "    font-family: 'ubuntu_light';\n" +
                "}"
        val test2 = "@font-face {\n" +
                "    src: url('file:///android_res/font/ubuntu_light.ttf');\n" +
                "    font-family: 'ubuntu_light'" +
                "}"
        val test3 = "@font-face {    src: url('file:///android_res/font/ubuntu_light.ttf');   font-family: 'ubuntu_light';}"
        val testFail = "@font-face {\n" +
                "    src: url('file:///android_res/font/ubuntu_light.ttf');\n" +
                "    font-family: 'ubuntu_light';\n"
        val testFail2 = "@font-face {\n" +
                "    src:  ;\n" +
                "    font-family: 'ubuntu_light';\n"


        // When
        val result1 = this.parser.getAttributes(test1)
        val result2 = this.parser.getAttributes(test2)
        val result3 = this.parser.getAttributes(test3)
        val result4 = this.parser.getAttributes(null)
        val result5 = this.parser.getAttributes("")

        // Then
        assertThat(result1).isEqualTo(listOf("src: url('file:///android_res/font/ubuntu_light.ttf')", "font-family: 'ubuntu_light'"))
        assertThat(result2).isEqualTo(listOf("src: url('file:///android_res/font/ubuntu_light.ttf')", "font-family: 'ubuntu_light'"))
        assertThat(result3).isEqualTo(listOf("src: url('file:///android_res/font/ubuntu_light.ttf')", "font-family: 'ubuntu_light'"))
        assertThat(result4).isEqualTo(emptyList<String>())
        assertThat(result5).isEqualTo(emptyList<String>())
        try {
            this.parser.getAttributes(testFail)
            throw Exception("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MalformedCSSException::class.java)
            assertThat(e.message).isEqualTo("CSS malformed at: $testFail")
        }
        try {
            this.parser.getAttributes(testFail2)
            throw Exception("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MalformedCSSException::class.java)
            assertThat(e.message).isEqualTo("CSS malformed at: $testFail2")
        }
    }
}
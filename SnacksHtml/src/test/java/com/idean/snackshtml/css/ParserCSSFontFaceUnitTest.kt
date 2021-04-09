package com.idean.snackshtml.css

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.idean.snackshtml.errors.MalformedCSSException
import com.idean.snackshtml.errors.MissingCSSMandatoryFieldException
import com.idean.snackshtml.errors.MissingTypeFaceResourceException
import com.idean.snackshtml.utils.css.ParserCSSFontFace
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class ParserCSSFontFaceUnitTest {
    private lateinit var parser: ParserCSSFontFace

    @Before
    fun setup() {
        this.parser = ParserCSSFontFace()
    }

    @Test
    fun test_getKey() {
        // Given
        val test1 = "font-family: 'ubuntu_light'"
        val test2 = "src: url('file:///android_res/font/ubuntu_regular.ttf')"
        val test3 = "     font-family:   'ubuntu_light'"
        val test4 = "           src:    url('file:///android_res/font/ubuntu_regular.ttf')"

        // When
        val result1 = this.parser.getKey(test1)
        val result2 = this.parser.getKey(test2)
        val result3 = this.parser.getKey(test3)
        val result4 = this.parser.getKey(test4)

        // Then
        assertThat(result1).isEqualTo("font-family")
        assertThat(result2).isEqualTo("src")
        assertThat(result3).isEqualTo("font-family")
        assertThat(result4).isEqualTo("src")
    }

    @Test
    fun test_getValue() {
        // Given
        val test1 = "font-family: 'ubuntu_light'"
        val test2 = "src: url('file:///android_res/font/ubuntu_regular.ttf')"
        val test3 = "     font-family:   'ubuntu_light'"
        val test4 = "           src:    url('file:///android_res/font/ubuntu_regular.ttf')"
        val test5 = "     font-family:  "


        // When
        val result1 = this.parser.getValue(test1)
        val result2 = this.parser.getValue(test2)
        val result3 = this.parser.getValue(test3)
        val result4 = this.parser.getValue(test4)

        try {
            this.parser.getValue(test5)
            throw Exception("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MissingCSSMandatoryFieldException::class.java)
            assertThat(e.message).isEqualTo("Missing CSS mandatory value at: $test5 : ???")
        }

        // Then
        assertThat(result1).isEqualTo("'ubuntu_light'")
        assertThat(result2).isEqualTo("url('file:///android_res/font/ubuntu_regular.ttf')")
        assertThat(result3).isEqualTo("'ubuntu_light'")
        assertThat(result4).isEqualTo("url('file:///android_res/font/ubuntu_regular.ttf')")
    }

    @Test
    fun test_getFontFamilyName() {
        // Given
        val test1 = "\"ubuntu_light\""
        val test2 = "'ubuntu_light'"
        val test3 = "    'ubuntu_light '  "
        val test4 = "   \"  ubuntu_light   \""
        val testFail = "    "


        // When
        val result1 = this.parser.getFontFamilyName(test1)
        val result2 = this.parser.getFontFamilyName(test2)
        val result3 = this.parser.getFontFamilyName(test3)
        val result4 = this.parser.getFontFamilyName(test4)

        try {
            this.parser.getFontFamilyName(testFail)
            throw Exception("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MissingCSSMandatoryFieldException::class.java)
            assertThat(e.message).isEqualTo("Missing CSS mandatory value at: $testFail : ???")
        }

        // Then
        assertThat(result1).isEqualTo("ubuntu_light")
        assertThat(result2).isEqualTo("ubuntu_light")
        assertThat(result3).isEqualTo("ubuntu_light")
        assertThat(result4).isEqualTo("ubuntu_light")
    }

    @Test
    fun test_getFontFamilyPath() {
        // Given
        val test1 = "url(\"file:///android_res/font/ubuntu_regular.ttf\")"
        val test2 = "url(\"file:///android_res/font/ubuntu_regular.ttf\")"
        val test3 = "     url('file:///android_res/font/ubuntu_regular.ttf')  "
        val testFail = "    "


        // When
        val result1 = this.parser.getFontFamilyPath(test1)
        val result2 = this.parser.getFontFamilyPath(test2)
        val result3 = this.parser.getFontFamilyPath(test3)

        try {
            this.parser.getFontFamilyPath(testFail)
            throw Exception("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(MissingCSSMandatoryFieldException::class.java)
            assertThat(e.message).isEqualTo("Missing CSS mandatory value at: $testFail : ???")
        }

        // Then
        assertThat(result1).isEqualTo("file:///android_res/font/ubuntu_regular.ttf")
        assertThat(result2).isEqualTo("file:///android_res/font/ubuntu_regular.ttf")
        assertThat(result3).isEqualTo("file:///android_res/font/ubuntu_regular.ttf")
    }

    @Test
    fun test_getFontAttributes() {
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
        assertThat(result1).isEqualTo(
            listOf(
                "src: url('file:///android_res/font/ubuntu_light.ttf')",
                "font-family: 'ubuntu_light'"
            )
        )
        assertThat(result2).isEqualTo(
            listOf(
                "src: url('file:///android_res/font/ubuntu_light.ttf')",
                "font-family: 'ubuntu_light'"
            )
        )
        assertThat(result3).isEqualTo(
            listOf(
                "src: url('file:///android_res/font/ubuntu_light.ttf')",
                "font-family: 'ubuntu_light'"
            )
        )
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


    @Test
    fun test_getFontFaceFromFont() {
        // Given
        val test1 = "file:///android_res/font/ubuntu_light.ttf')"
        val testFail = "@font-face {\n" +
                "    src:  ;\n" +
                "    font-family: 'ubuntu_light';\n"


        // When
//        val context: Context = ApplicationProvider.getApplicationContext()
//        assertThat(context).isNotNull()
//        val validator = Typeface.createFromAsset(context.resources.assets, "font/ubuntu_light.ttf")
//        assertThat(validator).isNotNull()

        // TODO cannot test asset...
//        val result = this.parser.getFontFaceFromFont(context, test1)

        // Then
//        assertThat(result).isEqualTo(validator)

//        try {
//            this.parser.getFontFaceFromFont(context, testFail)
//            throw Exception("")
//        } catch (e: Exception) {
//            assertThat(e).isInstanceOf(MissingTypeFaceResourceException::class.java)
//            assertThat(e.message).isEqualTo("Typeface path in css not found at: $testFail")
//        }
    }
}
package com.antartic.sudio.htmlsnacksproject

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.antartic.sudio.snackshtml.SnacksHtml
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class MainActivity : AppCompatActivity() {
    private val snacks: SnacksHtml by lazy { findViewById(R.id.snacksHtml) }
    private val wv: WebView by lazy { findViewById(R.id.webView) }
    companion object {
        private const val VIEW_PORT = "<meta name=\"viewport\" content=\"width=device-width\">"// <style>\"word-wrap: break-word;\"</style>"
        private const val ARTICLE_PATH = "file:///android_asset/css/simple_article.css"
        private const val HTML_LOAD_ARTICLE =
            "<head><link rel='stylesheet' type='text/css' href='$ARTICLE_PATH'/></head>"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = "<p><h1 style=\"margin: 50px; text-align:center;\">First <u>test</u></h1>\n<h1>Second test</h1><p>\n<img src=\"https://picsum.photos/id/1/500/500\" alt=\"Koï\" width=\"500\" height=\"600\">"
        val text2 = //"<h2 style=\"margin: 50px; text-align:center;\">test alexis H2</h2>\n" +
//                "<h1>test alexis H1test alexis H1test alexis H1test alexis H1</h1>\n" +
//                "<h2>test alexis H2test alexis H2test alexis H2test alexis H2</h2>\n" +
//                "<h3>test alexis H3test alexis H3test alexis H3test alexis H3</h3>\n" +
//                "<h4>test alexis H4test alexis H4test alexis H4test alexis H4test alexis H4</h4>\n" +
//                "<h5>test alexis H5test alexis H5test alexis H5test alexis H5H5test alexis H5test alexis H5test alexis H5</h5>\n" +
//                "<h6>test alexis H6test alexis H6test alexis H6test alexis H6H6test alexis H6test alexis H6test alexis H6</h6>\n" +
//                "<p>test alexis Ptest alexis Ptest alexis Ptest alexis Ptest alexis P</p>\n" +
                "<h6>test alexis <u>Ptest alexis</u> Ptest alexis Ptest alexis H6</h6>\n"+
                "  <p>test <strong><u>alexis</u></strong> P</p>\n" +
                "<p>It's important to sharpen our skills digital communications skills, understand the best ways to present ourselves and the information we're trying to communicate&nbsp;to improve the effectiveness of our communications with both clients and colleagues.</p>\n" +
                "<p>With a better understanding of when we should use different types of communications, and the best way to get our message across with each of them, we will be in a better position to do our jobs well, be more comfortable with this new normal of working remotely and ready to make the most of every interaction.</p>\n" +
                "<p><img src=\"https://picsum.photos/id/1/500/500\"></img></p>\n" +
                "<p>We've assembled some short and targeted training to help you reach the next level in online communications. You can find it here:<a href=\"https://degreed.com/pathway/m90l3krdp6?path=make-an-impact-virtually#/pathway\">https://degreed.com/pathway/m90l3krdp6?path=make-an-impact-virtually#/pathway</a></p>"

        val html = HTML_LOAD_ARTICLE + text2//VIEW_PORT + HTML_LOAD_ARTICLE + text2
        val result = "<html><body style=\"margin: 0px;padding-top: 0px;\">$html</body></html>"

        wv.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null)
        wv.setBackgroundColor(Color.GREEN)
        snacks.imageGetter = object: SnacksHtml.ImageGetter {
            override fun getImageFromUlr(
                url: String?,
                imageView: ImageView,
                onImageLoaded: () -> Unit
            ) {
                Glide.with(imageView)
                    .load(url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            onImageLoaded.invoke()
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            onImageLoaded.invoke()
                            return false
                        }
                    })
                    .into(imageView)

            }
        }
        snacks.parseHtml(result)
    }
}
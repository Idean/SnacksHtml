package com.idean.snackshtmlproject

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.idean.snackshtml.SnacksHtml


class MainActivity : AppCompatActivity() {
    private val snacks: SnacksHtml by lazy { findViewById(R.id.snacksHtml) }
    private val wv: WebView by lazy { findViewById(R.id.webView) }

    companion object {
        private const val ARTICLE_PATH = "file:///android_asset/css/simple_article.css"
        private const val HTML_LOAD_ARTICLE = "<head><link rel='stylesheet' type='text/css' href='$ARTICLE_PATH'/></head>"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rawHtml = "<h1>1. What is <a href=\"https://degreed.com/pathway/m90l3krdp6?path=make-an-impact-virtually#/pathway\">https://degreed.com/pathway/m90l3krdp6?path=make-an-impact-virtually#/pathway</a> </h1>\n" +
                    "<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>\n" +
                    "<h2>2. What is Lorem Ipsum?</h2>\n" +
                    "<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.</p>\n" +
                    "<h3><u>3. What is Lorem Ipsum?</u></h3>\n" +
                    "<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing</p>\n" +
                    "<p><img src=\"https://picsum.photos/id/1/500/500\"></img></p>\n" +
                    "<p>Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, here:<a href=\"https://degreed.com/pathway/m90l3krdp6?path=make-an-impact-virtually#/pathway\">https://degreed.com/pathway/m90l3krdp6?path=make-an-impact-virtually#/pathway</a></p>"

        val html = HTML_LOAD_ARTICLE + rawHtml
        val result = "<html><body style=\"margin: 0px;padding-top: 0px;\">$html</body></html>"

        wv.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null)

        snacks.imageGetter = object : SnacksHtml.ImageGetter {
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
        snacks.linkCallback = object: SnacksHtml.LinkCallback {
            override fun onLinkClicked(url: String?) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }

        }
        snacks.parseHtml(result)
    }
}
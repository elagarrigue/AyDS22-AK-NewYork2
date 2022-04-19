package ayds.newyork.songinfo.moredetails.fulllogic

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import ayds.newyork.songinfo.R
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.Gson
import com.google.gson.JsonObject
import android.content.Intent
import android.net.Uri
import com.squareup.picasso.Picasso
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    fun getARtistInfo(artistName: String?) {
        val NYTimesAPI = initializeAPI()

        Log.e("TAG", "artistName $artistName")
        Thread {
            var textFromNYTimes = DataBase.getInfo(dataBase, artistName)
            if (textFromNYTimes != null) {
                textFromNYTimes = "[*]$textFromNYTimes"
            } else {
                val callResponse: Response<String>
                try {
                    callResponse = NYTimesAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    val javaObject = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val response = javaObject["response"].asJsonObject
                    val abstract = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val articleUrl = response["docs"].asJsonArray[0].asJsonObject["web_url"]
                    if (abstract == null) {
                        textFromNYTimes = "No Results"
                    } else {
                        textFromNYTimes = abstract.asString.replace("\\n", "\n")
                        textFromNYTimes = textToHtml(textFromNYTimes, artistName)
                        DataBase.saveArtist(dataBase, artistName, textFromNYTimes)
                    }
                    createButtonWithLink(articleUrl.asString)
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            applyImageAndText(textFromNYTimes)
        }.start()
    }

    private fun createButtonWithLink(urlString: String?) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun applyImageAndText(text: String?) {
        val imageUrl =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
        Log.e("TAG", "Get Image from $imageUrl")
        val finalText = text
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            textPane2!!.text = Html.fromHtml(finalText)
        }
    }

    private fun initializeAPI(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val NYTimesAPI = retrofit.create(NYTimesAPI::class.java)
        return NYTimesAPI
    }

    private var dataBase: DataBase? = null
    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase, "test", "sarasa")
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "test"))
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "nada"))
        getARtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace("(?i)" + term!!.toRegex(), "<b>" + term.toUpperCase(Locale.getDefault()) + "</b>")
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}
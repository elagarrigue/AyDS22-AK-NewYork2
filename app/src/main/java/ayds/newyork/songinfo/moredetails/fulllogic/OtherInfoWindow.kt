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
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

private const val NY_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
private const val NY_TIMES_IMG = ""

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    private fun getArtistInfo(artistName: String?) {
        val newYorkTimesAPI = initializeAPI()

        Log.e("TAG", "artistName $artistName")
        Thread {
            var textFromNYTimes = DataBase.getInfo(dataBase, artistName)
            if (textFromNYTimes != null) {
                textFromNYTimes = "[*]$textFromNYTimes"
            } else {
                val callResponse: Response<String>
                try {
                    callResponse = newYorkTimesAPI.getArtistInfo(artistName).execute()
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

    private fun getTextFromExternal(artistInfo: JsonObject?, artistName: String?): String {
        var result : String
        val abstract = getAbstractFromArtistInfo(artistInfo)
        if (abstract == null) {
            result = "No Results"
        } else {
            result = abstract.replace("\\n", "\n")
            result = textToHtml(result, artistName)
        }
        return result
    }

    private fun getAbstractFromArtistInfo(artistInfo : JsonObject?) : String? {
        return artistInfo!!["docs"].asJsonArray[0].asJsonObject["abstract"].asString
    }

    private fun getURLFromArtistInfo(artistInfo: JsonObject?) : String? {
        return artistInfo!!["docs"].asJsonArray[0].asJsonObject["web_url"].asString
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
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            textPane2!!.text = HtmlCompat.fromHtml(text.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun initializeAPI(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(NY_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }

    private fun getRawArtistInfoFromService(artistName: String?) : Response<String>{
        val nyTimesAPI = initializeAPI()
        return nyTimesAPI.getArtistInfo(artistName).execute()
    }

    private fun getArtistInfoFromServiceAsJsonObject(artistName: String?): JsonObject? {
        val rawArtistInfo: Response<String>
        var result: JsonObject? = null
        try{
            rawArtistInfo = getRawArtistInfoFromService(artistName)
            Log.e("TAG", "JSON " + rawArtistInfo.body())
            val javaObject = Gson().fromJson(rawArtistInfo.body(), JsonObject::class.java)
            result = javaObject["response"].asJsonObject
        } catch (e: IOException){
            Log.e("TAG", "Error $e")
            e.printStackTrace()
        }
        return result
    }

    private var dataBase: DataBase? = null
    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase, "test", "sarasa")
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "test"))
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "nada"))
        getArtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("<html><div width=400>")
            stringBuilder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace("(?i)" + term!!.toRegex(), "<b>" + term.uppercase(Locale.getDefault()) + "</b>")
            stringBuilder.append(textWithBold)
            stringBuilder.append("</font></div></html>")
            return stringBuilder.toString()
        }
    }
}
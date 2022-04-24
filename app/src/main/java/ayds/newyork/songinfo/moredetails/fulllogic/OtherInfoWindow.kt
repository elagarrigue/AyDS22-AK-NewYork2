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
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

private const val NY_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
private const val NY_TIMES_IMG = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null
    private var dataBase: DataBase? = null
    private var artistName: String? = null
    private var abstractNYTimes: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        initializeDatabase()
        obtainArtistName()
        getArtistInfo()
    }

    private fun obtainArtistName(){
        artistName = intent.getStringExtra("artistName")
    }

    private fun getArtistInfo() {
        Thread {
            abstractNYTimes = DataBase.getInfo(dataBase, artistName)
            if (abstractNYTimes != null) {
                abstractNYTimes = "[*]$abstractNYTimes"
            } else {
                var articleUrl : String?
                try {
                    val artistInfoJsonObject = getArtistInfoFromExternal()
                    abstractNYTimes = getTextFromExternal(artistInfoJsonObject)
                    articleUrl = getURLFromArtistInfo(artistInfoJsonObject)
                    DataBase.saveArtist(dataBase, artistName, abstractNYTimes)
                    createButtonWithLink(articleUrl)
                } catch (error : Exception){
                    abstractNYTimes = "No se encontró"
                    articleUrl = "http://www.google.com"
                }
            }
            applyImageAndText()
        }.start()
    }

    private fun getTextFromExternal(artistInfo: JsonObject?): String {
        val result : String
        val abstract = getAbstractFromArtistInfo(artistInfo)
        result = abstract?.replace("\\n", "\n") ?: "No Results"
        return result
    }

    private fun getAbstractFromArtistInfo(artistInfo : JsonObject?) : String? {
        return artistInfo!!["response"].asJsonObject["docs"].asJsonArray[0].asJsonObject["abstract"].asString
    }

    private fun getURLFromArtistInfo(artistInfo: JsonObject?) : String? {
        return artistInfo!!["response"].asJsonObject["docs"].asJsonArray[0].asJsonObject["web_url"].asString
    }

    private fun createButtonWithLink(urlString: String?) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun applyImageAndText() {
        runOnUiThread {
            Picasso.get().load(NY_TIMES_IMG).into(findViewById<View>(R.id.imageView) as ImageView)
            textPane2!!.text = HtmlCompat.fromHtml(textToHtml(abstractNYTimes!!, artistName), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun initializeAPI(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(NY_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }

    private fun getRawArtistInfoFromExternal() : Response<String>{
        val nyTimesAPI = initializeAPI()
        return nyTimesAPI.getArtistInfo(artistName).execute()
    }

    private fun convertResponseToJsonObject(resp : Response<String>) : JsonObject?{
        return Gson().fromJson(resp.body(), JsonObject::class.java)
        //return javaObject["response"].asJsonObject
        //return Gson().fromJson(resp.body(), JsonObject::class.java)["response"].asJsonObject
    }

    private fun getArtistInfoFromExternal(): JsonObject? {
        val rawArtistInfoFromService: Response<String>

        var result: JsonObject? = null
        try{
            rawArtistInfoFromService = getRawArtistInfoFromExternal()
            println(rawArtistInfoFromService.toString())
            result = convertResponseToJsonObject(rawArtistInfoFromService)
        } catch (e: IOException){
            e.printStackTrace()
        }
        return result
    }

    private fun initializeDatabase() {
        dataBase = DataBase(this)
    }

    private fun textToHtml(text: String, term: String?): String {
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

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
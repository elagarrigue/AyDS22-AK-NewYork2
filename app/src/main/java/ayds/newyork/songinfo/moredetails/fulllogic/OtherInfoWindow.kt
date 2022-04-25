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
import java.lang.StringBuilder
import java.util.*

private const val NY_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
private const val NY_TIMES_IMG = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT  = "abstract"
private const val WEB_URL = "web_url"

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null
    private var dataBase: DataBase? = null
    private var artistName: String? = null
    private var abstractNYTimes: String? = null
    private var urlNYTimes: String? = null

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
            abstractNYTimes = dataBase!!.getInfo(artistName!!)
            if (abstractNYTimes != null) {
                abstractNYTimes = "[*]$abstractNYTimes"
            } else {
                try {
                    getArtistInfoFromExternal()
                    if(abstractNYTimes != null){
                        dataBase!!.saveArtist(artistName, abstractNYTimes)
                    }
                    createButtonWithLink(urlNYTimes)
                } catch(e : Exception){
                    abstractNYTimes = "No hay conexión con el servicio externo."
                }
            }
            applyImageAndText()
        }.start()
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

    private fun String?.getFirstItem() : JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        val resp = jsonObject[RESPONSE].asJsonObject
        val articles = resp[DOCS].asJsonArray
        return articles[0].asJsonObject
    }

    private fun getArtistInfoFromExternal(){
        val callResponse = getRawArtistInfoFromExternal()
        parseArtistInfo(callResponse.body())
    }

    private fun parseArtistInfo(serviceData : String?){
        try{
            serviceData?.getFirstItem()?.let { item ->
                abstractNYTimes = item.getAbstract()
                urlNYTimes = item.getUrl()
            }
        } catch(e : Exception){
            abstractNYTimes = null
        }
    }

    private fun JsonObject.getAbstract(): String? {
        return this[ABSTRACT].asString
    }

    private fun JsonObject.getUrl(): String? {
        return this[WEB_URL].asString
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
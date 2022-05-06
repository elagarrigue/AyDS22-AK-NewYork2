package ayds.newyork.songinfo.moredetails

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
import android.text.Spanned
import com.squareup.picasso.Picasso
import android.widget.Button
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesAPI
import retrofit2.Response
import java.lang.StringBuilder
import java.util.*

private const val NY_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
private const val NY_TIMES_IMG = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT  = "abstract"
private const val WEB_URL = "web_url"
private const val ASTERISK = "[*]"

class OtherInfoWindow : AppCompatActivity() {

    private lateinit var textAbstract: TextView
    private lateinit var btnUrl: Button
    private lateinit var nyTimesImg: ImageView

    private lateinit var dataBase: DataBase
    private lateinit var nyTimesAPI : NYTimesAPI

    private lateinit var artistName: String
    private var abstractNYTimes: String? = null
    private var urlNYTimes: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()

        initializeAPI()
        initializeDatabase()
        obtainArtistName()
        getArtistInfo()
    }

    private fun initProperties() {
        textAbstract = findViewById(R.id.textPane2)
        btnUrl = findViewById(R.id.openUrlButton)
        nyTimesImg = findViewById(R.id.imageView)
    }

    private fun obtainArtistName(){
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun getArtistInfo() {
        Thread {
            getArtistInfoFromRepository()
            applyImage()
            applyText()
            createButtonWithLink()
        }.start()
    }

    private fun getArtistInfoFromRepository() {
        abstractNYTimes = dataBase.getInfo(artistName)
        if (abstractNYTimes != null) {
            abstractNYTimes = ASTERISK + "$abstractNYTimes"
        } else {
            try {
                getArtistInfoFromExternal()
                if (abstractNYTimes != null) {
                    dataBase.saveArtist(artistName, abstractNYTimes, urlNYTimes)
                }
            } catch (e: Exception) {
                abstractNYTimes = null
            }
        }
    }

    private fun createButtonWithLink() {
        urlNYTimes?.let{
            setListenerForLinkBtn()
        } ?: run {
            disableLinkBtn()
        }
    }

    private fun setListenerForLinkBtn(){
        btnUrl.setOnClickListener {
            openLink(urlNYTimes)
        }
    }

    private fun disableLinkBtn(){
        runOnUiThread {
            btnUrl.isEnabled = false
        }
    }

    private fun openLink(urlString: String?){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }

    private fun applyText() {
        runOnUiThread {
            textAbstract.text = getAbstractAsHtml()
        }
    }

    private fun getAbstractAsHtml() : Spanned{
        return formatHtml(renderAbstractAsHtml(getAbstractText(), artistName))
    }

    private fun getAbstractText() : String {
        return abstractNYTimes ?: "No se encontró"
    }

    private fun formatHtml(abstractHtml : String) : Spanned {
        return HtmlCompat.fromHtml(
            abstractHtml,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun applyImage() {
        runOnUiThread {
            Picasso.get().load(NY_TIMES_IMG).into(nyTimesImg)
        }
    }

    private fun initializeAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(NY_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        nyTimesAPI = retrofit.create(NYTimesAPI::class.java)
    }

    private fun getRawArtistInfoFromExternal() : Response<String>{
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

    private fun renderAbstractAsHtml(abstract: String, artistName: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<html><div width=400>")
        stringBuilder.append("<font face=\"arial\">")
        val textWithBold = abstract
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)$artistName".toRegex(), "<b>" + artistName.uppercase(Locale.getDefault()) + "</b>")
        stringBuilder.append(textWithBold)
        stringBuilder.append("</font></div></html>")
        return stringBuilder.toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
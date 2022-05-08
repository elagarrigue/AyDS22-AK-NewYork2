package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.lang.StringBuilder
import java.util.*

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT  = "abstract"
private const val WEB_URL = "web_url"


class NYTimesToArtistResolverImpl : NYTimesToArtistResolver {

    override fun getArtistFromExternalData(serviceData: String?, artistName: String) : ArtistInfo? {
        return try{
            serviceData?.getFirstItem()?.let { item ->
                ArtistInfo(
                    artistName,
                    item.getInfo(),
                    item.getUrl(artistName)
                )
            }
        } catch(e : Exception){
            null
        }
    }

    private fun JsonObject.getInfo(): String {
        return this[ABSTRACT].asString
    }

    private fun JsonObject.getUrl(artistName: String): String {
        return renderAbstractAsHtml(this[WEB_URL].asString,artistName)
    }

    private fun String?.getFirstItem() : JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        val resp = jsonObject[RESPONSE].asJsonObject
        val articles = resp[DOCS].asJsonArray
        return articles[0].asJsonObject
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
}
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
        return try {
            serviceData?.getFirstItem()?.let { item ->
                ArtistInfo(
                    artistName,
                    item.getInfo(),
                    item.getUrl()
                )
            }
        } catch(e : Exception){
            null
        }
    }

    private fun JsonObject.getInfo(): String {
        return this[ABSTRACT].asString
    }

    private fun JsonObject.getUrl(): String {
        return this[WEB_URL].asString
    }

    private fun String?.getFirstItem() : JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        val resp = jsonObject[RESPONSE].asJsonObject
        val articles = resp[DOCS].asJsonArray
        return articles[0].asJsonObject
    }
}
package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Card
import java.lang.StringBuilder
import java.util.*

interface ArtistInfoDescriptionHelper {
    fun getCardText(artist: Card) : String
    fun getNotFoundText() : String
}

private const val NOT_FOUND = "Artist not found"
private const val LOCALLY_STORED_SYMBOL = "[*]"
private const val WIDTH = "400"
private const val HEADER = "<html><div width=$WIDTH>"
private const val FONT_NAME = "arial"
private const val FONT = "<font face=\"$FONT_NAME\">"
private const val CLOSE_HEADER = "</font></div></html>"
private const val LINE_BREAK = "<br>"
private const val OPEN_BOLD = "<b>"
private const val CLOSE_BOLD = "</b>"

internal class ArtistInfoDescriptionHelperImpl : ArtistInfoDescriptionHelper {

    override fun getCardText(artist: Card): String {
        val abstract  = "${if (artist.isLocallyStored) LOCALLY_STORED_SYMBOL else "" } ${artist.description}"
        return getFormattedArtistInfoTextArtist(abstract, artist)
    }


    private fun getFormattedArtistInfoTextArtist(abstract: String, artist: Card): String {
        return boldArtisName(renderAbstractAsHtml(abstract),artist.artistName)
    }

    override fun getNotFoundText(): String {
        return renderAbstractAsHtml(NOT_FOUND)
    }

    private fun renderAbstractAsHtml(abstract: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(HEADER)
        stringBuilder.append(FONT)
        val renderText = abstract
            .replace("'", " ")
            .replace("\n", LINE_BREAK)
        stringBuilder.append(renderText)
        stringBuilder.append(CLOSE_HEADER)
        return stringBuilder.toString()
    }

    private fun boldArtisName(abstract: String, artistName: String) : String{
        return abstract.replace("(?i)$artistName".toRegex(), OPEN_BOLD + artistName.uppercase(Locale.getDefault()) + CLOSE_BOLD)
    }
}
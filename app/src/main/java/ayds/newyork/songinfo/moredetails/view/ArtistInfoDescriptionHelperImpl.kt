package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtist
import java.lang.StringBuilder
import java.util.*

interface ArtistInfoDescriptionHelper {
    fun getArtistInfoText(artist: Artist = EmptyArtist) : String
}

private const val NY_NOT_FOUND = "Artist not found"
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

    override fun getArtistInfoText(artist: Artist): String {
        val rawArtistInfoText : String = when (artist) {
            is ArtistInfo ->
                (if (artist.isLocallyStored) LOCALLY_STORED_SYMBOL else "") + " ${artist.artistInfo} "

            else -> NY_NOT_FOUND
        }
        return renderAbstractAsHtml(rawArtistInfoText,artist.artistName)
    }

    private fun renderAbstractAsHtml(abstract: String, artistName: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(HEADER)
        stringBuilder.append(FONT)
        val textWithBold = abstract
            .replace("'", " ")
            .replace("\n", LINE_BREAK)
            .replace("(?i)$artistName".toRegex(), OPEN_BOLD + artistName.uppercase(Locale.getDefault()) + CLOSE_BOLD)
        stringBuilder.append(textWithBold)
        stringBuilder.append(CLOSE_HEADER)
        return stringBuilder.toString()
    }
}
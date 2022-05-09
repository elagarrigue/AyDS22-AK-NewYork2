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
private const val ASTERISK = "[*]"

internal class ArtistInfoDescriptionHelperImpl : ArtistInfoDescriptionHelper {

    override fun getArtistInfoText(artist: Artist): String {
        val toReturn : String = when (artist) {
            is ArtistInfo ->
                (if (artist.isLocallyStored) ASTERISK else "") + " ${artist.artistInfo} "

            else -> NY_NOT_FOUND
        }
        return renderAbstractAsHtml(toReturn,artist.artistName)
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
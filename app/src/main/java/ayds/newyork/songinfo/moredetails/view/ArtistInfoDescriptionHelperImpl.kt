package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtist

interface ArtistInfoDescriptionHelper {
    fun getArtistInfoText(artist: Artist = EmptyArtist) : String
}

private const val NY_NOT_FOUND = "Artist not found"
private const val ASTERISK = "[*]"

internal class ArtistInfoDescriptionHelperImpl : ArtistInfoDescriptionHelper {
    override fun getArtistInfoText(artist: Artist): String {
        return when (artist) {
            is ArtistInfo ->
                "${
                    if (artist.isLocallyStored) ASTERISK else "" +" ${artist.artistInfo} " 
                }\n"

            else -> NY_NOT_FOUND
        }
    }
}
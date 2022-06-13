package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.lisboa1.lastfm.LastFMArtistBiography
import ayds.lisboa1.lastfm.LastFMService
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import java.lang.Exception

internal class LastFMProxy(private val lastFmService : LastFMService) : Proxy {
    override fun getCard(name: String): Card? {
        return try{
            lastFmService.getArtistBio(name)?.let{ mapCard(it) }
        } catch(e : Exception){
            null
        }
    }

    private fun mapCard(artist: LastFMArtistBiography): ExternalCard {
        return ExternalCard(
            artist.artist,
            artist.biography,
            artist.articleUrl,
            Source.LASTFM,
            artist.logoUrl
        )
    }
}
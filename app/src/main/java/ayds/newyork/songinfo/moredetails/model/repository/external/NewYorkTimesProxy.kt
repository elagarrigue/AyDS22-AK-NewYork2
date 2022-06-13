package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.newyork2.newyorkdata.nytimes.NYTimesArtistInfo
import ayds.newyork2.newyorkdata.nytimes.NYTimesService
import java.lang.Exception

internal class NewYorkTimesProxy(private val nytimesService : NYTimesService) : Proxy {
    override fun getCard(name: String): Card? {
        return try{
            nytimesService.getArtist(name)?.let{ mapCard(it) }
        } catch(e : Exception){
            null
        }
    }

    private fun mapCard(artist: NYTimesArtistInfo): ExternalCard {
        return ExternalCard(
            artist.artistName,
            artist.artistInfo,
            artist.artistUrl,
            Source.NEWYORKTIMES,
            artist.source_logo_url
        )
    }
}
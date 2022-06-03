package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.newyork2.newyorkdata.nytimes.NYTimesService
import java.lang.Exception

internal class NewYorkTimesProxy(private val nytimesService : NYTimesService) : Proxy {
    override fun getCard(name: String): Card? {
        var card : Card? = null
        try{
            val artist = nytimesService.getArtist(name)
            if (artist != null) {
                card = ExternalCard(
                    artist.artistName,
                    artist.artistInfo,
                    artist.artistUrl,
                    Source.NEWYORKTIMES,
                    artist.source_logo_url
                )
            }
        } catch(e : Exception){
            card = null
        }
        return card
    }
}
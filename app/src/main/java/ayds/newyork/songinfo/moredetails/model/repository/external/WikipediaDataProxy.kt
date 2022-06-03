package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.winchester2.wikipedia.ExternalRepository
import java.lang.Exception

internal class WikipediaDataProxy(private val wikiService : ExternalRepository) : Proxy {
    override fun getCard(name: String): Card? {
        var card : Card? = null
        try{
            val artist = wikiService.getArtistDescription(name)
            if (artist != null) {
                card = ExternalCard(
                    name,
                    artist.description,
                    artist.source,
                    Source.WIKIPEDIA,
                    artist.sourceLogo
                )
            }
        } catch(e : Exception){
            card = null
        }
        return card
    }
}
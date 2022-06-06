package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.winchester2.wikipedia.ExternalRepository
import ayds.winchester2.wikipedia.WikipediaArticle
import java.lang.Exception

internal class WikipediaDataProxy(private val wikiService : ExternalRepository) : Proxy {
    override fun getCard(name: String): Card? {
        var card : Card? = null
        try{
            wikiService.getArtistDescription(name)?.let { card = mapCard(it,name) }
        } catch(e : Exception){
            card = null
        }
        return card
    }

    private fun mapCard(artist: WikipediaArticle, name : String): ExternalCard {
        return ExternalCard(
            name,
            artist.description,
            artist.source,
            Source.WIKIPEDIA,
            artist.sourceLogo
        )
    }
}
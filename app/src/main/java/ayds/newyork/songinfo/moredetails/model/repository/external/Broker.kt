package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface Broker  {
    fun getCards(name : String): List<Card>
}

internal class BrokerImpl(
    private val nytimesProxy: NewYorkTimesProxy,
    private val lastFMProxy: LastFMProxy,
    private val wikipediaProxy: WikipediaDataProxy
) : Broker {
    override fun getCards(name: String): List<Card> {
        var cardsList : MutableList<Card> = mutableListOf()
        val nyTimesCard = nytimesProxy.getCard(name)
        val lastFmCard = lastFMProxy.getCard(name)
        val wikipediaCard = wikipediaProxy.getCard(name)
        if (nyTimesCard != null){
            cardsList.add(nyTimesCard)
        }
        if (lastFmCard != null){
            cardsList.add(lastFmCard)
        }
        if (wikipediaCard != null){
            cardsList.add(wikipediaCard)
        }
        return cardsList
    }
}
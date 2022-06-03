package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface Broker  {
    fun getCards(name : String): List<Card>
}

internal class BrokerImpl(private val nytimesProxy: NewYorkTimesProxy) : Broker {
    override fun getCards(name: String): List<Card> {
        var cardsList : MutableList<Card> = mutableListOf()
        val nyTimesCard = nytimesProxy.getCard(name)
        if (nyTimesCard != null){
            cardsList.add(nyTimesCard)
        }
        return cardsList
    }

}
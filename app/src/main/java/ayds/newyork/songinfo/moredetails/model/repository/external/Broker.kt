package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface Broker  {
    fun getCards(name : String): List<Card>
}

internal class BrokerImpl(
    private val proxyList: List<Proxy>
) : Broker {

    override fun getCards(name: String): List<Card> {
        var cardsList : MutableList<Card> = mutableListOf()
        proxyList.forEach{ proxy ->
            proxy.getCard(name)?.let { cardsList.add(it)}
        }
        return cardsList
    }

}
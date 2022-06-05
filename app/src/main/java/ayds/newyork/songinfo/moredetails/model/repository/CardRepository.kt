package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.external.Broker
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.LocalStorage

interface CardRepository {

    fun getCardsByName(name: String) : List<Card>
}

internal class CardRepositoryImpl (
    private val localStorage: LocalStorage,
    private val servicesBroker : Broker
) : CardRepository {

    override fun getCardsByName(name: String): List<Card> {
        var localCardsList = localStorage.getCardsByName(name)
        if (!localCardsList.isEmpty()) {
            markCardAsLocal(localCardsList)
        } else {
            try {
                val externalCardsList = servicesBroker.getCards(name)
                if (!externalCardsList.isEmpty()) {
                    localStorage.saveCards(externalCardsList)
                }
                localCardsList = externalCardsList
            } catch (e: Exception) {
                localCardsList = mutableListOf()
            }
        }
        return localCardsList
    }

    private fun markCardAsLocal(cardsList : List<Card>) {
        cardsList.forEach {
            it.isLocallyStored = true
        }
    }
}
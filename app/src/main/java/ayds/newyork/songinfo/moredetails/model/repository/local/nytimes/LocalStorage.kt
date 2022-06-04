package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface LocalStorage {

    fun getCardsByName(name : String) : List<Card>

    fun saveCards(artist : List<Card>)
}
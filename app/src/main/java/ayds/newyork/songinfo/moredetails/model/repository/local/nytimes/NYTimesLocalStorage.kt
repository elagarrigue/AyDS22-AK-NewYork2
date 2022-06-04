package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface NYTimesLocalStorage {

    fun getArtistByName(name : String) : List<Card>

    fun saveArtist(artist : List<Card>)
}
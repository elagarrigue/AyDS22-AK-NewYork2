package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard

interface NYTimesLocalStorage {

    fun getArtistByName(name : String) : ExternalCard?

    fun saveArtist(artist : ExternalCard)
}
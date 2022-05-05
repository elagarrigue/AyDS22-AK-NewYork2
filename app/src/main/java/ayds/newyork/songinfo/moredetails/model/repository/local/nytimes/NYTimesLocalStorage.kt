package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface NYTimesLocalStorage {

    fun getArtistByName(name : String) : ArtistInfo?

    fun getArtistById(id : String) : ArtistInfo?

    fun saveArtist(artist : ArtistInfo)
}
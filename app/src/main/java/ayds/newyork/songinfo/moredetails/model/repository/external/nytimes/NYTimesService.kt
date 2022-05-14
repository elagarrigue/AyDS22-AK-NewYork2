package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface NYTimesService {

    fun getArtist(artistName: String) : ArtistInfo?
}
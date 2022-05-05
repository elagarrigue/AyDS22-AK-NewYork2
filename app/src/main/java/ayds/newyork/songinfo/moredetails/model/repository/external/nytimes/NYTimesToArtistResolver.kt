package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface NYTimesToArtistResolver {

    fun getArtistFromExternalData(serviceData: String?, artistName: String) : ArtistInfo?
}
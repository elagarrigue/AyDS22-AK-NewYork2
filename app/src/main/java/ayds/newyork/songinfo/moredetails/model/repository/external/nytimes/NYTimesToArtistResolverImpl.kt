package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

class NYTimesToArtistResolverImpl : NYTimesToArtistResolver {

    override fun getArtistFromExternalData(serviceData: String?, artistName: String) : ArtistInfo? {
        return null
    }
}
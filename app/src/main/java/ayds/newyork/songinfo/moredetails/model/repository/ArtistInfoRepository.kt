package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface ArtistInfoRepository {

    fun getArtistById(id : String) : ArtistInfo

    fun getArtistByName(name : String) : ArtistInfo
}

internal class ArtistInfoRepositoryImpl : ArtistInfoRepository {

    override fun getArtistById(id: String): ArtistInfo {
        TODO("Not yet implemented")
    }

    override fun getArtistByName(name: String): ArtistInfo {
        TODO("Not yet implemented")
    }

}
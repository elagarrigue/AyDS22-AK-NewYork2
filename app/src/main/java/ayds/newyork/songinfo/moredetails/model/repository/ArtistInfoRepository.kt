package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage

interface ArtistInfoRepository {

    fun getArtistById(id : String) : ArtistInfo

    fun getArtistByName(name : String) : ArtistInfo
}

private const val ASTERISK = "[*]"

internal class ArtistInfoRepositoryImpl (
    private val nyTimesLocalStorage: NYTimesLocalStorage
) : ArtistInfoRepository {

    override fun getArtistById(id: String): ArtistInfo {
        TODO("Not yet implemented")
    }

    override fun getArtistByName(name: String): ArtistInfo {
        TODO("Not yet implemented")
    }

}
package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork2.newyorkdata.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage
import ayds.newyork2.newyorkdata.nytimes.NYTimesArtistInfo

private const val SOURCE_LOGO_URL : String = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

interface ArtistInfoRepository {

    fun getArtistByName(name: String) : Card
}

internal class ArtistInfoRepositoryImpl (
    private val nyTimesLocalStorage: NYTimesLocalStorage,
    private val nytService : NYTimesService
) : ArtistInfoRepository {

    override fun getArtistByName(name: String): Card {
        var artistInfo = nyTimesLocalStorage.getArtistByName(name)
        if (artistInfo != null) {
            markArtistInfoAsLocal(artistInfo)
        } else {
            try {
                val artist = nytService.getArtist(name)
                artistInfo = mapArtistInfo(artist)
                if (artistInfo != null) {
                    nyTimesLocalStorage.saveArtist(artistInfo)
                }
            } catch (e: Exception) {
                artistInfo = null
            }
        }
        return artistInfo ?: EmptyCard
    }

    private fun markArtistInfoAsLocal(artistInfo : ExternalCard) {
        artistInfo.isLocallyStored = true
    }

    private fun mapArtistInfo(artist: NYTimesArtistInfo?): ExternalCard? {
        return if (artist!=null) {
        ExternalCard(
            artist.artistName,
            artist.artistInfo,
            artist.artistUrl,
            1,
            SOURCE_LOGO_URL,
            false
        )
        }
        else
            null
    }
}
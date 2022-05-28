package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtist
import ayds.newyork2.newyorkdata.external.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage
import ayds.newyork2.newyorkdata.external.nytimes.ExternalArtistInfo

interface ArtistInfoRepository {

    fun getArtistByName(name: String) : Artist
}

internal class ArtistInfoRepositoryImpl (
    private val nyTimesLocalStorage: NYTimesLocalStorage,
    private val nytService : NYTimesService
) : ArtistInfoRepository {

    override fun getArtistByName(name: String): Artist {
        var artistInfo = nyTimesLocalStorage.getArtistByName(name)
        if (artistInfo != null) {
            markArtistInfoAsLocal(artistInfo)
        } else {
            try {
                var artist = nytService.getArtist(name)
                artistInfo = mapArtistInfo(artist)
                if (artistInfo != null) {
                    nyTimesLocalStorage.saveArtist(artistInfo)
                }
            } catch (e: Exception) {
                artistInfo = null
            }
        }
        return artistInfo ?: EmptyArtist
    }

    private fun markArtistInfoAsLocal(artistInfo : ArtistInfo) {
        artistInfo.isLocallyStored = true
    }

    private fun mapArtistInfo(artist: ExternalArtistInfo?): ArtistInfo? {
        return if (artist!=null) {
        ArtistInfo(
            artist.artistName,
            artist.artistInfo,
            artist.artistUrl,
            false
        )
        }
        else
            null
    }
}
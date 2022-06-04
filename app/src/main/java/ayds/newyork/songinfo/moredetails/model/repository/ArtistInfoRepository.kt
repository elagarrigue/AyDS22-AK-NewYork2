package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.external.Broker
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage

interface ArtistInfoRepository {

    fun getArtistByName(name: String) : List<Card>
}

internal class ArtistInfoRepositoryImpl (
    private val nyTimesLocalStorage: NYTimesLocalStorage,
    private val servicesBroker : Broker
) : ArtistInfoRepository {

    override fun getArtistByName(name: String): List<Card> {
        var artistInfo = nyTimesLocalStorage.getArtistByName(name)
        if (!artistInfo.isEmpty()) {
            markArtistInfoAsLocal(artistInfo)
        } else {
            try {
                val artist = servicesBroker.getCards(name) // lista
                if (!artist.isEmpty()) {
                    nyTimesLocalStorage.saveArtist(artist)
                }
            } catch (e: Exception) {
                artistInfo = mutableListOf()
            }
        }
        return artistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo : List<Card>) {
        artistInfo.forEach {
            it.isLocallyStored = true
        }
    }
}
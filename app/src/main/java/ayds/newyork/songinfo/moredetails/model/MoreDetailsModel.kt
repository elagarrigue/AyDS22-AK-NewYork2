package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository

interface MoreDetailsModel{

    // val ArtistObservable: falta implementar

    fun searchArtist(term: String) : Artist

    fun getArtistById(id: String): Artist
}

internal class  MoreDetailsModelImpl (private val repository: ArtistInfoRepository) : MoreDetailsModel {

    // override val ArtistObservable = Subject<ArtistInfo>()

    override fun searchArtist(name: String): Artist {
        return repository.getArtistByName(name)
    }

    override fun getArtistById(id : String) : Artist{
        return repository.getArtistById(id)
    }
}
package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel{

    val artistObservable : Observable<Artist>

    fun searchArtist(name: String) : Artist

    fun getArtistById(id: String): Artist
}

internal class  MoreDetailsModelImpl (private val repository: ArtistInfoRepository) : MoreDetailsModel {

    override val artistObservable = Subject<Artist>()

    override fun searchArtist(name: String): Artist {
        return repository.getArtistByName(name)
    }

    override fun getArtistById(id : String) : Artist{
        return repository.getArtistById(id)
    }
}
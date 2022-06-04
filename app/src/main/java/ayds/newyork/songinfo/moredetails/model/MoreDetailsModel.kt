package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val artistObservable : Observable<List<Card>>

    fun searchArtist(name: String)
}

internal class  MoreDetailsModelImpl (private val repository: ArtistInfoRepository) : MoreDetailsModel {

    override val artistObservable = Subject<List<Card>>()

    override fun searchArtist(name: String) {
        repository.getArtistByName(name).let {
            artistObservable.notify(it)
        }
    }
}
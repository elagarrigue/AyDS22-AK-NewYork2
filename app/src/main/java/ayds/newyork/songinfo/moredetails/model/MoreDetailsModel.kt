package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val artistObservable : Observable<List<Card>>

    fun searchCards(name: String)
}

internal class  MoreDetailsModelImpl (private val repository: CardRepository) : MoreDetailsModel {

    override val artistObservable = Subject<List<Card>>()

    override fun searchCards(name: String) {
        repository.getCardsByName(name).let {
            artistObservable.notify(it)
        }
    }
}
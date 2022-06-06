package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val artistObservable : Observable<Card>

    fun searchCards(name: String)
    fun updateIndex()
    fun getNextCard()
}

internal class  MoreDetailsModelImpl (private val repository: CardRepository) : MoreDetailsModel {

    override val artistObservable = Subject<Card>()
    private lateinit var cards : List<Card>
    private var index : Int = 0
    override fun searchCards(name: String) {
        repository.getCardsByName(name).let {
            artistObservable.notify(it[index])
            cards = it
        }
    }

    override fun updateIndex() {
        index += 1
        if(index >= cards.size){
            index = 0
        }
    }

    override fun getNextCard() {
        if(!cards.isEmpty()){
            artistObservable.notify(cards[index])
        }else{
            artistObservable.notify(EmptyCard)
        }

    }
}
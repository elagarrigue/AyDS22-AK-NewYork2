package ayds.newyork.songinfo.moredetails.controller

import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.view.MoreDetailsEvent
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer

interface MoreDetailsController {
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController{

    private lateinit var moreDetailsView: MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        moreDetailsView.moreDetailsEventObservable.subscribe(observer)
    }

    private val observer: Observer<MoreDetailsEvent> =
        Observer { value ->
            when (value) {
                MoreDetailsEvent.GetNextCard -> getNextCard()
                MoreDetailsEvent.GetArtistInfo -> getArtistInfo()
                MoreDetailsEvent.OpenArtistInfoLink -> openArtistInfoUrl()
            }
        }

    private fun getNextCard(){
        moreDetailsModel.getNextCard()
    }

    private fun getArtistInfo() {
        Thread{
            moreDetailsModel.searchCards(moreDetailsView.uiState.name)
        }.start()
    }

    private fun openArtistInfoUrl() {
        moreDetailsView.openExternalLink(moreDetailsView.uiState.url)
    }

}
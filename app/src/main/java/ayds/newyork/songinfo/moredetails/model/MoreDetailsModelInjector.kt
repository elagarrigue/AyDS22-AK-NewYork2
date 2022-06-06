package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.lisboa1.lastfm.LastFMInjector
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import ayds.newyork.songinfo.moredetails.model.repository.CardRepositoryImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.*
import ayds.newyork.songinfo.moredetails.model.repository.external.BrokerImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.LastFMProxy
import ayds.newyork.songinfo.moredetails.model.repository.external.NewYorkTimesProxy
import ayds.newyork.songinfo.moredetails.model.repository.external.WikipediaDataProxy
import ayds.newyork2.newyorkdata.nytimes.NYTimesInjector
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.LocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb.LocalStorageImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.winchester2.wikipedia.WikipediaInjector


object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val nytimesProxy = NewYorkTimesProxy(NYTimesInjector.nyTimesService)
        val lastFmProxy = LastFMProxy(LastFMInjector.lastFMService)
        val wikipediaProxy = WikipediaDataProxy(WikipediaInjector.wikipediaService)
        val proxyList : List<Proxy> = mutableListOf(nytimesProxy,lastFmProxy,wikipediaProxy)
        val broker : Broker = BrokerImpl(proxyList)

        val localStorage : LocalStorage = LocalStorageImpl(moreDetailsView as Context)
        val repository : CardRepository = CardRepositoryImpl(localStorage, broker)
        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}
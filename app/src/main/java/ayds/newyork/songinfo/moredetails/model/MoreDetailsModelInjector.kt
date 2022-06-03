package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepositoryImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.Broker
import ayds.newyork.songinfo.moredetails.model.repository.external.BrokerImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.NewYorkTimesProxy
import ayds.newyork2.newyorkdata.nytimes.NYTimesInjector
import ayds.newyork2.newyorkdata.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb.NYTimesLocalStorageImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView


object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val nytimesProxy = NewYorkTimesProxy(NYTimesInjector.nyTimesService)
        val broker : Broker = BrokerImpl(nytimesProxy)

        val nyTimesLocalStorage : NYTimesLocalStorage = NYTimesLocalStorageImpl(moreDetailsView as Context)
        val nytService : NYTimesService = NYTimesInjector.nyTimesService
        val repository : ArtistInfoRepository = ArtistInfoRepositoryImpl(nyTimesLocalStorage, nytService)
        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}
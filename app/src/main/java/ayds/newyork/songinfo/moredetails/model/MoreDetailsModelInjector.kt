package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepositoryImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesInjector
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb.NYTimesLocalStorageImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView


object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getHomeModel(): MoreDetailsModel = moreDetailsModel

    fun initHomeModel(moreDetailsView: MoreDetailsView) {
        val nyTimesLocalStorage : NYTimesLocalStorage = NYTimesLocalStorageImpl(moreDetailsView as Context)
        val nytService : NYTimesService = NYTimesInjector.nyTimesService
        val repository : ArtistInfoRepository = ArtistInfoRepositoryImpl()
        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}
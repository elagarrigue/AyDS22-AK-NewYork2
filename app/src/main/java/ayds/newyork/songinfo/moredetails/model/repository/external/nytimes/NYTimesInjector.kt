package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val NY_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"

object NYTimesInjector {

    private val artistResolver : NYTimesToArtistResolver = NYTimesToArtistResolverImpl()
    private val apiFromNYTimes : NYTimesAPI = initializeAPI().create(NYTimesAPI::class.java)
    val nyTimesService: NYTimesService = NYTimesServiceImpl(artistResolver,apiFromNYTimes)

    private fun initializeAPI() =
        Retrofit.Builder()
            .baseUrl(NY_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
}
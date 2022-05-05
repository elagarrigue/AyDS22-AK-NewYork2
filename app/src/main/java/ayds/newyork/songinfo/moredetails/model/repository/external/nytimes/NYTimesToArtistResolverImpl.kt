package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

    private const val NY_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"

class NYTimesToArtistResolverImpl : NYTimesToArtistResolver {

    private val artistResolver : NYTimesToArtistResolver = NYTimesToArtistResolverImpl()
    private val api : NYTimesAPI = initializeAPI().create(NYTimesAPI::class.java)

    fun getArtistFromExternalData(serviceData: String?, artistName: String) : ArtistInfo? {
        return null
    }

    private fun initializeAPI() =
        Retrofit.Builder()
            .baseUrl(NY_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

}
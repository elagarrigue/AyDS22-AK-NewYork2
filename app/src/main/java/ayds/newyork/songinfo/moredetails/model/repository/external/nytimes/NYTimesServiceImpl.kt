package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val NY_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"

class NYTimesServiceImpl : NYTimesService{

    private val artistResolver : NYTimesToArtistResolver = NYTimesToArtistResolverImpl()
    private val apiFromNYTimes : NYTimesAPI = initializeAPI().create(NYTimesAPI::class.java)

    override fun getArtist(artistName: String) : ArtistInfo? {
        val callResponse = getRawArtistInfoFromExternal(artistName)
        return artistResolver.getArtistFromExternalData(callResponse.body(), artistName)
    }

    private fun initializeAPI() =
        Retrofit.Builder()
            .baseUrl(NY_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    private fun getRawArtistInfoFromExternal(artistName : String) : Response<String?> {
        return apiFromNYTimes.getArtistInfo(artistName)!!.execute()
    }
}
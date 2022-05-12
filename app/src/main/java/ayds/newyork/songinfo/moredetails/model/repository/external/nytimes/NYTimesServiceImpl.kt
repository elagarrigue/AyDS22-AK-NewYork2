package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import retrofit2.Response

class NYTimesServiceImpl (private val artistResolver : NYTimesToArtistResolver, private val apiFromNYTimes : NYTimesAPI): NYTimesService {

    override fun getArtist(artistName: String) : ArtistInfo? {
        val callResponse = getRawArtistInfoFromExternal(artistName)
        return artistResolver.getArtistFromExternalData(callResponse.body(), artistName)
    }

    private fun getRawArtistInfoFromExternal(artistName : String) : Response<String?> {
        return apiFromNYTimes.getArtistInfo(artistName)?.execute() ?: throw Exception("Response not found")
    }
}
package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.newyork.songinfo.moredetails.model.repository.external.NewYorkTimesProxy
import ayds.newyork2.newyorkdata.nytimes.NYTimesArtistInfo
import ayds.newyork2.newyorkdata.nytimes.NYTimesService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class NYTimesProxyTest {

    private val nyTimesService : NYTimesService = mockk(relaxUnitFun = true)

    private val nyTimesProxy : NewYorkTimesProxy by lazy{
        NewYorkTimesProxy(nyTimesService)
    }

    @Test
    fun `given existing artist by name should return a card`(){

        val card = ExternalCard(
            "name",
            "description",
            "urlInfo",
            Source.NEWYORKTIMES,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU",
            false
        )

        val artistInfo = NYTimesArtistInfo(
            "name",
            "description",
            "urlInfo"
        )
        every { nyTimesService.getArtist("name") } returns artistInfo

        val result = nyTimesProxy.getCard("name")

        verify { nyTimesService.getArtist("name") }
        Assert.assertEquals(card, result)
    }

    @Test
    fun `given non existing artist by name should return null`(){

        val card = null

        every { nyTimesService.getArtist("name") } returns null

        val result = nyTimesProxy.getCard("name")

        verify{ nyTimesService.getArtist("name") }
        Assert.assertEquals(card, result)
    }
}
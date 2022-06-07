package ayds.newyork.songinfo.moredetails.model.repository

import ayds.lisboa1.lastfm.LastFMArtistBiography
import ayds.lisboa1.lastfm.LastFMService
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.newyork.songinfo.moredetails.model.repository.external.LastFMProxy
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class LastFMProxyTest {

    private val lastFmService : LastFMService = mockk(relaxUnitFun = true)

    private val lastFmProxy : LastFMProxy by lazy{
        LastFMProxy(lastFmService)
    }

    @Test
    fun `given existing artist by name should return a card`(){

        val card = ExternalCard(
            "name",
            "description",
            "urlInfo",
            Source.LASTFM,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png",
            false
        )

        val artistBio = LastFMArtistBiography(
            "name",
            "description",
            "urlInfo"
        )
        every { lastFmService.getArtistBio("name") } returns artistBio

        val result = lastFmProxy.getCard("name")
        Assert.assertEquals(card, result)
    }

    @Test
    fun `given non existing artist by name should return null`(){

        val card = null

        every { lastFmService.getArtistBio("name") } returns null

        val result = lastFmProxy.getCard("name")
        Assert.assertEquals(card, result)
    }
}
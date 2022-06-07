package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.newyork.songinfo.moredetails.model.repository.external.WikipediaDataProxy
import ayds.winchester2.wikipedia.ExternalRepository
import ayds.winchester2.wikipedia.WikipediaArticle
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class WikipediaDataProxyTest {

    private val wikipediaService : ExternalRepository = mockk(relaxUnitFun = true)

    private val wikipediaDataProxy : WikipediaDataProxy by lazy{
        WikipediaDataProxy(wikipediaService)
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

        val artistArticle = WikipediaArticle(
            "name",
            "description",
            "urlInfo"
        )
        every { wikipediaService.getArtistDescription("name") } returns artistArticle

        val result = wikipediaDataProxy.getCard("name")
        Assert.assertEquals(card, result)
    }

    @Test
    fun `given non existing artist by name should return null`(){

        val card = null

        every { wikipediaService.getArtistDescription("name") }.throws(java.lang.IndexOutOfBoundsException())

        val result = wikipediaDataProxy.getCard("name")
        Assert.assertEquals(card, result)
    }
}
package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.newyork.songinfo.moredetails.model.repository.external.Broker
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.LocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CardRepositoryImplTest {

    private val localStorage: LocalStorage = mockk(relaxUnitFun = true)
    private val brokerService: Broker = mockk(relaxUnitFun = true)

    var list: MutableList<Card> = mutableListOf()

    private val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(localStorage, brokerService)
    }

    @Before
    fun init(){
        var card1 = ExternalCard(
            "name",
            "description",
            "urlInfo",
            Source.NEWYORKTIMES,
            "logo",
            false
        )

        var card2 = card1.copy(source = Source.LASTFM)
        var card3 = card1.copy(source = Source.WIKIPEDIA)

        list.add(card1)
        list.add(card2)
        list.add(card3)
    }

    @Test
    fun `given existing artist by name should return artist and mark it as local`() {
        every { localStorage.getCardsByName("name") } returns list

        val result = cardRepository.getCardsByName("name")

        Assert.assertEquals(list, result)
        list.forEach{
            Assert.assertTrue(it.isLocallyStored)
        }
    }

    @Test
    fun `given non existing artist by name should get the artist and store it`() {
        every { localStorage.getCardsByName("name") } returns emptyList()
        every { brokerService.getCards("name") } returns list

        val result = cardRepository.getCardsByName("name")

        Assert.assertEquals(list, result)
        list.forEach{
            Assert.assertFalse(it.isLocallyStored)
        }
        verify { localStorage.saveCards(list) }
    }

    @Test
    fun `given non existing artist by name should return empty artist`() {
        every { localStorage.getCardsByName("name") } returns emptyList()
        every { brokerService.getCards("name") } returns emptyList()

        val result = cardRepository.getCardsByName("name")
        val empty : List<Card> = mutableListOf()

        Assert.assertEquals(empty, result)
    }

    @Test
    fun `given service exception should return empty song`() {
        every { localStorage.getCardsByName("name") } returns emptyList()
        every { brokerService.getCards("name") } throws mockk<Exception>()

        val result = cardRepository.getCardsByName("name")
        val empty : List<Card> = mutableListOf()

        Assert.assertEquals(empty, result)
    }
}
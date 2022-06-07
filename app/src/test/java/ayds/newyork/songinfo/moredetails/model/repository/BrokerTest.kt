package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.repository.external.BrokerImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.Proxy
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class BrokerTest {
    private val proxy: Proxy = mockk()
    private val proxyList: List<Proxy> = arrayListOf(
        proxy,
        proxy,
        proxy
    )
    private val broker = BrokerImpl(proxyList)


    @Test
    fun `given an artistName it should call all proxies`() {
        val card: ExternalCard = mockk()
        val cardsInfo = arrayListOf(
            card,
            card,
            card
        )

        every { proxy.getCard("name") } returns card
        val cards = broker.getCards("name")
        assertEquals(cardsInfo, cards)
    }
}
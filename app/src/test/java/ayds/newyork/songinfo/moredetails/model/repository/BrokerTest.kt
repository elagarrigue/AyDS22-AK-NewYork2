package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.repository.external.BrokerImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.Proxy
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.text.Typography.times

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

        for(p in proxyList){
            verify { p.getCard("name") }
        }

        assertEquals(cardsInfo, cards)
    }
}
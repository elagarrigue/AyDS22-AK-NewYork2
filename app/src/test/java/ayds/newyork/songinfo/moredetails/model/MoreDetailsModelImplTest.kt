package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsModelImplTest {

    private val repository: CardRepository = mockk()

    private val moreDetailsModel: MoreDetailsModel by lazy {
        MoreDetailsModelImpl(repository)
    }

    @Test
    fun `on search song it should notify the result`() {
        val card: List<Card> = mockk()
        every { repository.getCardsByName("name") } returns card
        val artistTester: (List<Card>) -> Unit = mockk(relaxed = true)
        moreDetailsModel.artistObservable.subscribe {
            artistTester(it)
        }

        moreDetailsModel.searchCards("name")

        verify { artistTester(card) }
    }

}
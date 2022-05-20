package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsModelImplTest {

    private val repository: ArtistInfoRepository = mockk()

    private val moreDetailsModel: MoreDetailsModel by lazy {
        MoreDetailsModelImpl(repository)
    }

    @Test
    fun `on search song it should notify the result`() {
        val artist: Artist = mockk()
        every { repository.getArtistByName("name") } returns artist
        val artistTester: (Artist) -> Unit = mockk(relaxed = true)
        moreDetailsModel.artistObservable.subscribe {
            artistTester(it)
        }

        moreDetailsModel.searchArtist("name")

        verify { artistTester(artist) }
    }

}
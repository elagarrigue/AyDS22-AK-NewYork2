package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtist
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test


class ArtistInfoRepositoryImplTest {


    private val nyTimesLocalStorage: NYTimesLocalStorage = mockk(relaxUnitFun = true)
    private val nyTimesService: NYTimesService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistInfoRepository by lazy {
        ArtistInfoRepositoryImpl(nyTimesLocalStorage, nyTimesService)
    }

    @Test
    fun `given existing artist by name should return artist and mark it as local`() {
        val artist = ArtistInfo(
            "id",
            "name",
            "artistInfo",
            false
        )
        every { nyTimesLocalStorage.getArtistByName("name") } returns artist

        val result = artistRepository.getArtistByName("name")

        Assert.assertEquals(artist, result)
        Assert.assertTrue(artist.isLocallyStored)
    }

    @Test
    fun `given non existing artist by name should get the artist and store it`() {
        val artistInfo = ArtistInfo(
            "id",
            "name",
            "artistInfo",
            false
        )
        every { nyTimesLocalStorage.getArtistByName("name") } returns null
        every { nyTimesService.getArtist("name") } returns artistInfo


        val result = artistRepository.getArtistByName("name")

        Assert.assertEquals(artistInfo, result)
        Assert.assertFalse(artistInfo.isLocallyStored)
        verify { nyTimesLocalStorage.saveArtist(artistInfo) }
    }

    @Test
    fun `given non existing artist by name should return empty artist`() {
        every { nyTimesLocalStorage.getArtistByName("name") } returns null
        every { nyTimesService.getArtist("name") } returns null

        val result = artistRepository.getArtistByName("name")

        Assert.assertEquals(EmptyArtist, result)
    }

    @Test
    fun `given service exception should return empty song`() {
        every { nyTimesLocalStorage.getArtistByName("name") } returns null
        every { nyTimesService.getArtist("name") } throws mockk<Exception>()

        val result = artistRepository.getArtistByName("name")

        Assert.assertEquals(EmptyArtist, result)
    }
}
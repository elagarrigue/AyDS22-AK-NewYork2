package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import org.junit.Assert
import org.junit.Test

class ArtistInfoDescriptionHelperImplTest {

    private val artistInfoDescriptionHelperImplTest = ArtistInfoDescriptionHelperImpl()

    @Test
    fun `given a local artist whose article does not contain their name it should return the description`(){
        val artistInfo: Artist = ArtistInfo(
        "name",
        "[*] The Daily Caller is giving away one gun a week until Election Day.",
        "articleUrl",
        true
        )

        val result = artistInfoDescriptionHelperImplTest.getArtistInfoText(artistInfo)

        val expected =
            "<html><div width=400><font face=\"arial\">[*] The Daily Caller is giving away one gun a week until Election Day.</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a external artist whose article does not contain their name it should return the description`(){
        val artistInfo: Artist = ArtistInfo(
            "name",
            "The Daily Caller is giving away one gun a week until Election Day.",
            "articleUrl",
            false
        )

        val result = artistInfoDescriptionHelperImplTest.getArtistInfoText(artistInfo)

        val expected =
            "<html><div width=400><font face=\"arial\">[*] The Daily Caller is giving away one gun a week until Election Day.</font></div></html>"

        Assert.assertEquals(expected, result)
    }
}
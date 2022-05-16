package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtist
import org.junit.Assert
import org.junit.Test

class ArtistInfoDescriptionHelperImplTest {

    private val artistInfoDescriptionHelperImplTest = ArtistInfoDescriptionHelperImpl()

    @Test
    fun `given a local artist whose article does not contain their name it should return the description`() {
        val artistInfo: Artist = ArtistInfo(
        "name",
        "The Daily Caller is giving away one gun a week until Election Day.",
        "articleUrl",
        true
        )

        val result = artistInfoDescriptionHelperImplTest.getArtistInfoText(artistInfo)

        val expected =
            "<html><div width=400><font face=\"arial\">[*] The Daily Caller is giving away one gun a week until Election Day.</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a external artist whose article does not contain their name it should return the description`() {
        val artistInfo: Artist = ArtistInfo(
            "name",
            "The Daily Caller is giving away one gun a week until Election Day.",
            "articleUrl",
            false
        )

        val result = artistInfoDescriptionHelperImplTest.getArtistInfoText(artistInfo)

        val expected =
            "<html><div width=400><font face=\"arial\"> The Daily Caller is giving away one gun a week until Election Day.</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a external artist whose article does contain their name it should return the description with bolded name`() {
        val artistInfo: Artist = ArtistInfo(
            "Duki",
            "Ned Martel reviews documentary movie Raging Dove, directed by Duki Dror (S)",
            "articleUrl",
            false
        )

        val result = artistInfoDescriptionHelperImplTest.getArtistInfoText(artistInfo)

        val expected =
        "<html><div width=400><font face=\"arial\"> Ned Martel reviews documentary movie Raging Dove, directed by <b>DUKI</b> Dror (S)</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a local artist whose article does contain their name it should return the description with bolded name`() {
        val artistInfo: Artist = ArtistInfo(
            "Duki",
            "Ned Martel reviews documentary movie Raging Dove, directed by Duki Dror (S)",
            "articleUrl",
            true
        )

        val result = artistInfoDescriptionHelperImplTest.getArtistInfoText(artistInfo)

        val expected =
            "<html><div width=400><font face=\"arial\">[*] Ned Martel reviews documentary movie Raging Dove, directed by <b>DUKI</b> Dror (S)</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a non artist info it should return the artist not found description`() {
        val artistInfo: Artist = EmptyArtist

        val result = artistInfoDescriptionHelperImplTest.getArtistInfoText(artistInfo)

        val expected = "<html><div width=400><font face=\"arial\"><b></b>A<b></b>r<b></b>t<b></b>i<b></b>s<b></b>t<b></b> <b></b>n<b></b>o<b></b>t<b></b> <b></b>f<b></b>o<b></b>u<b></b>n<b></b>d<b></b></font></div></html>"

        Assert.assertEquals(expected, result)
    }
}
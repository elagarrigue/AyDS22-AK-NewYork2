package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.*
import org.junit.Assert
import org.junit.Test

class ArtistInfoDescriptionHelperImplTest {

    private val artistInfoDescriptionHelperImplTest = ArtistInfoDescriptionHelperImpl()

    @Test
    fun `given a local artist whose article does not contain their name it should return the description`() {
        val artistInfo: Card = ExternalCard(
            "name",
            "The Daily Caller is giving away one gun a week until Election Day.",
            "articleUrl",
            Source.NEWYORKTIMES,
            "",
            true
        )

        val result = artistInfoDescriptionHelperImplTest.getCardText(artistInfo)

        val expected =
            "<html><div width=400><font face=\"arial\">[*] The Daily Caller is giving away one gun a week until Election Day.</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a external artist whose article does not contain their name it should return the description`() {
        val artistInfo: Card = ExternalCard(
            "name",
            "The Daily Caller is giving away one gun a week until Election Day.",
            "articleUrl",
            Source.NEWYORKTIMES,
            "",
            false
        )

        val result = artistInfoDescriptionHelperImplTest.getCardText(artistInfo)

        val expected =
            "<html><div width=400><font face=\"arial\"> The Daily Caller is giving away one gun a week until Election Day.</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a artist whose article does contain their name it should return the description with bolded name`() {
        val artistInfo: Card = ExternalCard(
            "Duki",
            "Ned Martel reviews documentary movie Raging Dove, directed by Duki Dror (S)",
            "articleUrl",
            Source.NEWYORKTIMES,
            "",
            false
        )

        val result = artistInfoDescriptionHelperImplTest.getCardText(artistInfo)

        val expected =
        "<html><div width=400><font face=\"arial\"> Ned Martel reviews documentary movie Raging Dove, directed by <b>DUKI</b> Dror (S)</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a non artist info it should return the artist not found description`() {
        val card: Card = EmptyCard

        val result = artistInfoDescriptionHelperImplTest.getCardText(card)

        val expected = "<html><div width=400><font face=\"arial\">Artist not found</font></div></html>"

        Assert.assertEquals(expected, result)
    }
}
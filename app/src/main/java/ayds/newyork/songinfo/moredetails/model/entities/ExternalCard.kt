package ayds.newyork.songinfo.moredetails.model.entities

interface Card {
    val artistName: String
    val description: String
    val infoUrl: String
    val source: Source
    val sourceLogoUrl: String
    var isLocallyStored: Boolean
}

data class ExternalCard(
    override val artistName: String,
    override val description: String,
    override val infoUrl: String,
    override val source: Source,
    override val sourceLogoUrl: String,
    override var isLocallyStored: Boolean = false
) : Card

object EmptyCard : Card {
    override val artistName: String = ""
    override val description: String = ""
    override val infoUrl: String = ""
    override val source: Source = Source.NEWYORKTIMES
    override val sourceLogoUrl: String = ""
    override var isLocallyStored: Boolean = false
}
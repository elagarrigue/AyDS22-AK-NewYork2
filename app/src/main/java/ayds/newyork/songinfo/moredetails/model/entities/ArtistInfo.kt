package ayds.newyork.songinfo.moredetails.model.entities

interface Artist{
    val id : String
    val artistName: String
    val artistInfo: String
    var isLocallyStored: Boolean
}

data class ArtistInfo(
    override val id : String,
    override val artistName: String,
    override val artistInfo: String,
    override var isLocallyStored: Boolean = false
) : Artist

object EmptyArtist : Artist {
    override val id : String = ""
    override val artistName: String = ""
    override val artistInfo: String = ""
    override var isLocallyStored: Boolean = false
}
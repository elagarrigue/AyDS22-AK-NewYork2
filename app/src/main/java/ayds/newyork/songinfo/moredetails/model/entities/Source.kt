package ayds.newyork.songinfo.moredetails.model.entities

private const val TEXTO_LASTFM: String = "LASTFM"
private const val TEXTO_WIKIPIEDIA = "WIKIPIEDIA"
private const val TEXTO_NEWYORKTIMES = "NEWYORKTIMES"

enum class Source(value : String) {
    LASTFM(TEXTO_LASTFM),
    WIKIPEDIA(TEXTO_WIKIPIEDIA),
    NEWYORKTIMES(TEXTO_NEWYORKTIMES);

    private val text = value

    fun getText() : String {
        return text
    }
}
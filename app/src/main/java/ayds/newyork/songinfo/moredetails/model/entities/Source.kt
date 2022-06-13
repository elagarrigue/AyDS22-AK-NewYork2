package ayds.newyork.songinfo.moredetails.model.entities

private const val TEXTO_LASTFM: String = "LASTFM"
private const val TEXTO_WIKIPIEDIA = "WIKIPIEDIA"
private const val TEXTO_NEWYORKTIMES = "NEWYORKTIMES"

enum class Source(val value : String) {
    LASTFM(TEXTO_LASTFM),
    WIKIPEDIA(TEXTO_WIKIPIEDIA),
    NEWYORKTIMES(TEXTO_NEWYORKTIMES);
}
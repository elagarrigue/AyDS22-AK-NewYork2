package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb

const val ARTIST_INFO_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val URL_COLUMN = "url"
const val SOURCE_COLUMN = "source"
const val SOURCE_LOGO_COLUMN = "source_logo"

const val createArtistInfoTableQuery: String =
    "create table $ARTIST_INFO_TABLE (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, "+
            "$URL_COLUMN string, "+
            "$SOURCE_COLUMN integer, "+
            "$SOURCE_LOGO_COLUMN string)"

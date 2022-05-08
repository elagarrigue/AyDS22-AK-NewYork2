package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb

const val ARTIST_INFO_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"

const val createArtistInfoTableQuery: String =
    "create table $ARTIST_INFO_TABLE (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, "+
            "$SOURCE_COLUMN string)"

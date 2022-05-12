package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface CursorToArtistInfoMapper {

    fun map(cursor : Cursor) : ArtistInfo?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {

    override fun map(cursor : Cursor) : ArtistInfo? {
        var artistInfo : ArtistInfo? = null
        if(cursor.moveToNext()) {
            artistInfo = ArtistInfo(
                artistName = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN)),
                artistInfo = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN)),
                artistUrl = cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_COLUMN))
            )
        }
        cursor.close()
        return artistInfo
    }
}
package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard

interface CursorToArtistInfoMapper {

    fun map(cursor : Cursor) : ExternalCard?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {

    override fun map(cursor : Cursor) : ExternalCard? {
        var artistInfo : ExternalCard? = null
        if(cursor.moveToNext()) {
            artistInfo = ExternalCard(
                artistName = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN)),
                infoUrl = cursor.getString(cursor.getColumnIndexOrThrow(URL_COLUMN)),
                source = cursor.getInt(cursor.getColumnIndexOrThrow(SOURCE_COLUMN)),
                sourceLogoUrl = cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_LOGO_COLUMN))
            )
        }
        cursor.close()
        return artistInfo
    }
}
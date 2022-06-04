package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source

interface CursorToCardMapper {

    fun map(cursor : Cursor) : List<ExternalCard>
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor : Cursor) : List<ExternalCard> {
        var cardInfo : ExternalCard? = null
        var cardsList : MutableList<ExternalCard> = mutableListOf()
        while(cursor.moveToNext()) {
            val ordinalSource = cursor.getInt(cursor.getColumnIndexOrThrow(SOURCE_COLUMN))
            cardInfo = ExternalCard(
                artistName = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN)),
                infoUrl = cursor.getString(cursor.getColumnIndexOrThrow(URL_COLUMN)),
                source = Source.values()[ordinalSource],
                sourceLogoUrl = cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_LOGO_COLUMN))
            )
            cardsList.add(cardInfo)
        }
        cursor.close()
        return cardsList
    }
}
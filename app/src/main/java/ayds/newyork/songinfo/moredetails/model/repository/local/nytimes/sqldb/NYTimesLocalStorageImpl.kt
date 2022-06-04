package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.NYTimesLocalStorage

private const val nameDataBase : String = "dictionary.db"

internal class NYTimesLocalStorageImpl(context: Context?) : SQLiteOpenHelper(context, nameDataBase, null, 1),
    NYTimesLocalStorage {

    private val mapper : CursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()

    override fun onCreate(dataBase: SQLiteDatabase){
        dataBase.execSQL(createArtistInfoTableQuery)
    }

    override fun onUpgrade(dataBase: SQLiteDatabase, oldVersion: Int, newVersion: Int){}

    private fun createArtistNameCursor(artist: String) : Cursor{
        val db = this.readableDatabase
        val projection = getInfoProjection()
        val selection = getInfoSelection()
        val selectionArgs = arrayOf(artist)
        val sortOrder = getInfoSort()
        return db.query(
            ARTIST_INFO_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    private fun getInfoProjection() : Array<String> = arrayOf(ID_COLUMN, ARTIST_COLUMN, INFO_COLUMN,URL_COLUMN, SOURCE_COLUMN,SOURCE_LOGO_COLUMN)

    private fun getInfoSelection() : String = "$ARTIST_COLUMN = ?"

    private fun getInfoSort() : String = "$ARTIST_COLUMN DESC"

    override fun getArtistByName(name: String): List<ExternalCard> {
        val cursor = createArtistNameCursor(name)
        return mapper.map(cursor)
    }

    override fun saveArtist(artist: List<Card>) {
        artist.forEach() {
            val values = ContentValues().apply {
                put(ARTIST_COLUMN, it.artistName)
                put(INFO_COLUMN, it.description)
                put(URL_COLUMN, it.infoUrl)
                put(SOURCE_COLUMN, it.source.ordinal)
                put(SOURCE_LOGO_COLUMN, it.sourceLogoUrl)
            }
            writableDatabase?.insert(ARTIST_INFO_TABLE, null, values)
        }
    }
}
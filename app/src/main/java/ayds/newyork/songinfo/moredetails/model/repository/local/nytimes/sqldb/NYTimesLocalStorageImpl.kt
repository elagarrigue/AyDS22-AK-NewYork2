package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
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

    private fun createIdCursor(id: String) : Cursor{
        val db = this.readableDatabase
        val projection = getInfoProjection()
        val selection = getIdSelection()
        val selectionArgs = arrayOf(id)
        return db.query(
            ARTIST_INFO_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
    }

    private fun getInfoProjection() : Array<String> = arrayOf(ID_COLUMN, ARTIST_COLUMN, INFO_COLUMN)

    private fun getInfoSelection() : String = "$ARTIST_COLUMN = ?"

    private fun getIdSelection() : String = "$ID_COLUMN = ?"

    private fun getInfoSort() : String = "$ARTIST_COLUMN DESC"

    override fun getArtistByName(name: String): ArtistInfo? {
        val cursor = createArtistNameCursor(name)
        return mapper.map(cursor)
    }

    override fun getArtistById(id: String): ArtistInfo? {
        val cursor = createIdCursor(id)
        return mapper.map(cursor)
    }

    override fun saveArtist(artist: ArtistInfo) {
        val values = ContentValues().apply {
            put(ARTIST_COLUMN, artist.artistName)
            put(INFO_COLUMN, artist.artistInfo)
            put(SOURCE_COLUMN, artist.artistUrl)
        }

        writableDatabase?.insert(ARTIST_INFO_TABLE, null, values)
    }
}
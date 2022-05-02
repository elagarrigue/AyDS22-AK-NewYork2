package ayds.newyork.songinfo.moredetails.model.repository.local.nytimes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val nameDataBase : String = "dictionary.db"

class DataBase(context: Context?) : SQLiteOpenHelper(context, nameDataBase, null, 1){

    private val artistColumn : String = "artist"
    private val infoColumn : String = "info"
    private val sourceColumn : String = "source"
    private val idColumn : String = "id"
    private val tableArtists : String = "artists"

    override fun onCreate(dataBase: SQLiteDatabase){
        dataBase.execSQL("create table $tableArtists ($idColumn INTEGER PRIMARY KEY AUTOINCREMENT, $artistColumn string, $infoColumn string, $sourceColumn integer)")
    }

    override fun onUpgrade(dataBase: SQLiteDatabase, oldVersion: Int, newVersion: Int){}

    fun saveArtist(artist: String?, info: String?, url: String?){
        val db = this.writableDatabase
        val values = createMapValues(artist, info, url)
        db.insert(tableArtists, null, values)
    }

    private fun createMapValues(artist : String?, info : String?, url: String?): ContentValues{
        val values = ContentValues()
        values.put(artistColumn, artist)
        values.put(infoColumn, info)
        values.put(sourceColumn, url)
        return values
    }

    fun getInfo(artist: String): String? {
        val cursor = createCursor(artist)
        return getArtistFromCursor(cursor)
    }

    private fun createCursor(artist: String) : Cursor{
        val db = this.readableDatabase
        val projection = getInfoProjection()
        val selection = getInfoSelection()
        val selectionArgs = arrayOf(artist)
        val sortOrder = getInfoSort()
        return db.query(
            tableArtists,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    private fun getInfoProjection() : Array<String> = arrayOf(idColumn, artistColumn, infoColumn)

    private fun getInfoSelection() : String = "$artistColumn = ?"

    private fun getInfoSort() : String = "$artistColumn DESC"

    private fun getArtistFromCursor(cursor : Cursor) : String?{
        var info : String? = null
        if(cursor.moveToNext()){
            info = cursor.getString(
                cursor.getColumnIndexOrThrow(infoColumn)
            )
        }
        cursor.close()
        return info
    }
}
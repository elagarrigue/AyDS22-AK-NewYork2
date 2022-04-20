package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1){

    override fun onCreate(dataBase: SQLiteDatabase){
        val createQuery : String =  "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        dataBase.execSQL(createQuery)
    }

    override fun onUpgrade(dataBase: SQLiteDatabase, oldVersion: Int, newVersion: Int){}

    companion object {
        private const val artistColumn : String = "artist"
        private const val infoColumn : String = "info"
        private const val sourceColumn : String = "source"
        private const val idColumn : String = "id"
        private const val tableArtists : String = "artists"

        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?){
            val db = dbHelper.writableDatabase
            val values = createMapValues(artist, info)
            val newRowId = db.insert("$tableArtists", null, values)
        }

        private fun createMapValues(artist : String?, info : String?): ContentValues{
            val values = ContentValues()
            values.put(artistColumn, artist)
            values.put(infoColumn, info)
            values.put(sourceColumn, 1)
            return values
        }

        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = dbHelper.readableDatabase
            val cursor = createCursor(db,artist)
            val items: MutableList<String> = getListInfo(cursor)
            closeCursor(cursor)
            return returnFirst(items)
        }

        private fun createCursor(dataBase : SQLiteDatabase, artist: String) : Cursor{
            val projection = getInfoProjection()
            val selection = getInfoSelection()
            val selectionArgs = arrayOf(artist)
            val sortOrder = getInfoSort()
            return dataBase.query(
                "$tableArtists",
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

        private fun getListInfo(cursor : Cursor) : MutableList<String>{
            val items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow(infoColumn)
                )
                items.add(info)
            }
            return items
        }

        private fun closeCursor(cursor : Cursor){ cursor.close() }

        private fun returnFirst(list : MutableList<String>) : String? {
            val toReturn : String? =
            if(list.isEmpty()){
                null
            }else{
                list[0]
            }
            return  toReturn
        }
    }
}
package ayds.newyork.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import ayds.newyork.songinfo.moredetails.fulllogic.DataBase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1){

    override fun onCreate(dataBase: SQLiteDatabase){
        val createQuery : String =  "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        dataBase.execSQL(createQuery)
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(dataBase: SQLiteDatabase, oldVersion: Int, newVersion: Int){}

    companion object {
        private const val artistColumn : String = "artist"
        private const val infoColumn : String = "info"
        private const val sourceColumn : String = "source"
        private const val idColumn : String = "id"
        private const val tableArtists : String = "artists"

        fun testDB(){
            var dataBaseConnection: Connection? = null
            val dataBaseUrl : String = "jdbc:sqlite:./dictionary.db"
            val sqlQuery : String = "select * from $tableArtists"
            try {
                dataBaseConnection = createConnectionDataBase(dataBaseUrl)
                val statement = createStatementDataBase(dataBaseConnection)
                val rs = statement.executeQuery(sqlQuery)
                while (rs.next()) {
                    println("$idColumn = " + rs.getInt(idColumn))
                    println("$artistColumn = " + rs.getString(artistColumn))
                    println("$infoColumn = " + rs.getString(infoColumn))
                    println("$sourceColumn = " + rs.getString(sourceColumn))
                }
            } catch (exception : SQLException) {
                System.err.println(exception.message)
            } finally {
                closeConnectionDataBase(dataBaseConnection)
            }
        }

            private fun createConnectionDataBase(dataBaseUrl : String) : Connection{
                var dataBaseConnection: Connection? = null
                dataBaseConnection = DriverManager.getConnection(dataBaseUrl)
                return dataBaseConnection
            }

            private fun createStatementDataBase(dataBaseConnection : Connection) : Statement{
                val statementTimeOut : Int = 30
                val statement = dataBaseConnection.createStatement()
                statement.queryTimeout = statementTimeOut
                return statement
            }

            private fun closeConnectionDataBase(dataBaseConnection : Connection?){
                try {
                    dataBaseConnection?.close()
                } catch (closeFailException: SQLException) {
                    System.err.println(closeFailException)
                }
            }

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

        private fun getInfoProjection() : Array<String> = arrayOf(idColumn, artistColumn, infoColumn)

        private fun getInfoSelection() : String = "$artistColumn = ?"

        private fun getInfoSort() : String = "$artistColumn DESC"

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

        private fun returnFirst(list : MutableList<String>) : String? {
            var toReturn : String?
            if(list.isEmpty()){
                toReturn = null
            }else{
                toReturn = list[0]
            }
            return  toReturn
        }

        private fun closeCursor(cursor : Cursor){ cursor.close() }

    }
}
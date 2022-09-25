package com.example.flights

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

//const val DATABASE_VERSION:Int=1
//const val DATABASE_NAME:String="DB1"
//const val DATABASE_TABLE:String="Users"

object DB:BaseColumns{
    const val TABLE_NAME = "Users"
    const val COLUMN_NAME_ID = "id"
    const val COLUMN_NAME_SURNAME = "Surname"
    const val COLUMN_NAME_NAME = "Name"
    const val COLUMN_NAME_PATRONYMIC = "Patronymic"
    const val COLUMN_NAME_LOGIN = "Login"
    const val COLUMN_NAME_GENDER = "Gender"
    const val COLUMN_NAME_NUMBER = "Number"
    const val COLUMN_NAME_PASSWORD = "Password"
    const val COLUMN_NAME_ADMIN = "Admin"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "DB1"

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${DB.TABLE_NAME} (" +
//                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${DB.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
                "${DB.COLUMN_NAME_SURNAME} TEXT," +
                "${DB.COLUMN_NAME_NAME} TEXT," +
                "${DB.COLUMN_NAME_PATRONYMIC} TEXT," +
                "${DB.COLUMN_NAME_LOGIN} TEXT," +
                "${DB.COLUMN_NAME_GENDER} TEXT," +
                "${DB.COLUMN_NAME_NUMBER} TEXT," +
                "${DB.COLUMN_NAME_PASSWORD} TEXT," +
                "${DB.COLUMN_NAME_ADMIN} INTEGER)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${DB.TABLE_NAME}"
}

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB.DATABASE_NAME, null, DB.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DB.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersionn: Int, newVersion: Int) {
        db.execSQL(DB.SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}
class UpdateDB(context: Context){
    private val dbHelper = DBHelper(context)

    fun insertDB(id: Int, Surname: String, Name: String, Patronymic:String, Login: String, Gender:String, Number:String, Password: String, Admin: Int): Long? {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DB.COLUMN_NAME_ID, id)
            put(DB.COLUMN_NAME_SURNAME, Surname)
            put(DB.COLUMN_NAME_NAME, Name)
            put(DB.COLUMN_NAME_PATRONYMIC, Patronymic)
            put(DB.COLUMN_NAME_LOGIN, Login)
            put(DB.COLUMN_NAME_GENDER, Gender)
            put(DB.COLUMN_NAME_NUMBER, Number)
            put(DB.COLUMN_NAME_PASSWORD, Password)
            put(DB.COLUMN_NAME_ADMIN, Admin)
        }

        val newRowId = db.insert(DB.TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun readDB(Columns:Array<String>?, Selection:String?, SelectionArgs: Array<String>?, SortColumn:String, Sort: Int): MutableList<MutableList<String>> {
        val db = dbHelper.readableDatabase

        val projection:Array<String>? = Columns

// Filter results WHERE "title" = 'My Title'
        val selection = if(Selection!=null) "$Selection = ?" else null

// How you want the results sorted in the resulting Cursor
        val sort:Array<String> = arrayOf("DESC","ASC")
        val sortOrder =  "$SortColumn ${sort[Sort]}"
//        val sortOrder = "${DB.COLUMN_NAME_SUBTITLE} DESC"

        val cursor = db.query(
            DB.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            SelectionArgs,  // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        val itemIds:MutableList<MutableList<String>> = mutableListOf()
        var itemId:MutableList<String> = mutableListOf()
        with(cursor) {
            while (moveToNext()) {
                for(i in 0 until columnCount) {
//                    val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID)).toString()

                    itemId.add(getString(i))
                }
                itemIds.add(itemId)
                itemId=mutableListOf()
            }
        }
        cursor.close()
        db.close()
        return itemIds
    }
    fun deleteDB(Selection:String?,SelectionArgs:Array<String>?): Int? {
        val db = dbHelper.writableDatabase
        val selection = if(Selection!=null)"$Selection LIKE ?" else null

        val deletedRows = db.delete(DB.TABLE_NAME, selection, SelectionArgs)
        db.close()
        return deletedRows
    }
    fun updateDB(Column: String?, ColumnArgs: String?, Selection:String?, SelectionArgs:Array<String>){
        val db = dbHelper.writableDatabase

// New value for one column
//        val title = "MyNewTitle"
        val values = if(Column!=null)ContentValues().apply {
            put(Column, ColumnArgs)
        } else null

// Which row to update, based on the title
        val selection = "$Selection LIKE ?"
        val count = db.update(
            DB.TABLE_NAME,
            values,
            selection,
            SelectionArgs)
        db.close()
    }

}



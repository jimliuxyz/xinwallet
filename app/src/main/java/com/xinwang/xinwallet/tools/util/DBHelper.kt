package com.xinwang.xinwallet.tools.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.xinwang.xinwallet.jsonrpc.JSONRPC

class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {


    val VERSION = 1
    val DATABABSE_CREATE_TABLE = "create table currency(" +
            "_ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," +
            "name VARCHAR(5) NOT NULL," +
            "isDefault INTEGER NOT NULL," +
            "isVisible INTEGER)"

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(DATABABSE_CREATE_TABLE)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put("name", "userid")
        values.put("isDefault", 1)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert("currency", null, values)
        JSONRPC().showToast(newRowId.toString())
        return true
    }

    fun readUsers() {
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from currency where isDefault ==9", null)
            if (cursor!!.moveToFirst()){
              var ss= cursor.getString(cursor.getColumnIndex("name"))
                println("antte$ss")
            }
        } catch (e: SQLiteException) {
           // db.execSQL(SQL_CREATE_ENTRIES)
           // return ArrayList()
        }


    }
}
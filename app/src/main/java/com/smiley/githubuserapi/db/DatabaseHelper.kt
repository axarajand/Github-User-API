package com.smiley.githubuserapi.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.smiley.githubuserapi.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.smiley.githubuserapi.db.DatabaseContract.*

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbuser"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${UserColumns.COLUMN_USERNAME} TEXT NOT NULL," +
                " ${UserColumns.COLUMN_AVATAR_URL} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}
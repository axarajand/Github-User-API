package com.smiley.githubuserapi.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.smiley.githubuserapi.db.DatabaseContract.UserColumns.Companion.COLUMN_USERNAME
import com.smiley.githubuserapi.db.DatabaseContract.UserColumns.Companion.TABLE_NAME

class UserHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: UserHelper? = null

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_USERNAME ASC")
    }

    fun queryByUsername(username: String?): Cursor {
        return database.query(DATABASE_TABLE, null, "$COLUMN_USERNAME = ?", arrayOf(username), null, null, null, null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username: String?): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_USERNAME = '$username'", null)
    }

    fun update(username: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$COLUMN_USERNAME = ?", arrayOf(username))
    }

}
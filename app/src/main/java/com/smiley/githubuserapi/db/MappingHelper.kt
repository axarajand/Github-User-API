package com.smiley.githubuserapi.db

import android.database.Cursor
import com.smiley.githubuserapi.UserItem

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<UserItem> {
        val userList = ArrayList<UserItem>()
        userCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_AVATAR_URL))
                userList.add(UserItem(username, avatar))
            }
        }
        return userList
    }

//    fun mapCursorToObject(userCursor: Cursor?): UserItem {
//        var user = UserItem()
//        userCursor?.apply {
//            moveToFirst()
//            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_USERNAME))
//            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_AVATAR_URL))
//            user = UserItem(username, avatar)
//        }
//        return user
//    }
}
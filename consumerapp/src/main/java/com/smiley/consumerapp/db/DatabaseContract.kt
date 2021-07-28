package com.smiley.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    companion object {
        const val AUTHORITY = "com.smiley.githubuserapi.user"
        const val SCHEME = "content"
    }

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_users"
            const val _ID = "_id"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_AVATAR_URL = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

}
package com.smiley.githubuserapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel: ViewModel() {

    val listUser = MutableLiveData<ArrayList<UserItem>>()

    fun setUser(username: String, unknownSearch: String) {
        val listItems = ArrayList<UserItem>()
        val url = "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token f71fd2f66e7f118adb9a4116c8137791764639e9")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val cekRespone = responseObject.getInt("total_count")
                    if (cekRespone == 0) {
                        val userItem = UserItem()
                        userItem.username = unknownSearch
                        listItems.add(userItem)
                    }else {
                        val list = responseObject.getJSONArray("items")
                        for (i in 0 until list.length()) {
                            val user = list.getJSONObject(i)
                            val userItem = UserItem()
                            val username = user.getString("login")
                            val avatar = user.getString("avatar_url")

                            userItem.username = username
                            userItem.avatar = avatar
                            listItems.add(userItem)
                        }
                    }
                    //set data ke adapter
                    listUser.postValue(listItems)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<UserItem>> {
        return listUser
    }

}
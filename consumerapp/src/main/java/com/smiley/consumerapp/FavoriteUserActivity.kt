package com.smiley.consumerapp

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smiley.consumerapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.smiley.consumerapp.db.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)
        title = getString(R.string.title_favorite_user)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                val moveIntent = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                moveIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                moveIntent.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatar)
                startActivity(moveIntent)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        rv_favorite_user.layoutManager = LinearLayoutManager(this)
        rv_favorite_user.adapter = adapter

        loadUserAsync()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val user = deferredNotes.await()
            showLoading(false)
            if (user.size > 0) {
                adapter.setData(user)
            } else {
                adapter.setData(ArrayList())
                showSnackbarMessage(getString(R.string.no_data))
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite_user, message, Snackbar.LENGTH_SHORT).show()
    }

}
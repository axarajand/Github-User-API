package com.smiley.githubuserapi

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.smiley.githubuserapi.db.DatabaseContract
import com.smiley.githubuserapi.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.smiley.githubuserapi.db.MappingHelper
import com.smiley.githubuserapi.db.UserHelper
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_user.*
import org.json.JSONObject

class DetailUserActivity : AppCompatActivity() {

    private val imgSize = 100
    private lateinit var userHelper: UserHelper
    private lateinit var uriWithUsername: Uri

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        supportActionBar?.elevation = 0f
        title = getString(R.string.title_detail)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)

        uriWithUsername = Uri.parse("$CONTENT_URI/$username")

        var statusFavorite: Boolean
        val cursor = contentResolver?.query(uriWithUsername, null, null, null, null)
        val checkStatus = MappingHelper.mapCursorToArrayList(cursor)
        statusFavorite = checkStatus.size > 0
        setStatusFavorite(statusFavorite)
        fab.setOnClickListener {
            statusFavorite = !statusFavorite

            if(statusFavorite) {
                val values = ContentValues()
                values.put(DatabaseContract.UserColumns.COLUMN_USERNAME, username)
                values.put(DatabaseContract.UserColumns.COLUMN_AVATAR_URL, avatar)
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.success_insert, username), Toast.LENGTH_SHORT).show()
                setStatusFavorite(statusFavorite)
            }else{
                contentResolver.delete(uriWithUsername, null, null)
                Toast.makeText(this, getString(R.string.success_delete, username), Toast.LENGTH_SHORT).show()
                setStatusFavorite(statusFavorite)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        setDetailUser(username)
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun setDetailUser(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token f71fd2f66e7f118adb9a4116c8137791764639e9")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val list = JSONObject(result)
                    val tvusername = list.getString("login")
                    val avatar = list.getString("avatar_url")
                    var name = list.getString("name")
                    var location = list.getString("location")
                    var company = list.getString("company")
                    var repository = list.getString("public_repos")

                    if (name == "null") {
                        name = getString(R.string.default_name)
                    }
                    if (location == "null") {
                        location = getString(R.string.default_location)
                    }
                    if (company == "null") {
                        company = getString(R.string.default_company)
                    }

                    tv_name.text = name
                    tv_username.text = tvusername
                    tv_location.text = location
                    tv_company.text = company
                    tv_repository.text = getString(R.string.repository, repository)
                    Glide.with(this@DetailUserActivity)
                        .load(avatar)
                        .apply(RequestOptions().override(imgSize))
                        .into(img_avatar)

                    showLoading(false)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    private fun setStatusFavorite(status: Boolean) {
        if(status == false) {
            fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }else{
            fab.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }

}
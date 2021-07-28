package com.smiley.consumerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class  MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val mIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(mIntent)
            }
            R.id.favorite_user -> {
                val mIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.title_search)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                val moveIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                moveIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                moveIntent.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatar)
                startActivity(moveIntent)
            }
        })

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        btn_search.setOnClickListener {
            val username = edit_user.text.toString()
            if (username.isEmpty()) return@setOnClickListener
            showLoading(true)

            val unknownSearch = getString(R.string.unknown_search)
            mainViewModel.setUser(username, unknownSearch)
        }

        mainViewModel.getUser().observe(this, Observer { userItem ->
            if (userItem != null) {
                adapter.setData(userItem)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
package com.smiley.githubuserapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray

class FollowingFragment : Fragment() {

    private lateinit var adapter: UserAdapter

    companion object {
        private val ARG_USERNAME = "arg_username"

        fun newInsteance(username: String?): FollowingFragment{
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                val moveIntent = Intent(activity, DetailUserActivity::class.java)
                moveIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                startActivity(moveIntent)
            }
        })

        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.adapter = adapter

        val username = arguments?.getString(ARG_USERNAME)
        setFollowing(username)
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun setFollowing(username: String?) {
        val listItems = ArrayList<UserItem>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "token f71fd2f66e7f118adb9a4116c8137791764639e9")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val jsonArray  = JSONArray(result)
                    if (jsonArray.length() == 0) {
                        val userItem = UserItem()
                        userItem.username = getString(R.string.unknown_following)
                        listItems.add(userItem)
                    }else{
                        for (i in 0 until jsonArray.length()) {
                            val user = jsonArray.getJSONObject(i)
                            val userItem = UserItem()
                            val username = user.getString("login")
                            val avatar = user.getString("avatar_url")

                            userItem.username = username
                            userItem.avatar = avatar
                            listItems.add(userItem)
                        }
                    }
//                    set data ke adapter
                    adapter.setData(listItems)
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

}
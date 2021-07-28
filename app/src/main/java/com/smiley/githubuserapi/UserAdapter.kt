package com.smiley.githubuserapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var mData = ArrayList<UserItem>()
    private val imgSize = 65
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserItem>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(UserViewHolder: UserViewHolder, position: Int) {
        UserViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItem: UserItem) {
            with(itemView){
                tv_username.text = userItem.username
                Glide.with(itemView.context)
                    .load(userItem.avatar)
                    .apply(RequestOptions().override(imgSize))
                    .into(img_avatar)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(userItem) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserItem)
    }

}
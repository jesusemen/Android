package com.dodemy.roomapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.layout_user_list.view.*

class UserListAdapter : RecyclerView.Adapter<MyViewHolder>() {

    private var userList = mutableListOf<Users>()

    private var clickListener: ListClickListener<Users>? = null

    fun setUsers(users: List<Users>) {
        this.userList = users.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_user_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val user = userList[position]
        holder.location.text = user.location
        holder.username.text = user.userName
        holder.email.text = user.email
        holder.layout.setOnClickListener {
            clickListener?.onClick(user,position)
        }

        holder.imgDelete.setOnClickListener {
            clickListener?.onDelete(user)
        }

    }

    fun setOnItemClick(listClickListener: ListClickListener<Users>) {
        this.clickListener = listClickListener
    }

}

class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val username: TextView = view.text_username
    val location: TextView = view.text_location
    val email: TextView = view.text_email
    val layout: ConstraintLayout = view.layout
    val imgDelete: ImageView = view.imgDelete
}


interface ListClickListener<T> {
    fun onClick(data: T, position: Int)
    fun onDelete(user: T)
}

package com.edpub.chatapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersListAdapter(private var usersList:List<User>) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    private var mListener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    class ViewHolder(view: View, listener: OnItemClickListener?):RecyclerView.ViewHolder(view){
        var tvUserName: TextView = view.findViewById(R.id.tvUserName)
        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = usersList[position]
        holder.tvUserName.text = user.NAME.toString()
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
}
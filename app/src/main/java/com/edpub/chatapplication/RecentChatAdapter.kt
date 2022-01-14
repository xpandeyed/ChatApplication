package com.edpub.chatapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecentChatAdapter(private var recentChats: MutableList<RecentChat>) : RecyclerView.Adapter<RecentChatAdapter.ViewHolder>() {
    private var mListener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    class ViewHolder(view: View, listener: OnItemClickListener?):RecyclerView.ViewHolder(view){
        var tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        var tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_chat, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = recentChats[position]
        holder.tvUserName.text = chat.NAME
        holder.tvMessage.text = chat.MESSAGE
    }

    override fun getItemCount(): Int {
        return recentChats.size
    }
}
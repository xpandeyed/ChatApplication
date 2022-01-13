package com.edpub.chatapplication

import android.os.TestLooperManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessagesAdapter(private var messages : MutableList<Message>):RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvMessage: TextView = view.findViewById<TextView>(R.id.tvMessage)
        val tvUserName : TextView = view.findViewById<TextView>(R.id.tvUserName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.users_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.tvUserName.text = message.user
        holder.tvMessage.text = message.message
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
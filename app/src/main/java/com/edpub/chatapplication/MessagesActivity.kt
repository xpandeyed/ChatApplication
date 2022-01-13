package com.edpub.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagesActivity : AppCompatActivity() {

    private val sender:String = Firebase.auth.currentUser!!.uid
    private val name = Firebase.auth.currentUser!!.displayName!!
    private lateinit var receiver:String
    private lateinit var rvMessages :RecyclerView
    private lateinit var adapter : MessagesAdapter
    private var messages  = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        receiver = intent.getStringExtra("RECEIVER")!!

        loadMessages()

        rvMessages = findViewById(R.id.rvMessages)
        adapter = MessagesAdapter(messages)
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(this)
        val bSend = findViewById<Button>(R.id.bSend)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        bSend.setOnClickListener {
            val message = etMessage.text.toString()
            if(message.isEmpty() || message.length>99){
                Toast.makeText(this, "Message must have length between 0 and 100", Toast.LENGTH_SHORT).show()
            }
            else{
                sendMessage(message)
                etMessage.setText("")
            }
        }

    }

    private fun loadMessages(){
        messages.clear()
        Log.i("XPND", "load Messages Called")
        CoroutineScope(Dispatchers.IO).launch{
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("USERS").child(sender).child(receiver)
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        messages.clear()
                        for(message in snapshot.children){
                            Log.i("XPND", message.toString())
                            val currMessage = message.getValue(Message::class.java)
                            Log.i("NEWNEW", currMessage.toString())
                            messages.add(currMessage!!)
                        }
                        adapter.notifyDataSetChanged()


                    }else{
                        Log.i("XPND", "Snapshot does not exist")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.i("XPND", error.toString())
                }
            })
        }
    }

    private fun sendMessage(message: String) {
        messages.add(Message(name, message))
        Log.i("NEWNEW", messages.toString())
        adapter.notifyDataSetChanged()
        val database = Firebase.database.getReference("USERS")
        database.child(sender).child(receiver).child(messages.size.toString()).child(name).setValue(message)
        database.child(receiver).child(sender).child(messages.size.toString()).child(name).setValue(message)
    }
}
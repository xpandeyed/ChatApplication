package com.edpub.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var fName : String
    private var messages  = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        setSupportActionBar(findViewById(R.id.tbChat))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        receiver = intent.getStringExtra("RECEIVER")!!
        fName = intent.getStringExtra("FNAME")!!

        val tbChat = findViewById<Toolbar>(R.id.tbChat)
        tbChat.title = fName


        loadMessages()

        rvMessages = findViewById(R.id.rvMessages)
        adapter = MessagesAdapter(messages)
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(this)
        val fabSend = findViewById<FloatingActionButton>(R.id.fabSend)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        fabSend.setOnClickListener {
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
        val database = Firebase.database.getReference("USERS")

        database.child(sender).child(receiver).child(messages.size.toString()).child("NAME").setValue(name)
        database.child(sender).child(receiver).child(messages.size.toString()).child("MESSAGE").setValue(message)
        database.child(receiver).child(sender).child(messages.size.toString()).child("NAME").setValue(name)
        database.child(receiver).child(sender).child(messages.size.toString()).child("MESSAGE").setValue(message)

        database.child(sender).child("RECENTS").child(receiver).child("UID").setValue(receiver)
        database.child(sender).child("RECENTS").child(receiver).child("NAME").setValue(fName)
        database.child(sender).child("RECENTS").child(receiver).child("MESSAGE").setValue(message)


        database.child(receiver).child("RECENTS").child(sender).child("UID").setValue(sender)
        database.child(receiver).child("RECENTS").child(sender).child("NAME").setValue(name)
        database.child(receiver).child("RECENTS").child(sender).child("MESSAGE").setValue(message)

    }
}
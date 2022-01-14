package com.edpub.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private val adapter = RecentChatAdapter(DataCollection.recentChats)
    private val sender = Firebase.auth.currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadRecentChats()

        val rvRecentChats = findViewById<RecyclerView>(R.id.rvRecentChats)
        rvRecentChats.layoutManager=LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: RecentChatAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val receiver = DataCollection.recentChats[position]
                val name = receiver.NAME
                val uid = receiver.UID
                val intent = Intent(this@HomeActivity, MessagesActivity::class.java)
                intent.putExtra("RECEIVER", uid)
                intent.putExtra("FNAME", name)
                startActivity(intent)
            }
        })


        rvRecentChats.adapter = adapter






        val tbHome = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tbHome)
        tbHome.inflateMenu(R.menu.home_activity_appbar)
        tbHome.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.miProfile->{
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        val fabNewMessage = findViewById<FloatingActionButton>(R.id.fabNewMessage)
        fabNewMessage.setOnClickListener {
            val intent = Intent(this, UsersListActivity::class.java)
            startActivity(intent)
        }
    }


    private fun loadRecentChats(){
        Log.i("XPND", "load Users Called")
        CoroutineScope(Dispatchers.IO).launch{
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("USERS").child(sender).child("RECENTS")
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        Log.i("XPND", "Chapter titles exist")
                        DataCollection.recentChats.clear()
                        for(user in snapshot.children){
                            val currUser = user.getValue(RecentChat::class.java)
                            DataCollection.recentChats.add(currUser!!)
                        }
                        adapter.notifyDataSetChanged()


                    }else{
                        Log.i("XPND", "Chapter titles do not exist")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.i("XPND", error.toString())
                }
            })
        }
    }
}
package com.edpub.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersListActivity : AppCompatActivity() {
    private val adapter = UsersListAdapter(DataCollection.usersList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        setSupportActionBar(findViewById(R.id.tbUsersList))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(!DataCollection.isUsersListLoaded){
            loadUsers()
        }

        val rvUsers = findViewById<RecyclerView>(R.id.rvUsers)
        
        adapter.setOnItemClickListener(object : UsersListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                val receiver = DataCollection.usersList[position].UID
                val name = DataCollection.usersList[position].NAME
                val intent = Intent(this@UsersListActivity, MessagesActivity::class.java)
                intent.putExtra("RECEIVER", receiver)
                intent.putExtra("FNAME", name)

                Log.i("NEWNEW", receiver.toString())
                Log.i("NEWNEW", "Starting chat activity")
                startActivity(intent)

            }
        })
        
        rvUsers.adapter = adapter
        rvUsers.layoutManager = LinearLayoutManager(this)
    }

    private fun loadUsers(){
        Log.i("XPND", "load Users Called")
        CoroutineScope(Dispatchers.IO).launch{
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("USERS")
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        Log.i("XPND", "Chapter titles exist")
                        DataCollection.usersList.clear()
                        val me = Firebase.auth.currentUser!!.uid
                        for(user in snapshot.children){
                            val currUser = user.getValue(User::class.java)
                            if(currUser!!.UID==me){
                                continue
                            }
                            DataCollection.usersList.add(currUser)
                        }
                        DataCollection.isUsersListLoaded = true
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
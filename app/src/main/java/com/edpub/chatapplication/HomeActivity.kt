package com.edpub.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
}
package com.edpub.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(findViewById(R.id.tbProfile))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.tvUserName).text = FirebaseAuth.getInstance().currentUser?.displayName
        findViewById<TextView>(R.id.tvEmail).text = FirebaseAuth.getInstance().currentUser?.email
        Glide.with(this).load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(findViewById(R.id.ivProfilePicture))
    }
}
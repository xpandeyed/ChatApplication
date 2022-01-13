package com.edpub.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(findViewById(R.id.tbProfile))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
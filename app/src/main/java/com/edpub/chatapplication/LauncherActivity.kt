package com.edpub.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Firebase.auth.currentUser==null) {
            val intent = Intent(this@LauncherActivity, SignUp::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_launcher)
    }
}
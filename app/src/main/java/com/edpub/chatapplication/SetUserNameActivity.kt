package com.edpub.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SetUserNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_user_name)

        val etUserName = findViewById<EditText>(R.id.etUserName)
        val bSetUserName = findViewById<Button>(R.id.bSetUserName)

        bSetUserName.setOnClickListener {
            val userName = etUserName.text.toString()
            FirebaseDatabase.getInstance().getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("NAME").setValue(userName)
        }

    }
    private fun isValidUserName():Boolean{

        return false
    }
}
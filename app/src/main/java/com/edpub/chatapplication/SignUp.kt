package com.edpub.chatapplication

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val privacyPolicyMessage = "By Signing In you agree to our Terms and Conditions and Privacy Policy."
        val spannable = SpannableString(privacyPolicyMessage)
        val span1 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val url = "https://xpandeyed.github.io/EdPubCPPWeb/TermsAndConditions.html"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
        val span2 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val url = "https://xpandeyed.github.io/EdPubCPPWeb/PrivacyPolicy.html"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
        spannable.setSpan(span1, 31, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(span2, 56, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val tvTermsConditionsAndPrivacyPolicy = findViewById<TextView>(R.id.tvTermsConditionsAndPrivacyPolicy)
        tvTermsConditionsAndPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
        tvTermsConditionsAndPrivacyPolicy.text = spannable


        val troubleMessage = "Having trouble in signing in? Contact Us."
        val spannableContactUs = SpannableString(troubleMessage)
        val span3 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("lalbiharipandeyg@gmail.com")) // recipients
                    putExtra(Intent.EXTRA_SUBJECT, "Trouble in Signing In")
                }
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@SignUp, "No email app found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        spannableContactUs.setSpan(span3, 30, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
        val tvContact = findViewById<TextView>(R.id.tvContact)
        tvContact.movementMethod = LinkMovementMethod.getInstance()
        tvContact.text = spannableContactUs

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        val cvSignIn = findViewById<CardView>(R.id.cvSignIn)
        cvSignIn.setOnClickListener {
            signIn()
        }


    }

    private val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        Log.i("XPND", "2 : Register activity for result called")
        findViewById<ProgressBar>(R.id.pbSignUp).visibility = View.GONE
        if(it.resultCode == Activity.RESULT_OK){
            Log.i("XPND", "3 : Result code matched")
            Log.i("XPND", "${it.resultCode} || ${Activity.RESULT_OK}")
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

            if(task.isSuccessful){
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Toast.makeText(this@SignUp, "Authenticating with Google.", Toast.LENGTH_SHORT).show()
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.i("XPND", "Api Exception $e")
                    Toast.makeText(this@SignUp, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Log.i("XPND", "3 : Result code match failed")
            Log.i("XPND", "${it.resultCode} || ${Activity.RESULT_OK}")
        }
    }
    private fun signIn() {
        findViewById<ProgressBar>(R.id.pbSignUp).visibility = View.VISIBLE
        val signInIntent = googleSignInClient.signInIntent
        result.launch(signInIntent)
        Log.i("XPND", "1 : sign in function called")
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.i("XPND", "4: firebaseAuthWithGoogle invoked")
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("XPND", "5: firebaseAuthWithGoogle's launch invoked")
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this@SignUp){ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@SignUp,"Sign in with credential is successful",Toast.LENGTH_SHORT).show()
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.i("XPND", "Task is successful")

                            if(task.result.additionalUserInfo!!.isNewUser){
                                val user = auth.currentUser
                                val uid = user?.uid
                                val firebaseDatabase = FirebaseDatabase.getInstance().getReference("USERS")
                                firebaseDatabase.child(uid!!).child("NAME").setValue(user.displayName)
                                firebaseDatabase.child(uid).child("UID").setValue(uid)
                            }



                            val intent = Intent(this@SignUp, HomeActivity::class.java)
                            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    } else {
                        Log.i("XPND", "Task failed")
                        Toast.makeText(this@SignUp, "${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Log.i("XPND", "Task failure message")
                }
        }
    }
}
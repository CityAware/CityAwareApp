package com.example.cityaware


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cityaware.R.id.loading_spinner
import com.google.android.material.textfield.TextInputEditText
//import com.google.firebase.auth.FirebaseUser


public class LogInActivity : AppCompatActivity(){

    var LogIn_email: TextInputEditText? = null
    var LogIn_password:TextInputEditText? = null
    var toSignUp: TextView? = null
    var LogIn_btn: Button? = null
    var i: Intent? = null
    var loaderIV: ImageView? = null
    var errorTV: TextView? = null
//    var user: FirebaseUser? = null
    var label: String? = null
    var sp: SharedPreferences? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        LogIn_email = findViewById(R.id.logInEmail)
        LogIn_password = findViewById(R.id.logInPassword)
        LogIn_btn = findViewById(R.id.login_btn1)
        toSignUp = findViewById(R.id.login_to_signup_tv)

        sp = getSharedPreferences("user", MODE_PRIVATE)
        loaderIV = findViewById(R.id.loading_spinner)
        loaderIV?.let { it.visibility = View.GONE}
        errorTV = findViewById(R.id.login_error)
    }
}
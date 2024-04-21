package com.example.cityaware

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


import com.example.cityaware.model.Model
import com.google.android.material.textfield.TextInputEditText


class SignUpActivity : AppCompatActivity() {
    var editTextemail: TextInputEditText? = null
    var editTextpassword: TextInputEditText? = null
    var SignUpBtn: Button? = null
    var i: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        editTextemail = findViewById<TextInputEditText>(R.id.email)
        editTextpassword = findViewById<TextInputEditText>(R.id.password)
        editTextpassword!!.setTransformationMethod(PasswordTransformationMethod())
        SignUpBtn = findViewById<Button>(R.id.signUpBtn)
        SignUpBtn!!.setOnClickListener(View.OnClickListener {
            val password: String
            val email: String
            email = editTextemail!!.getText().toString()
            password = editTextpassword!!.getText().toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "missing email or password", Toast.LENGTH_LONG).show()
                return@OnClickListener
            } else {
                Model.instance().signUp(email, password) { isok ->
                    if (isok as Boolean) {
                        // Sign up success, update UI with the signed-in user's information
                        Toast.makeText(
                            this@SignUpActivity,
                            "user has been authenticated",
                            Toast.LENGTH_SHORT
                        ).show()
                        i = Intent(
                            applicationContext,
                            MainActivity::class.java
                        )
                        startActivity(i)
                        return@signUp
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                //
            }
        })
    }
}
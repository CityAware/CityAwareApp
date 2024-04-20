package com.example.cityaware


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cityaware.R.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    var editTextemail: TextInputEditText? = null
    var editTextpassword: TextInputEditText? = null
    var loginBtn: Button? = null
    var mAuth: FirebaseAuth? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)
        editTextemail = findViewById(id.email)
        editTextpassword = findViewById(id.password)
        editTextpassword!!.setTransformationMethod(PasswordTransformationMethod())
        loginBtn = findViewById<Button>(id.login_btn)
        loginBtn!!.setOnClickListener(View.OnClickListener {
            val password: String
            val email: String
            mAuth = FirebaseAuth.getInstance()
            email = editTextemail!!.getText().toString()
            password = editTextpassword!!.getText().toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "missing email or password", Toast.LENGTH_LONG).show()
                return@OnClickListener
            } else {
                mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(
                                this@LoginActivity, "user has been authenticated",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        })
    }
}
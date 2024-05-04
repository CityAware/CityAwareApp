package com.example.karenhub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.karenhub.model.Model
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser

class LogInActivity : AppCompatActivity() {
    var LogIn_email: TextInputEditText? = null
    var LogIn_password: TextInputEditText? = null
    var toSignUp: TextView? = null
    var LogIn_btn: Button? = null
    var i: Intent? = null
    var loaderIV: ImageView? = null
    var errorTV: TextView? = null
    var user: FirebaseUser? = null
    var label: String? = null
    var sp: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        LogIn_email = findViewById(R.id.logInEmail)
        LogIn_password = findViewById(R.id.logInPassword)
        LogIn_btn = findViewById(R.id.login_btn1)
        toSignUp = findViewById(R.id.login_to_signup_tv)
        user = Model.instance().auth.currentUser
        sp = getSharedPreferences("user", MODE_PRIVATE)
        loaderIV = findViewById(R.id.loading_spinner)
        loaderIV!!.setVisibility(View.GONE)
        errorTV = findViewById(R.id.login_error)

        FirebaseApp.initializeApp(this)
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        toSignUp!!.setOnClickListener(View.OnClickListener {
            i = Intent(applicationContext, SignUpActivity::class.java)
            val bundle = ActivityOptionsCompat.makeCustomAnimation(
                applicationContext, android.R.anim.fade_in, android.R.anim.fade_out
            )
                .toBundle()
            startActivity(i, bundle)
            finish()
        })
        LogIn_btn!!.setOnClickListener(View.OnClickListener {
            errorTV!!.setText("")
            val email: String
            val password: String
            email = LogIn_email!!.getText().toString()
            password = LogIn_password!!.getText().toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "the password or email you insert is not valid",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                LogIn_btn!!.setClickable(false)
                loaderIV!!.post(Runnable {
                    loaderIV!!.setVisibility(View.VISIBLE)
                    val animation = RotateAnimation(
                        360.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    animation.interpolator = LinearInterpolator()
                    animation.duration = 1000
                    animation.repeatCount = Animation.INFINITE
                    loaderIV!!.startAnimation(animation)
                })
                Model.instance().login(email, password) { result: Pair<Boolean, String?> ->
                    if (result.first) {
                        Model.instance().db.collection("users")
                            .whereEqualTo("email", email).get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    //Toast.makeText(LogInActivity.this, result.second, Toast.LENGTH_SHORT).show();
                                    errorTV!!.setText(result.second)
                                    val querySnapshot = task.result
                                    if (!querySnapshot.isEmpty) {
                                        val document = querySnapshot.documents[0]
                                        label = document.getString("label")
                                        val editor = sp!!.edit()
                                        editor.putString("email", email)
                                        editor.putString("label", label)
                                        editor.putString("password", password)
                                        editor.apply()
                                        i = Intent(applicationContext, MainActivity::class.java)
                                        val bundle = ActivityOptionsCompat.makeCustomAnimation(
                                            applicationContext,
                                            android.R.anim.fade_in,
                                            android.R.anim.fade_out
                                        )
                                            .toBundle()
                                        startActivity(i, bundle)
                                        finish()
                                    }
                                } else {
                                    errorTV!!.setText("An Error has occurred")
                                }
                            }
                    } else {
                        errorTV!!.setText(result.second)
                    }
                    loaderIV!!.post(Runnable {
                        loaderIV!!.clearAnimation()
                        loaderIV!!.setVisibility(View.GONE)
                    })
                    LogIn_btn!!.setClickable(true)
                }
            }
        })
    }
}
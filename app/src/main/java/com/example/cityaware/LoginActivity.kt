package com.example.cityaware


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import com.example.cityaware.model.Model
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser


class LogInActivity : AppCompatActivity() {
   lateinit var LogIn_email: TextInputEditText
    lateinit var LogIn_password: TextInputEditText
    lateinit var LogIn_btn: Button
    lateinit var i: Intent
    var user: FirebaseUser? = null
    lateinit var toSignUp: TextView
    lateinit var loaderIV: ImageView
    lateinit var errorTV: TextView
    lateinit var label: String
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LogIn_email = findViewById(R.id.logInEmail)
        LogIn_password = findViewById(R.id.logInPassword)
        LogIn_btn = findViewById(R.id.login_btn1)
        toSignUp = findViewById(R.id.login_to_signup_tv)
        sp = getSharedPreferences("user", MODE_PRIVATE)
        loaderIV = findViewById(R.id.loading_spinner)
        loaderIV.visibility = View.GONE
        errorTV = findViewById(R.id.login_error)

        if (user != null) {
            user=Model.instance().auth.currentUser
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        toSignUp.setOnClickListener {
            i = Intent(applicationContext, SignUpActivity::class.java)
            val bundle = ActivityOptionsCompat.makeCustomAnimation(
                applicationContext, android.R.anim.fade_in, android.R.anim.fade_out
            )
                .toBundle()
            startActivity(i, bundle)
            finish()
        }

        LogIn_btn.setOnClickListener(View.OnClickListener {
            errorTV.text = ""
            val email: String
            val password: String
            email = LogIn_email.getText().toString()
            password = LogIn_password.getText().toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "the password or email you insert is not valid",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                LogIn_btn.isClickable = false
                loaderIV.post {
                    loaderIV.setVisibility(View.VISIBLE)
                    val animation = RotateAnimation(
                        360.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    animation.interpolator = LinearInterpolator()
                    animation.setDuration(1000)
                    animation.setRepeatCount(Animation.INFINITE)
                    loaderIV.startAnimation(animation)
                }
                Model.instance().login(email, password,  Model.Listener<Pair<Boolean, String>> {
                    Model.instance().login(email, password,  Model.Listener<Pair<Boolean,String>> {
                        var second = it.second
                        if (it.first){
                            Model.instance().db.collection("users").whereEqualTo("email",email).get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    errorTV.setText(second)
                                    val querySnapshot = task.result
                                    if (!querySnapshot.isEmpty) {
                                        val document = querySnapshot.documents[0]
                                        label = document.getString("label")!!
                                        val editor = sp.edit()
                                        editor.putString("email", email)
                                        editor.putString("label", label)
                                        editor.putString("password", password)
                                        editor.apply()
                                        i = Intent(applicationContext, MainActivity::class.java)
                                        val bundle = ActivityOptionsCompat.makeCustomAnimation(
                                            applicationContext,
                                            android.R.anim.fade_in,
                                            android.R.anim.fade_out
                                        ).toBundle()
                                        startActivity(i, bundle)
                                        finish()
                                    }
                                } else {
                                    errorTV.text = "An Error has occurred"
                                }
                            }
                        }else {
                            errorTV.text = second
                        }
                        loaderIV.post {
                            loaderIV.clearAnimation()
                            loaderIV.visibility = View.GONE
                        }
                        LogIn_btn.isClickable = true
                    })
                })
            }
        })
    }
}
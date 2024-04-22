package com.example.cityaware


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
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


class SignUpActivity : AppCompatActivity() {
    var editTextEmail: TextInputEditText? = null
    var editTextPassword: TextInputEditText? = null
    var editTextAccLabel: TextInputEditText? = null
    var errorTV: TextView? = null
    var loaderIV: ImageView? = null
    var signUpBtn: Button? = null
    var i: Intent? = null
    var sp: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setTitle(R.string.signup)
        sp = getSharedPreferences("user", MODE_PRIVATE)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextPassword!!.setTransformationMethod(PasswordTransformationMethod())
        editTextAccLabel = findViewById(R.id.AccLabel)
        LabelChecker()
        errorTV = findViewById(R.id.signup_error)
        loaderIV = findViewById(R.id.loading_spinner)
        loaderIV!!.setVisibility(View.GONE)
        signUpBtn = findViewById(R.id.signUpBtn)
        SignUpListener()
    }

    private fun SignUpListener() {
        signUpBtn!!.setOnClickListener {
            errorTV!!.text = ""
            val password: String
            val email: String
            val label: String
            email = editTextEmail!!.getText().toString()
            password = editTextPassword!!.getText().toString()
            label = editTextAccLabel!!.getText().toString()
            if (email.isEmpty() || password.isEmpty() || label.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "missing either an email, label, or password.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                signUpBtn!!.isClickable = false
                // Load in UI Thread
                loaderIV!!.post {
                    loaderIV!!.setVisibility(View.VISIBLE)
                    val animation = RotateAnimation(
                        360.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    animation.interpolator = LinearInterpolator()
                    animation.setDuration(1000)
                    animation.setRepeatCount(Animation.INFINITE)
                    loaderIV!!.startAnimation(animation)
                }
                Model.instance().signUp(email, label, password, object : Model.Listener<Pair<Boolean?, String?>?> {
                    override fun onComplete(result: Pair<Boolean?, String?>?) {
                        if (result?.first == true) {
                            // Sign up success, update UI with the signed-in user's information
                            Toast.makeText(this@SignUpActivity, result.second, Toast.LENGTH_SHORT)
                                .show()
                            val editor = sp!!.edit()
                            editor.putString("email", email)
                            editor.putString("label", label)
                            editor.putString("password", password)
                            editor.apply()
                            i = Intent(applicationContext, MainActivity::class.java)
                            val bundle = ActivityOptionsCompat.makeCustomAnimation(
                                applicationContext, android.R.anim.fade_in, android.R.anim.fade_out
                            )
                                .toBundle()
                            startActivity(i, bundle)
                            finish()
                        } else {
                            errorTV!!.setText(result?.second ?: "")
                            Toast.makeText(this@SignUpActivity, result?.second, Toast.LENGTH_SHORT)
                                .show()
                        }
                        loaderIV!!.post {
                            loaderIV!!.clearAnimation()
                            loaderIV!!.setVisibility(View.GONE)
                        }
                        signUpBtn!!.isClickable = true
                    }
                })
            }
        }
    }

    private fun LabelChecker() {
        editTextAccLabel!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed
                var s = s
                val keyRegex = "[a-zA-Z0-9]"
                if (s.toString().contains(" ")) {
                    s = s.toString().replace(" ", "")
                    editTextAccLabel!!.setText(s)
                    editTextAccLabel!!.setSelection(s.length)
                }
                if (s.length != 0 && !s[s.length - 1].toString()
                        .matches(keyRegex.toRegex()) && before != 1
                ) {
                    return
                }
                if (s.length != 0 && !s.toString().startsWith("@")) {
                    if (s.toString().split("@".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray().size > 1) {
                        val atIndex = s.toString().lastIndexOf("@")
                        val PrevS = s.subSequence(atIndex, s.length).toString()
                        editTextAccLabel!!.setText(PrevS)
                        editTextAccLabel!!.setSelection(1)
                    } else {
                        s = "@$s"
                        editTextAccLabel!!.setText(s)
                        editTextAccLabel!!.setSelection(s.length)
                    }
                }
                if (s.toString() == "@") {
                    s = ""
                    editTextAccLabel!!.setText(s)
                    editTextAccLabel!!.setSelection(s.length)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }
}
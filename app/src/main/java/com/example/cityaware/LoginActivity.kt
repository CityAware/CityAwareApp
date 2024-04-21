package com.example.cityaware



import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cityaware.R;
import com.example.cityaware.model.Model
import com.google.android.material.textfield.TextInputEditText


class LogInActivity : AppCompatActivity() {
    var LogIn_email: TextInputEditText? = null
    var LogIn_password: TextInputEditText? = null
    var LogIn_btn: Button? = null
    var i: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        LogIn_email = findViewById<TextInputEditText>(R.id.logInEmail)
        LogIn_password = findViewById<TextInputEditText>(R.id.logInPassword)
        LogIn_btn = findViewById<Button>(R.id.login_btn1)
        LogIn_btn!!.setOnClickListener(View.OnClickListener {
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
                Model.instance().login(email, password, object : Model.Listener<String?> {
                    override fun onComplete(isValid: String?) {
                        if (!isValid.isNullOrEmpty() && isValid.contains(email)) {
                            Toast.makeText(this@LogInActivity, " not null", Toast.LENGTH_SHORT).show()
                        }
                        if (isValid == email) {
                            Toast.makeText(this@LogInActivity, "user has been registered", Toast.LENGTH_SHORT).show()
                            i = Intent(applicationContext, MainActivity::class.java)
                            startActivity(i)
                        }
                    }
                })
            }
        })
    }
}
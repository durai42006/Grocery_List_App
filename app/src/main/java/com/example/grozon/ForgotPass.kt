package com.example.grozon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPass : AppCompatActivity() {

    private lateinit var mil: EditText
    private lateinit var submitEmail: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_page)

        mil = findViewById(R.id.logmail)
        submitEmail = findViewById(R.id.submitMail)
        val backToLogin = findViewById<TextView>(R.id.back2log)

        // Add listener for user input mail
        mil.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val stp = p0.toString().trim()
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(stp).matches()) {
                    Toast.makeText(this@ForgotPass, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Add listener to the button
        submitEmail.setOnClickListener {
            val email = mil.text.toString()
            if (email.isNotEmpty()) {
                if (isEmailRegistered(email)) {
                    val intent = Intent(this, ResetPassword::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        backToLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isEmailRegistered(email: String): Boolean {
        val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        return sharedPref.contains("$email:password")
    }
}

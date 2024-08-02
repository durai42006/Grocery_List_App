package com.example.grozon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ResetPassword : AppCompatActivity() {

    private var isPasswordValid = false


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.reset_password)
        val resetpass=findViewById<EditText>(R.id.resetpass)
        val resetconfpass=findViewById<EditText>(R.id.resetconfpass)
        val resetbutton = findViewById<Button>(R.id.resetbutton)

        val email = intent.getStringExtra("email") ?: return


        resetbutton.setOnClickListener {
            val pass = resetpass.text.toString().trim()
            val confpass = resetconfpass.text.toString().trim()

            if (pass.isNotEmpty()) {
                resetPassword(email, pass,confpass)
            }
            else if (confpass!=pass)
            {
                Toast.makeText(this, "Please enter a same password", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            }

        }

        resetpass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val st = p0.toString().trim()
                isPasswordValid = when {
                    st.isEmpty() -> {
                        Toast.makeText(this@ResetPassword, "Password required", Toast.LENGTH_SHORT).show()
                        false
                    }
                    st.length < 8 -> {
                        Toast.makeText(this@ResetPassword, "Password must contain 8 characters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[a-z].*")) -> {
                        Toast.makeText(this@ResetPassword, "Password must contain small letters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[A-Z].*")) -> {
                        Toast.makeText(this@ResetPassword, "Password must contain capital letters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[!@#$%^&*(),.?:{}|<>].*")) -> {
                        Toast.makeText(this@ResetPassword, "Password must contain special characters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    else -> true
                }
            }
        })

        val hideshowicon1 = findViewById<ImageView>(R.id.showhideicon1)
        val hideshowicon2 = findViewById<ImageView>(R.id.showhideicon2)
        var isPasswordVisible = false

        hideshowicon1.setOnClickListener {
            if (isPasswordVisible) {
                resetpass.transformationMethod = PasswordTransformationMethod.getInstance()
                hideshowicon1.setImageResource(R.drawable.baseline_visibility_off)
            } else {
                resetpass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                hideshowicon1.setImageResource(R.drawable.baseline_visibility_on)
            }
            isPasswordVisible = !isPasswordVisible
            resetpass.setSelection(resetpass.text.length)
        }

        hideshowicon2.setOnClickListener {
            if (isPasswordVisible) {
                resetconfpass.transformationMethod = PasswordTransformationMethod.getInstance()
                hideshowicon2.setImageResource(R.drawable.baseline_visibility_off)
            } else {
                resetconfpass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                hideshowicon2.setImageResource(R.drawable.baseline_visibility_on)
            }
            isPasswordVisible = !isPasswordVisible
            resetconfpass.setSelection(resetconfpass.text.length)
        }

    }


    private fun resetPassword(email: String, newPassword: String, confPassword: String) {
        if (newPassword == confPassword) {
            val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("$email:password", newPassword) // Update the password
            editor.apply()

            Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show()

            // Navigate back to login or main activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        }
    }


}

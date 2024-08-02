package com.example.grozon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private var isEmailValid = false
    private var isPasswordValid = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            // Navigate to homepage
            val intent = Intent(this, Action::class.java)
            startActivity(intent)
            finish()
        }

        val username = findViewById<EditText>(R.id.logmail)
        val pass = findViewById<EditText>(R.id.logpass)
        val bt = findViewById<Button>(R.id.btn)

        bt.setOnClickListener {
            val email = username.text.toString().trim()
            val password = pass.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid) {
                if (verifyUserDetails(email, password))
                {
                    saveLoginState(email,true)
                    val intent = Intent(this, Action::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                if (email.isEmpty()) {
                    Toast.makeText(this@Login, "Email id required", Toast.LENGTH_SHORT).show()
                } else if (password.isEmpty()) {
                    Toast.makeText(this@Login, "Password required", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Enter correct Email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        pass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val st = p0.toString().trim()
                isPasswordValid = when {
                    st.isEmpty() -> {
                        Toast.makeText(this@Login, "Password required", Toast.LENGTH_SHORT).show()
                        false
                    }
                    st.length < 8 -> {
                        Toast.makeText(this@Login, "Password must contain 8 characters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[a-z].*")) -> {
                        Toast.makeText(this@Login, "Password must contain small letters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[A-Z].*")) -> {
                        Toast.makeText(this@Login, "Password must contain capital letters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[!@#$%^&*(),.?:{}|<>].*")) -> {
                        Toast.makeText(this@Login, "Password must contain special characters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    else -> true
                }
            }
        })

        val hideshowicon = findViewById<ImageView>(R.id.showhideicon)
        var isPasswordVisible = false

        hideshowicon.setOnClickListener {
            if (isPasswordVisible) {
                pass.transformationMethod = PasswordTransformationMethod.getInstance()
                hideshowicon.setImageResource(R.drawable.baseline_visibility_off)
            } else {
                pass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                hideshowicon.setImageResource(R.drawable.baseline_visibility_on)
            }
            isPasswordVisible = !isPasswordVisible
            pass.setSelection(pass.text.length)
        }

        val fgot = findViewById<TextView>(R.id.forpass)
        fgot.setOnClickListener {
            val intent = Intent(this, ForgotPass::class.java)
            startActivity(intent)
            finish()
        }

        val sign = findViewById<TextView>(R.id.newacc)
        sign.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val stp = p0.toString().trim()
                isEmailValid = when {
                    !Patterns.EMAIL_ADDRESS.matcher(stp).matches() -> {
                        Toast.makeText(this@Login, "Enter Correct mail id", Toast.LENGTH_SHORT).show()
                        false
                    }
                    else -> true
                }
            }
        })
    }

    private fun verifyUserDetails(email: String, password: String): Boolean {
        val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val storedPassword = sharedPref.getString("$email:password", null)
        Log.d("data","data : $storedPassword")
        return storedPassword == password
    }

    private fun saveLoginState(email: String,isLoggedIn: Boolean) {
        val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("current_user_email", email)
        editor.putBoolean("is_logged_in", isLoggedIn)
        editor.apply()
    }
}

package com.example.grozon

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignUp : AppCompatActivity() {

    private var isEmailValid = false
    private var isPasswordValid = false
    private var isConfirmPasswordValid = false

    private lateinit var dobEditText: EditText
    private lateinit var sin: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup)

        val lin = findViewById<TextView>(R.id.Backlogin)
        lin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        val nm = findViewById<EditText>(R.id.userName)
        val eml = findViewById<EditText>(R.id.mail)
            dobEditText=findViewById(R.id.dob_text_view)
        val psd = findViewById<EditText>(R.id.pass)
        val cnfmpswd = findViewById<EditText>(R.id.conpass)
            sin = findViewById(R.id.signupbtn)

        dobEditText.setOnClickListener {
            showDatePickerDialog()
        }

        sin.setOnClickListener {
            val fullname = nm.text.toString().trim()
            val mail = eml.text.toString().trim()
            val dob=dobEditText.text.toString().trim()
            val first_pass = psd.text.toString().trim()
            val confirm_pass = cnfmpswd.text.toString().trim()

            if (fullname.isNotEmpty() && mail.isNotEmpty() && first_pass.isNotEmpty() && confirm_pass.isNotEmpty() && first_pass == confirm_pass && isEmailValid && isPasswordValid) {
                saveUserDetails(fullname, mail, first_pass,dob)
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            } else {
                if (fullname.isEmpty()) {
                    Toast.makeText(this, "Enter your Name", Toast.LENGTH_SHORT).show()
                } else if (mail.isEmpty()) {
                    Toast.makeText(this, "Enter your Mail", Toast.LENGTH_SHORT).show()
                } else if (first_pass.isEmpty()) {
                    Toast.makeText(this, "Enter your New password", Toast.LENGTH_SHORT).show()
                } else if (confirm_pass.isEmpty()) {
                    Toast.makeText(this, "Confirm your Password", Toast.LENGTH_SHORT).show()
                } else if (confirm_pass != first_pass) {
                    Toast.makeText(this, "Enter the same password for confirmation", Toast.LENGTH_SHORT).show()
                } else if (!isEmailValid) {
                    Toast.makeText(this, "Enter a correct email format", Toast.LENGTH_SHORT).show()
                } else if (!isPasswordValid) {
                    Toast.makeText(this, "Password must meet all the requirements", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val hideshowicon = findViewById<ImageView>(R.id.showhideicon)
        var isPasswordVisible = false
        hideshowicon.setOnClickListener {
            if (isPasswordVisible) {
                psd.transformationMethod = PasswordTransformationMethod.getInstance()
                hideshowicon.setImageResource(R.drawable.baseline_visibility_off)
            } else {
                psd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                hideshowicon.setImageResource(R.drawable.baseline_visibility_on)
            }
            isPasswordVisible = !isPasswordVisible
            psd.setSelection(psd.text.length)
        }
        val hideshowicon2 = findViewById<ImageView>(R.id.showhideicon1)
        var isPasswordVisible2 = false
        hideshowicon2.setOnClickListener {
            if (isPasswordVisible2) {
                cnfmpswd.transformationMethod = PasswordTransformationMethod.getInstance()
                hideshowicon2.setImageResource(R.drawable.baseline_visibility_off)
            } else {
                cnfmpswd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                hideshowicon2.setImageResource(R.drawable.baseline_visibility_on)
            }
            isPasswordVisible2 = !isPasswordVisible2
            psd.setSelection(cnfmpswd.text.length)
        }


        eml.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val stp = p0.toString().trim()
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(stp).matches()
                if (!isEmailValid) {
                    Toast.makeText(this@SignUp, "Enter a correct email format", Toast.LENGTH_SHORT).show()
                }
            }
        })

        psd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val st = p0.toString().trim()
                isPasswordValid = when {
                    st.isEmpty() -> {
                        Toast.makeText(this@SignUp, "Password required", Toast.LENGTH_SHORT).show()
                        false
                    }
                    st.length < 8 -> {
                        Toast.makeText(this@SignUp, "Password must contain at least 8 characters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[a-z].*")) -> {
                        Toast.makeText(this@SignUp, "Password must contain small letters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[A-Z].*")) -> {
                        Toast.makeText(this@SignUp, "Password must contain capital letters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    !st.matches(Regex(".*[!@#$%^&*(),.?:{}|<>].*")) -> {
                        Toast.makeText(this@SignUp, "Password must contain special characters", Toast.LENGTH_SHORT).show()
                        false
                    }
                    else -> true
                }
            }
        })

//        cnfmpswd.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun afterTextChanged(p0: Editable?) {
//                val st = p0.toString().trim()
//                isConfirmPasswordValid = st == psd.text.toString().trim()
//                if (isConfirmPasswordValid==false) {
//                    Toast.makeText(this@SignUp, "Passwords do not match", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
            dobEditText.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }



    private fun saveUserDetails(name: String, email: String, password: String, dob:String) {
        val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("$email:email", email)
        editor.putString("$email:name", name)
        editor.putString("$email:password", password)
        editor.putString("$email:D.O.B",dob)
        editor.putString("current_user_email", email)
        Log.d("gova","gova:$name : $password")


        editor.apply()
    }

}

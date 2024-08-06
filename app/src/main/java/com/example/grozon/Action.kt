package com.example.grozon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import importExcel

class Action : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.action)

        val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user_email", null) ?: ""
        val storedname = sharedPref.getString("$email:name","no name")



        val head="$storedname"

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.title=head

        drawerLayout = findViewById(R.id.drawer_layout)
        val navaView: NavigationView = findViewById(R.id.nav_view)
        navaView.setNavigationItemSelectedListener(this)


        val headerView = navaView.getHeaderView(0)
        val nameTextView = headerView.findViewById<TextView>(R.id.nav_header_title)
        nameTextView.text = "$email"

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        if (savedInstanceState == null) {
            replaceFragment(importExcel())
            navaView.setCheckedItem(R.id.firstFragment)
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction:FragmentTransaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()

    }

    private fun handleLogout() {
        // Clear login state
        val sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_logged_in", false)
            apply()
        }

        // Navigate to login activity
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.firstFragment->replaceFragment(importExcel())
            R.id.secondFragment->replaceFragment(YourFiles())
            R.id.thirdFragment->replaceFragment(Settings())
            R.id.fourthFragment->handleLogout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}
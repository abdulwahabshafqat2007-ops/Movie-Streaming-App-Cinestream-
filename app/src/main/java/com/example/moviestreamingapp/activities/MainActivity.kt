package com.example.moviestreamingapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviestreamingapp.R
import com.example.moviestreamingapp.database.FirestoreHelper
import com.example.moviestreamingapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestoreHelper = FirestoreHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hardcoded user data (no Firebase login required)
        val userName = "Abdulwahab"
        val planType = "Premium"
        val userId = 23038

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(userName, planType, userId), false)
        }

        bottomNav.setOnItemSelectedListener { menuItem ->
            val fragment: Fragment? = when (menuItem.itemId) {
                R.id.nav_home -> HomeFragment.newInstance(userName, planType, userId)
                R.id.nav_search -> SearchFragment()
                R.id.nav_discover -> ApiMoviesFragment()
                R.id.nav_watchlist -> WatchlistFragment()
                R.id.nav_profile -> {
                    // Open Compose Profile screen (no logout, just for viewing)
                    startActivity(Intent(this, ComposeProfileActivity::class.java))
                    null
                }
                else -> null
            }
            fragment?.let { loadFragment(it, false); true } ?: (menuItem.itemId == R.id.nav_profile)
        }
    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }
}
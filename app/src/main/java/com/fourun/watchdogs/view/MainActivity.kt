package com.fourun.watchdogs.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fourun.watchdogs.R
import com.fourun.watchdogs.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

@Suppress("OVERRIDE_DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()



        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, navigate to home fragment
            navigateToHomeFragment()
        } else {
            // User is not logged in, go to the login screen
            navigateToLoginFragment()
        }


        // Get the NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? NavHostFragment
        navController = navHostFragment?.navController ?: throw IllegalStateException("NavController not found")



        // Set up the ActionBar with the NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Connect the NavigationView to the NavController
        binding.navDrawer.setupWithNavController(navController)

        // Connect the BottomNavigationView to the NavController
        binding.bottomNavigation.setupWithNavController(navController)

    }

    private fun navigateToHomeFragment() {
        navController.navigate(R.id.homeFragment)
    }

    private fun navigateToLoginFragment() {
        navController.navigate(R.id.loginFragment)
    }

}
package com.tkjen.youtube.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tkjen.youtube.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.shortsFragment, R.id.libaryFragment)
        )

        // Standard setup
        bottomNavView.setupWithNavController(navController)

        // Custom handling for bottom navigation items
        bottomNavView.setOnItemSelectedListener { item ->
            // Handle navigation with proper back stack management
            when (item.itemId) {
                R.id.homeFragment, R.id.shortsFragment, R.id.libaryFragment -> {
                    // Pop back stack to start destination then navigate
                    navController.popBackStack(navController.graph.startDestinationId, false)
                    navController.navigate(item.itemId)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
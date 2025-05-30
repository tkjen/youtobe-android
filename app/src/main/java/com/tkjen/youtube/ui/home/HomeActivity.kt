package com.tkjen.youtube.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment // **QUAN TRỌNG**
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tkjen.youtube.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tạm thời bình luận enableEdgeToEdge() để loại trừ khả năng gây lỗi
        // enableEdgeToEdge()

        // **1. setContentView PHẢI được gọi TRƯỚC TIÊN**
        setContentView(R.layout.activity_home)

        // **2. Lấy NavController từ NavHostFragment MỘT CÁCH CHÍNH XÁC**
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        // 3. Tìm BottomNavigationView
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 4. Liên kết NavController với BottomNavigationView
        bottomNavView.setupWithNavController(navController)
    }
}
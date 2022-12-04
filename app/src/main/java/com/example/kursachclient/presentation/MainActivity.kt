package com.example.kursachclient.presentation

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kursachclient.R
import com.example.kursachclient.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)

//        window.decorView.windowInsetsController!!.hide(WindowInsets.Type.statusBars())
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun hideBottomNav() {
        try {
            binding.bottomNavigation.visibility = View.GONE
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun displayBottomNav() {
        try {
            binding.bottomNavigation.visibility = View.VISIBLE
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun hideOrderMenu() {
        try {
            val menu: Menu = binding.bottomNavigation.menu
            val target: MenuItem = menu.findItem(R.id.orderFragment)
            target.isVisible = false
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun showOrderMenu() {
        try {
            val menu: Menu = binding.bottomNavigation.menu
            val target: MenuItem = menu.findItem(R.id.orderFragment)
            target.isVisible = true
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }




}
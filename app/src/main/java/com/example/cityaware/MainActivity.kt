package com.example.cityaware

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_navhost) as NavHostFragment?
        navController = navHostFragment!!.navController
        setupActionBarWithNavController(this, navController!!)

        val navView = findViewById<BottomNavigationView>(R.id.main_bottomNavigationView)
        setupWithNavController(navView, navController!!)

    }
    var fragmentMenuId = 0
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (fragmentMenuId != 0) {
            menu.removeItem(fragmentMenuId)
        }
        fragmentMenuId = 0
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected( item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController!!.popBackStack()
        } else {
            fragmentMenuId = item.itemId
            return onNavDestinationSelected(item, navController!!)
        }
        return super.onOptionsItemSelected(item)
    }
}
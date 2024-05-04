package com.example.cityaware

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity constructor() : AppCompatActivity() {
    var navController: NavController? = null
    var fragment_state: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val navHostFragment: NavHostFragment? =
            getSupportFragmentManager().findFragmentById(R.id.main_navhost) as NavHostFragment?
        navController = navHostFragment!!.navController
        setupActionBarWithNavController(this, navController!!)
        val navView: BottomNavigationView = findViewById(R.id.main_bottomNavigationView)
        setupWithNavController(navView, navController!!)
        navController!!.addOnDestinationChangedListener(OnDestinationChangedListener({ navController: NavController, navDestination: NavDestination, bundle: Bundle? ->
            if ((navDestination.label == "Set Location")) {
                fragment_state = 1
                navView.getMenu().clear()
                navView.inflateMenu(R.menu.bar_map)
                val item: MenuItem = navView.getMenu().findItem(R.id.save_location)
                item.setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener({ i: MenuItem? ->
                    val viewModelProvider: ViewModelProvider = ViewModelProvider(this)
                    val viewModel: MapsFragmentModel = viewModelProvider.get(
                        MapsFragmentModel::class.java
                    )
                    val savedInstanceStateData: Bundle? = viewModel.savedInstanceStateData
                    val location: LatLng? = savedInstanceStateData!!.getParcelable("locationTemp")
                    val locationName: String? = savedInstanceStateData.getString("locationNameTemp")
                    savedInstanceStateData.putParcelable("location", location)
                    savedInstanceStateData.putString("locationName", locationName)
                    viewModel.savedInstanceStateData = savedInstanceStateData
                    /*MapsFragmentDirections.ActionMapsFragmentToAddNewPostFragment action =
                            MapsFragmentDirections.actionMapsFragmentToAddNewPostFragment(location,locationName);*/navController.popBackStack()
                    true
                }))
                setupWithNavController(navView, navController)
            } else if (fragment_state == 1) {
                fragment_state = 0
                navView.getMenu().clear()
                navView.inflateMenu(R.menu.bar_menu)
                setupWithNavController(navView, navController)
            } else {
                fragment_state = 0
            }
        }))
    }

    var fragmentMenuId: Int = 0
    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu)
        if (fragmentMenuId != 0) {
            menu.removeItem(fragmentMenuId)
        }
        fragmentMenuId = 0
        return super.onCreateOptionsMenu(menu)
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            navController!!.popBackStack()
        } else {
            fragmentMenuId = item.getItemId()
            return onNavDestinationSelected(item, (navController)!!)
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onSupportNavigateUp(): Boolean {
        return false
    }

    public override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        var statusBarHeight: Int = 0
        val resourceId: Int = getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId)
        }
        val actionBarHeight: Int = getSupportActionBar()!!.getHeight()
        val topLevelLayout: ViewGroup = findViewById(R.id.main_navhost)
        topLevelLayout.setPadding(0, actionBarHeight + statusBarHeight, 0, 0)
        getSupportActionBar()!!.hide()
    }
}
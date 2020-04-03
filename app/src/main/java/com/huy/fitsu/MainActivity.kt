package com.huy.fitsu

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_act.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        setupNavigationDrawer()

        setSupportActionBar(toolbar)

        setupNavigation()

    }

    private fun setupNavigationDrawer() {
        drawer_layout.setStatusBarBackground(R.color.colorPrimaryDark)
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.categoriesFragment
        )
            .setOpenableLayout(drawer_layout)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    private fun setupBottomNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.categoriesFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottom_navigation.setupWithNavController(navController)
    }

    private fun showHideBottomNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.categoriesFragment -> showBottomNavigation()
                R.id.addEditCategoryFragment -> hideBottomNavigation()
            }
        }
    }

    private fun showBottomNavigation() {
        controlBottomNavigationVisibility(View.VISIBLE)
    }

    private fun hideBottomNavigation() {
        controlBottomNavigationVisibility(View.GONE)
    }

    private fun controlBottomNavigationVisibility(visibility: Int) {
        bottom_bar.visibility = visibility
        add_transactions_fab.visibility = visibility
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

package com.example.foodies

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
private lateinit var mnavcont:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_FULLSCREEN
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        mnavcont = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_notifications, R.id.navigation_dashboard, R.id.navigation_home
            )
        )
        setupActionBarWithNavController(mnavcont, appBarConfiguration)
        navView.setupWithNavController(mnavcont)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mnavcont,null)
    }
    fun hidebnav(){
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration=300
binding.navView.visibility=View.GONE
    }
    fun showbnav(){
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration=300
        binding.navView.visibility=View.VISIBLE
    }
}
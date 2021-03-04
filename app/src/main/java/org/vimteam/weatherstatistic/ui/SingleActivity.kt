package org.vimteam.weatherstatistic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import org.koin.android.ext.android.bind
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.databinding.ActivitySingleBinding
import org.vimteam.weatherstatistic.ui.interfaces.LoadState

class SingleActivity : AppCompatActivity(), LoadState {

    private lateinit var binding: ActivitySingleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController,appBarConfiguration)
    }

    override fun setLoadState(state: Boolean) {
        binding.loadingFrameLayout.visibility = if (state) View.VISIBLE else View.GONE
    }
}
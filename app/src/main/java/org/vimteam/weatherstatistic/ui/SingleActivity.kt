package org.vimteam.weatherstatistic.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import org.koin.android.ext.android.inject
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.base.ConnectivityListener
import org.vimteam.weatherstatistic.databinding.ActivitySingleBinding
import org.vimteam.weatherstatistic.ui.interfaces.LoadState

class SingleActivity : AppCompatActivity(), LoadState {

    private lateinit var binding: ActivitySingleBinding

    private val connectivityListener: ConnectivityListener by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        connectivityListener.isConnected.observe(this) {
            val networkState = if (it) getString(R.string.network_available) else getString(R.string.network_unavailable)
            Toast.makeText(this, networkState, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setLoadState(state: Boolean) {
        binding.loadingFrameLayout.visibility = if (state) View.VISIBLE else View.GONE
    }
}
package com.example.chatapp.view

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.chatapp.R
import com.example.chatapp.utils.StorageManager
import com.example.chatapp.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    @Inject
    lateinit var storageManager: StorageManager

    private val mViewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
        mViewModel.showProgress.observe(this) {
            it.getContentIfNotHandled()?.let { isVisible ->
                findViewById<ProgressBar>(R.id.progressBar).isVisible = isVisible
            }
        }
        val destination = if (storageManager.getToken() != "") {
            R.id.fragmentHomeMain
        } else {
            R.id.fragmentAuthMain
        }
        navGraph.startDestination = destination
        navController.graph = navGraph
    }
}
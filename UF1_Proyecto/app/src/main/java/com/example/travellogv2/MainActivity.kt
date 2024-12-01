package com.example.travellogv2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular NavController al FragmentContainerView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Configurar destinos de nivel superior (me dice que appBarConfiguration no está siendo utilizada)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(R.id.homeFragment, R.id.viajesFragment, R.id.anadirViajeFragment, R.id.viajesPendientesFragment)
//        )

        // Configurar el menú inferior con NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Asegurar que al seleccionar un destino principal se reinicie correctamente
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.viajesFragment -> {
                    navController.navigate(R.id.viajesFragment)
                    true
                }
                R.id.anadirViajeFragment -> {
                    navController.navigate(R.id.anadirViajeFragment)
                    true
                }
                R.id.viajesPendientesFragment -> {
                    navController.navigate(R.id.viajesPendientesFragment)
                    true
                }
                else -> false
            }
        }
    }
}




/* Código funcional 2024-11-24

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtén el NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Configura el BottomNavigationView con el NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)
    }
}


 */

/* Código anterior funcional

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

  super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}*/
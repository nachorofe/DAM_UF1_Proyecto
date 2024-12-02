package com.example.travellogv2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Llamamos al método que inicializa la actividad
        setContentView(R.layout.activity_main) // Definimos la vista de la actividad principal

        // Vincular NavController al FragmentContainerView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment // Asociamos el Fragment_Container_View del activity_main al controlador de navegación a través de su id
        val navController = navHostFragment.navController // Asociamos la navegación al fragment

        // Configurar el menú inferior con NavController, con el id definido en activity_main
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController) // Vinculamos el menú de navegación con el controlador de navegación

        // Asegurar que al seleccionar un destino principal se reinicie correctamente
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
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
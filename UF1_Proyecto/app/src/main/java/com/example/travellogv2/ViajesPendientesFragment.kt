package com.example.travellogv2

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travellogv2.database.ViajeDatabase
import com.example.travellogv2.database.entity.ViajePendiente
import kotlinx.coroutines.launch
import java.util.Locale

class ViajesPendientesFragment : Fragment() {

    private lateinit var database: ViajeDatabase
    private lateinit var adapter: ViajesPendientesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_viajes_pendientes, container, false)

        // Inicializar la base de datos
        database = ViajeDatabase.getDatabase(requireContext())

        // Configurar el RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rvViajesPendientes)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inicializar el adaptador
        adapter = ViajesPendientesAdapter(emptyList()) { viajeId ->
            mostrarConfirmacionEliminar(viajeId)
        }
        recyclerView.adapter = adapter

        // Configurar el botón y el campo de texto
        val etNuevoViajePendiente = view.findViewById<EditText>(R.id.etNuevoViajePendiente)
        val btnAgregarViajePendiente = view.findViewById<Button>(R.id.btnAgregarViajePendiente)

        btnAgregarViajePendiente.setOnClickListener {
            val nuevoViaje = etNuevoViajePendiente.text.toString().trim()
            if (nuevoViaje.isNotEmpty()) {
                agregarViajePendiente(nuevoViaje)
                etNuevoViajePendiente.text.clear() // Limpiar el campo de texto
            } else {
                Toast.makeText(requireContext(), "Por favor, introduce un lugar", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar los viajes pendientes
        cargarViajesPendientes()

        return view
    }

    private fun cargarViajesPendientes() {
        lifecycleScope.launch {
            val viajesPendientes = database.viajeDao().obtenerViajesPendientes()
            adapter.actualizarDatos(viajesPendientes) // Cargamos toda la lista de viajes
        }
    }

    private fun agregarViajePendiente(viaje: String) {
        lifecycleScope.launch {
            // Convertir el nombre del viaje a minúsculas solo para la comparación insensible al caso
            val viajeEnMinusculas = viaje.trim().lowercase(Locale.getDefault()) // Convertir a minúsculas para la comprobación

            // Comprobar si ya existe un viaje pendiente con el mismo nombre (sin distinguir mayúsculas/minúsculas)
            val viajeExistente = database.viajeDao().obtenerViajePendientePorNombre(viajeEnMinusculas)

            if (viajeExistente != null) {
                // Si existe, mostrar mensaje sin hacer ninguna modificación
                Toast.makeText(requireContext(), "Este viaje pendiente ya está en la lista", Toast.LENGTH_SHORT).show()
            } else {
                // Si no existe, añadimos un nuevo viaje pendiente
                // Insertamos el viaje tal y como lo introdujo el usuario (sin modificar mayúsculas/minúsculas)
                database.viajeDao().insertarViajePendiente(ViajePendiente(viaje = viaje.trim())) // Se mantiene el formato original
                Toast.makeText(requireContext(), "Viaje pendiente añadido", Toast.LENGTH_SHORT).show()
            }

            // Actualizar la lista de viajes pendientes
            cargarViajesPendientes()
        }
    }

    private fun mostrarConfirmacionEliminar(viajeId: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage("¿Estás seguro de que quieres eliminar este viaje pendiente?")
            .setPositiveButton("Sí") { _, _ ->
                eliminarViajePendiente(viajeId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun eliminarViajePendiente(viajeId: Int) {
        lifecycleScope.launch {
            database.viajeDao().eliminarViajePendientePorId(viajeId)
            Toast.makeText(requireContext(), "Viaje pendiente eliminado", Toast.LENGTH_SHORT).show()
            cargarViajesPendientes() // Actualizar la lista después de eliminar
        }
    }
}





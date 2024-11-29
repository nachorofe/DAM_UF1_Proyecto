package com.example.travellogv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travellogv2.database.ViajeDatabase
import kotlinx.coroutines.launch

class ViajesFragment : Fragment() {

    private lateinit var database: ViajeDatabase
    private lateinit var adapter: ViajesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_viajes, container, false)

        // Configurar la base de datos
        database = ViajeDatabase.getDatabase(requireContext())

        // Configurar RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rvViajes)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Configurar adaptador con callback para navegar
        adapter = ViajesAdapter(emptyList()) { viajeId ->
            val action = ViajesFragmentDirections.actionViajesFragmentToViajeFragment(viajeId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter

        // Cargar datos de la base de datos
        cargarViajes()

        return view
    }

    private fun cargarViajes() {
        lifecycleScope.launch {
            val viajes = database.viajeDao().obtenerTodosLosViajes()
            if (viajes.isNotEmpty()) {
                adapter.actualizarDatos(viajes) // Actualizar el adaptador con la lista de viajes
            }
        }
    }
}




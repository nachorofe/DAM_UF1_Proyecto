package com.example.travellogv2

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ViajeFragment : Fragment() {

    private var viajeId: Int = 0 // Variable para almacenar el ID del viaje

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_viaje, container, false)

        // Obtener el argumento viajeId desde Safe Args
        viajeId = ViajeFragmentArgs.fromBundle(requireArguments()).viajeId

        // Aquí puedes usar viajeId para cargar los datos del viaje
        cargarViaje(view)

        return view
    }

    private fun cargarViaje(view: View) {
        val lugarTextView = view.findViewById<TextView>(R.id.tvLugar)
        val descripcionTextView = view.findViewById<TextView>(R.id.tvDescripcion)
        val fotoImageView = view.findViewById<ImageView>(R.id.ivFoto)

        lifecycleScope.launch {
            val database = ViajeDatabase.getDatabase(requireContext())
            val viaje = database.viajeDao().obtenerViajePorId(viajeId)

            // Mostrar los datos del viaje en el layout
            if (viaje != null) {
                lugarTextView.text = viaje.lugar
                descripcionTextView.text = viaje.descripcion

                if (viaje.foto.isNotEmpty()) {
                    fotoImageView.setImageURI(Uri.parse(viaje.foto))
                } else {
                    fotoImageView.setImageResource(R.drawable.fotopordefecto) // Imagen predeterminada
                }
            } else {
                lugarTextView.text = "Viaje no encontrado"
                descripcionTextView.text = ""
                fotoImageView.setImageResource(R.drawable.fotopordefecto) // Imagen predeterminada
            }
        }
    }

}

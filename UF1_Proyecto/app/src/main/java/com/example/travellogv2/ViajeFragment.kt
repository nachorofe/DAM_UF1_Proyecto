package com.example.travellogv2

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.travellogv2.database.ViajeDatabase
import kotlinx.coroutines.launch
import java.io.File

class ViajeFragment : Fragment() {

    private var viajeId: Int = 0 // Variable para almacenar el ID del viaje

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_viaje, container, false)

        // Obtenemos el argumento viajeId desde Safe Args
        viajeId = ViajeFragmentArgs.fromBundle(requireArguments()).viajeId

        // Cargamos el viaje
        cargarViaje(view)

        // Configurar el botón de eliminar
        val btnEliminarViaje = view.findViewById<Button>(R.id.btnEliminarViaje)
        btnEliminarViaje.setOnClickListener {
            // Mostrar la confirmación antes de eliminar el viaje
            mostrarConfirmacionEliminar()
        }
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
                lugarTextView.text = getString(R.string.txtViajeNoEncontrado)
                descripcionTextView.text = ""
                fotoImageView.setImageResource(R.drawable.fotopordefecto) // Imagen predeterminada
            }
        }
    }

    // Función para mostrar la confirmación antes de eliminar el viaje
    private fun mostrarConfirmacionEliminar() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.txtConfirmacionEliminacion))
            .setPositiveButton(getString(R.string.txtSi)) { _, _ ->
                eliminarViaje() // Llamamos a la función para eliminar el viaje
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun eliminarViaje() {
        // lifecycleScope es una extensión de Jetpack que crea una corutina (asíncrona) para la tarea que envuelve, permitiendo que se siga ejecutando el fragmento y cancelándose automáticamente si la actividad es destruida
        lifecycleScope.launch {
            // Obtener la referencia de la base de datos
            val database = ViajeDatabase.getDatabase(requireContext())

            // Obtener el viaje desde la base de datos usando su ID
            val viaje = database.viajeDao().obtenerViajePorId(viajeId)
            if (viaje != null) {
                // Eliminar el viaje usando el ID
                database.viajeDao().eliminarViaje(viajeId)  // Aquí pasas el ID del viaje

                // Eliminar la foto si existe en el almacenamiento
                val fotoPath = viaje.foto
                if (fotoPath.isNotEmpty()) {
                    val file = File(fotoPath)
                    if (file.exists()) {
                        file.delete() // Eliminar la foto físicamente
                    }
                }

                // Mostrar mensaje y regresar a la pantalla anterior
                Toast.makeText(requireContext(),getString(R.string.txtViajeEliminado), Toast.LENGTH_SHORT).show()
                val navController = findNavController()
                navController.popBackStack()
            }
        }
    }

}
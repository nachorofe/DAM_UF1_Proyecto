package com.example.travellogv2

import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.travellogv2.database.ViajeDatabase
import com.example.travellogv2.database.entity.Viaje
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AnadirViajeFragment : Fragment() {

    private lateinit var database: ViajeDatabase
    private var fotoUri: Uri? = null // Guardará la URI temporal de la foto

    // Obtenemos la foto (registerForActivityResult viene a reemplazar a startActivityForResult y onActivityResult)
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            fotoUri = it // Guardamos la URI de la imagen seleccionada

            // Actualizamos el botón de foto con la nueva información
            val btnFoto = view?.findViewById<Button>(R.id.btnFoto)
            btnFoto?.text = getString(R.string.foto_anadida)

            // Aplica colores personalizados al botón
            try {
                btnFoto?.setBackgroundColor(resources.getColor(R.color.teal_200, null)) // Cambia el color de fondo
                btnFoto?.setTextColor(resources.getColor(android.R.color.white, null)) // Cambia el color del texto
            } catch (e: Resources.NotFoundException) {
                Log.e("AnadirViajeFragment", "El recurso de color no se encontró: ${e.message}")
                btnFoto?.setBackgroundColor(Color.LTGRAY) // Color predeterminado
                btnFoto?.setTextColor(Color.BLACK) // Color predeterminado
            }

            Log.d("AnadirViajeFragment", "Foto seleccionada: $fotoUri")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anadir_viaje, container, false)

        database = ViajeDatabase.getDatabase(requireContext())

        val etLugar = view.findViewById<EditText>(R.id.etLugar)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcion)
        val btnFoto = view.findViewById<Button>(R.id.btnFoto)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        // Botón para seleccionar una foto de la galería
        btnFoto.setOnClickListener {
            abrirGaleria()
        }

        // Botón para guardar el viaje
        btnGuardar.setOnClickListener {
            val lugar = etLugar.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            if (lugar.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.validacion), Toast.LENGTH_SHORT).show()
            } else {
                // Guardar el viaje, con la foto si está disponible
                val rutaFoto = fotoUri?.let { guardarFotoInternamente(it) } ?: ""
                guardarViaje(lugar, descripcion, rutaFoto)
            }
        }

        return view
    }

    private fun abrirGaleria() {
        pickImageLauncher.launch("image/*") // Abre el selector de contenido para imágenes
    }

    private fun guardarFotoInternamente(uri: Uri): String {
        val fileName = "foto_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)

        try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

//            Log.d("AnadirViajeFragment", "Foto guardada internamente en: ${file.absolutePath}")
            return file.absolutePath
        } catch (e: Exception) {
            Log.e("AnadirViajeFragment", "Error al guardar la foto: ${e.message}")
            return ""
        }
    }

    private fun guardarViaje(lugar: String, descripcion: String, foto: String) {
        val viaje = Viaje(lugar = lugar, descripcion = descripcion, foto = foto)
        lifecycleScope.launch {
            database.viajeDao().insertarViaje(viaje)
            Toast.makeText(requireContext(), "Viaje guardado correctamente", Toast.LENGTH_SHORT).show()
/*            mostrarUltimoViaje() */
        }
    }

}



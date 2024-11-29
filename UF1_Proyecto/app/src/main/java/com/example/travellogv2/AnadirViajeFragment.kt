package com.example.travellogv2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AnadirViajeFragment : Fragment() {

    private lateinit var database: ViajeDatabase
    private var fotoUri: Uri? = null // Guardará la URI temporal de la foto

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            fotoUri = it // Guarda la URI de la imagen seleccionada

            // Actualiza el botón de foto con la nueva información
            val btnFoto = view?.findViewById<Button>(R.id.btnFoto1)
            btnFoto?.text = "Foto añadida. Pulsa para cambiar"

            // Aplica colores personalizados al botón
            try {
                btnFoto?.setBackgroundColor(resources.getColor(R.color.teal_200, null)) // Cambia el color de fondo
                btnFoto?.setTextColor(resources.getColor(android.R.color.white, null)) // Cambia el color del texto
            } catch (e: Resources.NotFoundException) {
                Log.e("AnadirViajeFragment", "El recurso de color no se encontró: ${e.message}")
                btnFoto?.setBackgroundColor(Color.LTGRAY) // Color predeterminado
                btnFoto?.setTextColor(Color.BLACK) // Color predeterminado
            }

            Toast.makeText(requireContext(), "Foto seleccionada: $fotoUri", Toast.LENGTH_SHORT).show()
            Log.d("AnadirViajeFragment", "Foto seleccionada: $fotoUri")
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anadir_viaje, container, false)

        database = ViajeDatabase.getDatabase(requireContext())

        val etLugar = view.findViewById<EditText>(R.id.etLugar)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcion)
        val btnFoto = view.findViewById<Button>(R.id.btnFoto1)
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
                Toast.makeText(requireContext(), "Lugar y descripción son obligatorios", Toast.LENGTH_SHORT).show()
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

            Log.d("AnadirViajeFragment", "Foto guardada internamente en: ${file.absolutePath}")
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
            mostrarUltimoViaje()
        }
    }

    private fun mostrarUltimoViaje() {
        lifecycleScope.launch {
            val viajes = database.viajeDao().obtenerTodosLosViajes()
            if (viajes.isNotEmpty()) {
                val ultimoViaje = viajes.last()
                Log.d("UltimoViaje", "Lugar: ${ultimoViaje.lugar}, Descripción: ${ultimoViaje.descripcion}, Foto: ${ultimoViaje.foto}")
                Toast.makeText(
                    requireContext(),
                    "Último viaje - Lugar: ${ultimoViaje.lugar}, Descripción: ${ultimoViaje.descripcion}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Log.d("UltimoViaje", "No se encontraron viajes en la base de datos.")
            }
        }
    }
}


/* Código funcional 2024-11-27 12:38
class AnadirViajeFragment : Fragment() {

    private lateinit var database: ViajeDatabase
    private var fotoUri: Uri? = null // Guardará la URI temporal de la foto

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anadir_viaje, container, false)

        database = ViajeDatabase.getDatabase(requireContext())

        val etLugar = view.findViewById<EditText>(R.id.etLugar)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcion)
        val btnFoto = view.findViewById<Button>(R.id.btnFoto1)
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
                Toast.makeText(requireContext(), "Lugar y descripción son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Guardar el viaje, con la foto si está disponible
                val rutaFoto = fotoUri?.let { guardarFotoInternamente(it) } ?: ""
                guardarViaje(lugar, descripcion, rutaFoto)
            }
        }

        return view
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            fotoUri = data.data // Guarda la URI de la foto seleccionada

            // Actualizar el botón al seleccionar una foto
            val btnFoto = view?.findViewById<Button>(R.id.btnFoto1)
            btnFoto?.text = "Foto añadida. Pulsa para cambiar"


            // Verifica que el color existe antes de aplicarlo
            try {
                btnFoto?.setBackgroundColor(resources.getColor(R.color.teal_200, null)) // Cambia el color de fondo
                btnFoto?.setTextColor(resources.getColor(android.R.color.white, null)) // Cambia el color del texto
            } catch (e: Resources.NotFoundException) {
                Log.e("AnadirViajeFragment", "El recurso de color no se encontró: ${e.message}")
                // Aplicar colores predeterminados en caso de error
                btnFoto?.setBackgroundColor(Color.LTGRAY)
                btnFoto?.setTextColor(Color.BLACK)
            }

            Toast.makeText(requireContext(), "Foto seleccionada: $fotoUri", Toast.LENGTH_SHORT).show()
            Log.d("AnadirViajeFragment", "Foto seleccionada: $fotoUri")
        }
    }


    private fun guardarFotoInternamente(uri: Uri): String {
        // Crea un archivo dentro del almacenamiento interno de la aplicación
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

            Log.d("AnadirViajeFragment", "Foto guardada internamente en: ${file.absolutePath}")
            return file.absolutePath // Devuelve la ruta del archivo
        } catch (e: Exception) {
            Log.e("AnadirViajeFragment", "Error al guardar la foto: ${e.message}")
            return ""
        }
    }

    private fun guardarViaje(lugar: String, descripcion: String, foto: String) {
        val viaje = Viaje(lugar = lugar, descripcion = descripcion, foto = foto)
        lifecycleScope.launch {
            // Insertar el viaje en la base de datos
            database.viajeDao().insertarViaje(viaje)
            Toast.makeText(requireContext(), "Viaje guardado correctamente", Toast.LENGTH_SHORT).show()

            mostrarUltimoViaje()
        }
    }

    private fun mostrarUltimoViaje() {
        lifecycleScope.launch {
            val viajes = database.viajeDao().obtenerTodosLosViajes()
            if (viajes.isNotEmpty()) {
                val ultimoViaje = viajes.last()
                Log.d("UltimoViaje", "Lugar: ${ultimoViaje.lugar}, Descripción: ${ultimoViaje.descripcion}, Foto: ${ultimoViaje.foto}")
                Toast.makeText(
                    requireContext(),
                    "Último viaje - Lugar: ${ultimoViaje.lugar}, Descripción: ${ultimoViaje.descripcion}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Log.d("UltimoViaje", "No se encontraron viajes en la base de datos.")
            }
        }
    }
} */



package com.example.travellogv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener la referencia al TextView
        val textViewViaja: TextView = view.findViewById(R.id.textViewViaja)
        val textViewSuena: TextView = view.findViewById(R.id.textViewSuena)
        val textViewDisfruta: TextView = view.findViewById(R.id.textViewDisfruta)

        // Hacer que los TextViews sean visibles
        textViewViaja.visibility = View.VISIBLE
        textViewSuena.visibility = View.VISIBLE
        textViewDisfruta.visibility = View.VISIBLE

        // Crear una animación para mover el texto desde la izquierda hasta el centro
        val translateAnimationFromLeft = TranslateAnimation(
            Animation.ABSOLUTE, -1000f,  // Desplazar desde la izquierda (fuera de la pantalla)
            Animation.ABSOLUTE, 0f,     // Terminar en el centro horizontal
            Animation.ABSOLUTE, 0f,     // Sin desplazamiento vertical
            Animation.ABSOLUTE, 0f
        )

        // Crear una animación para mover el texto desde la derecha hasta el centro
        val translateAnimationFromRight = TranslateAnimation(
            Animation.ABSOLUTE, 1000f,  // Desplazar desde la derecha (fuera de la pantalla)
            Animation.ABSOLUTE, 0f,     // Terminar en el centro horizontal
            Animation.ABSOLUTE, 0f,     // Sin desplazamiento vertical
            Animation.ABSOLUTE, 0f
        )

        // Configurar la animación
        translateAnimationFromLeft.duration = 2000  // Duración de la animación en milisegundos (2 segundos)
        translateAnimationFromLeft.fillAfter = true // Mantener la posición final de la animación

        translateAnimationFromRight.duration = 2000 // Duración de la animación en milisegundos (2 segundos)
        translateAnimationFromRight.fillAfter = true // Mantener la posición final de la animación

        // Iniciar las animaciones
        textViewViaja.startAnimation(translateAnimationFromLeft)
        Thread.sleep(2500)
        textViewSuena.startAnimation(translateAnimationFromRight)
        Thread.sleep(2500)
        textViewDisfruta.startAnimation(translateAnimationFromLeft)
    }
}




/* CÓDIGO PREVIO FUNCIONAL

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

} */
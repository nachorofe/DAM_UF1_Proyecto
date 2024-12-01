package com.example.travellogv2

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.view.View

import android.view.animation.Animation

import android.view.animation.TranslateAnimation

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
//        Thread.sleep(2500)
        textViewSuena.startAnimation(translateAnimationFromRight)
//        Thread.sleep(2500)
        textViewDisfruta.startAnimation(translateAnimationFromLeft)

        // Retrasamos 2 segundos para que aparezca la frase final del eslogan
        val textViewExperiencias = view.findViewById<TextView>(R.id.textViewExperiencias)

        val textoCompleto = getString(R.string.experiencias)

        textViewExperiencias.text = ""  // Inicialmente vacío
        textViewExperiencias.alpha = 0f  // Inicia invisible

// Fade-in para el TextView
        val fadeIn = ObjectAnimator.ofFloat(textViewExperiencias, "alpha", 0f, 1f)
        fadeIn.duration = 2000 // Duración de 2 segundos

        val handler = Handler(Looper.getMainLooper())
        val delay = 100L  // Tiempo entre letras (100ms)

        var index = 0
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (index < textoCompleto.length) {
                    textViewExperiencias.append(textoCompleto[index].toString())  // Agregar una letra a la vez
                    index++
                    handler.postDelayed(this, delay)
                }
            }
        }, 1500)  // Espera segundo y medio antes de empezar a mostrar las letras

// Inicia la animación de fade-in
        fadeIn.start()
    }
}
package com.example.travellogv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travellogv2.database.dao.ViajePendiente

class ViajesPendientesAdapter(
    private var viajes: List<ViajePendiente>,
    private val onDeleteClick: (Int) -> Unit // Callback para manejar el borrado
) : RecyclerView.Adapter<ViajesPendientesAdapter.ViajeViewHolder>() {

    inner class ViajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.tvViajePendiente)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarViajePendiente)

        fun bind(viaje: ViajePendiente) {
            textView.text = viaje.viaje

            // Acción para eliminar el viaje
            btnEliminar.setOnClickListener {
                onDeleteClick(viaje.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viaje_pendiente, parent, false)
        return ViajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        holder.bind(viajes[position])
    }

    override fun getItemCount(): Int = viajes.size

    // Método para actualizar los datos
    fun actualizarDatos(nuevosViajes: List<ViajePendiente>) {
        viajes = nuevosViajes
        notifyDataSetChanged()
    }
}



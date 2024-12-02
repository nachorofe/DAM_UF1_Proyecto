package com.example.travellogv2

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travellogv2.database.entity.Viaje

class ViajesAdapter(
    private var listaViajes: List<Viaje>,
    private val onItemClick: (Int) -> Unit // Pasar el ID del viaje seleccionado
) : RecyclerView.Adapter<ViajesAdapter.ViajeViewHolder>() {

    class ViajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivViaje: ImageView = itemView.findViewById(R.id.ivViaje)
        val tvViaje: TextView = itemView.findViewById(R.id.tvViaje)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viaje, parent, false)
        return ViajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        val viaje = listaViajes[position]

        holder.tvViaje.text = viaje.lugar

        // Si la foto está disponible, la cargamos. En caso contrario, añadimos la foto por defecto
        if (viaje.foto.isNotEmpty()) {
            holder.ivViaje.setImageURI(Uri.parse(viaje.foto))
        } else {
            holder.ivViaje.setImageResource(R.drawable.fotopordefecto) // Imagen por defecto
        }

        // Callback al hacer clic en el elemento
        holder.itemView.setOnClickListener {
            onItemClick(viaje.id) // Pasar el ID del viaje
        }
    }

    override fun getItemCount(): Int = listaViajes.size

    fun actualizarDatos(nuevosViajes: List<Viaje>) {
        listaViajes = nuevosViajes
        notifyDataSetChanged()
    }
}




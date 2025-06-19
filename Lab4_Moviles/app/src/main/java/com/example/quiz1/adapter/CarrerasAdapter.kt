// src/main/java/com/example/quiz1/adapter/CarrerasAdapter.kt
package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionacademicaapp.model.Carrera
import com.example.quiz1.R

class CarrerasAdapter(private val items: List<Carrera>) :
    RecyclerView.Adapter<CarrerasAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val line1: TextView = itemView.findViewById(R.id.tvLine1)
        private val line2: TextView = itemView.findViewById(R.id.tvLine2)
        private val line3: TextView = itemView.findViewById(R.id.tvLine3)

        fun bind(carrera: Carrera) {
            line1.text = "Nombre: ${carrera.nombre}"
            line2.text = "Código: ${carrera.codigo}"
            line3.text = "Título: ${carrera.titulo}"

            itemView.findViewById<TextView>(R.id.tvLine4)?.visibility = View.GONE
            itemView.findViewById<TextView>(R.id.tvLine5)?.visibility = View.GONE
            itemView.findViewById<TextView>(R.id.tvLine6)?.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_default, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

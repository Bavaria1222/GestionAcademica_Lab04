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

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNombre: TextView = v.findViewById(R.id.tvCarreraNombre)
        val tvCodigo: TextView = v.findViewById(R.id.tvCarreraCodigo)
        val tvTitulo:  TextView = v.findViewById(R.id.tvCarreraTitulo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.carrera_item, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c = items[position]
        holder.tvNombre.text = "Nombre: ${c.nombre}"
        holder.tvCodigo.text = "Código: ${c.codigo}"
        holder.tvTitulo.text  = "Título: ${c.titulo}"
    }
}

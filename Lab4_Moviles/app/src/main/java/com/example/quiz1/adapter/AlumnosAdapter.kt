package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionacademicaapp.model.Alumno
import com.example.quiz1.R

class AlumnosAdapter(
    private val listaOriginal: MutableList<Alumno>,
    private val onItemClick: (Alumno) -> Unit
) : RecyclerView.Adapter<AlumnosAdapter.ViewHolder>(), Filterable {

    // Esta es la que realmente se dibuja
    private var listaFiltrada: MutableList<Alumno> = listaOriginal.toMutableList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCedula: TextView          = itemView.findViewById(R.id.tvCedula)
        val tvNombre: TextView          = itemView.findViewById(R.id.tvNombre)
        val tvTelefono: TextView        = itemView.findViewById(R.id.tvTelefono)
        val tvEmail: TextView           = itemView.findViewById(R.id.tvEmail)
        val tvFechaNacimiento: TextView = itemView.findViewById(R.id.tvFechaNacimiento)
        val tvCarrera: TextView         = itemView.findViewById(R.id.tvCarrera)

        fun bind(alumno: Alumno) {
            tvCedula.text          = "Cédula: ${alumno.cedula}"
            tvNombre.text          = "Nombre: ${alumno.nombre}"
            tvTelefono.text        = "Teléfono: ${alumno.telefono}"
            tvEmail.text           = "Email: ${alumno.email}"
            tvFechaNacimiento.text = "Nacimiento: ${alumno.fechaNacimiento}"
            tvCarrera.text         = "Carrera ID: ${alumno.idCarrera}"

            itemView.setOnClickListener { onItemClick(alumno) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context)
            .inflate(R.layout.alumno_item, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listaFiltrada[position])
    }

    override fun getItemCount(): Int = listaFiltrada.size

    /** Llamar para vaciar y recargar TODO el adapter con una nueva lista */
    fun setData(nuevaLista: List<Alumno>) {
        listaOriginal.clear()
        listaOriginal.addAll(nuevaLista)
        listaFiltrada = listaOriginal.toMutableList()
        notifyDataSetChanged()
    }

    /** Swipe to delete */
    fun eliminarItem(pos: Int) {
        listaOriginal.removeAt(pos)
        listaFiltrada = listaOriginal.toMutableList()
        notifyItemRemoved(pos)
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val txt = constraint?.toString()?.lowercase() ?: ""
            val filtered = if (txt.isEmpty()) {
                listaOriginal
            } else {
                listaOriginal.filter {
                    it.cedula.lowercase().contains(txt) ||
                            it.nombre.lowercase().contains(txt) ||
                            it.email.lowercase().contains(txt)
                }
            }
            return FilterResults().apply { values = filtered }
        }
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            listaFiltrada = (results?.values as? List<Alumno>)?.toMutableList()
                ?: mutableListOf()
            notifyDataSetChanged()
        }
    }
}

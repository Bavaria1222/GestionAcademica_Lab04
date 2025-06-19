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
        private val line1: TextView = itemView.findViewById(R.id.tvLine1)
        private val line2: TextView = itemView.findViewById(R.id.tvLine2)
        private val line3: TextView = itemView.findViewById(R.id.tvLine3)
        private val line4: TextView = itemView.findViewById(R.id.tvLine4)
        private val line5: TextView = itemView.findViewById(R.id.tvLine5)
        private val line6: TextView = itemView.findViewById(R.id.tvLine6)

        fun bind(alumno: Alumno) {
            line1.text = "Nombre: ${alumno.nombre}"
            line2.text = "Cédula: ${alumno.cedula}"
            line3.text = "Teléfono: ${alumno.telefono}"
            line4.text = "Email: ${alumno.email}"
            line5.text = "Nacimiento: ${alumno.fechaNacimiento}"
            line6.text = "Carrera ID: ${alumno.idCarrera}"

            line1.visibility = View.VISIBLE
            line2.visibility = View.VISIBLE
            line3.visibility = View.VISIBLE
            line4.visibility = View.VISIBLE
            line5.visibility = View.VISIBLE
            line6.visibility = View.VISIBLE

            itemView.setOnClickListener { onItemClick(alumno) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_default, parent, false)
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

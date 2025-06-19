package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.Profesor
import java.util.*

class ProfesoresAdapter(
    private val listaOriginal: MutableList<Profesor>,
    private val onItemClick: (Profesor) -> Unit
) : RecyclerView.Adapter<ProfesoresAdapter.ProfesorViewHolder>(), Filterable {

    // Lista que se muestra en pantalla (filtrada)
    private var listaFiltrada: MutableList<Profesor> = listaOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_default, parent, false)
        return ProfesorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfesorViewHolder, position: Int) {
        holder.bind(listaFiltrada[position])
    }

    override fun getItemCount(): Int = listaFiltrada.size

    /** Devuelve el elemento filtrado en la posición `pos`. */
    fun getItem(pos: Int): Profesor = listaFiltrada[pos]

    /** Reemplaza las listas original y filtrada con `nuevaLista` y notifica cambio. */
    fun actualizarLista(nuevaLista: List<Profesor>) {
        listaOriginal.clear()
        listaOriginal.addAll(nuevaLista)
        listaFiltrada = listaOriginal.toMutableList()
        notifyDataSetChanged()
    }

    // ========================= Filtros =========================
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val texto = constraint?.toString()?.lowercase(Locale.getDefault())?.trim() ?: ""
                val resultados: List<Profesor> = if (texto.isEmpty()) {
                    listaOriginal
                } else {
                    listaOriginal.filter { prof ->
                        prof.cedula.lowercase(Locale.getDefault()).contains(texto) ||
                                prof.nombre.lowercase(Locale.getDefault()).contains(texto) ||
                                prof.telefono.lowercase(Locale.getDefault()).contains(texto) ||
                                prof.email.lowercase(Locale.getDefault()).contains(texto)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultados
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = (results?.values as? List<Profesor>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    // ====================== ViewHolder ======================
    inner class ProfesorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val line1: TextView = itemView.findViewById(R.id.tvLine1)
        private val line2: TextView = itemView.findViewById(R.id.tvLine2)
        private val line3: TextView = itemView.findViewById(R.id.tvLine3)
        private val line4: TextView = itemView.findViewById(R.id.tvLine4)
        private val line5: TextView? = itemView.findViewById(R.id.tvLine5)
        private val line6: TextView? = itemView.findViewById(R.id.tvLine6)

        fun bind(profesor: Profesor) {
            line1.text = "Cédula: ${profesor.cedula}"
            line2.text = "Nombre: ${profesor.nombre}"
            line3.text = "Teléfono: ${profesor.telefono}"
            line4.text = "Email: ${profesor.email}"

            line5?.visibility = View.GONE
            line6?.visibility = View.GONE

            itemView.setOnClickListener { onItemClick(profesor) }
        }
    }
}

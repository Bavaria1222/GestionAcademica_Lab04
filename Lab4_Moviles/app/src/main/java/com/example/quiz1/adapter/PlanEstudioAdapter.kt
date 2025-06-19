package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.PlanEstudio
import java.util.*

class PlanEstudioAdapter(
    private val listaOriginal: MutableList<PlanEstudio>,
    private val onItemClick: (PlanEstudio) -> Unit
) : RecyclerView.Adapter<PlanEstudioAdapter.PlanViewHolder>(), Filterable {

    // Esta lista contendrá los elementos filtrados para mostrar en pantalla
    private var listaFiltrada: MutableList<PlanEstudio> = listaOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_default, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(listaFiltrada[position])
    }

    override fun getItemCount(): Int = listaFiltrada.size

    /**
     * Devuelve el elemento filtrado en la posición dada.
     * Útil para el swipe o para pasar el objeto a la pantalla de edición.
     */
    fun getItem(pos: Int): PlanEstudio = listaFiltrada[pos]

    /**
     * Reemplaza la lista original y la lista filtrada con la nueva lista de PlanEstudio,
     * y notifica al RecyclerView que los datos cambiaron.
     */
    fun actualizarLista(nuevaLista: List<PlanEstudio>) {
        listaOriginal.clear()
        listaOriginal.addAll(nuevaLista)
        // Cuando tengamos datos nuevos, reseteamos la lista filtrada para mostrar todo
        listaFiltrada = listaOriginal.toMutableList()
        notifyDataSetChanged()
    }

    // ================================
    // Implementación de Filterable
    // ================================
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val texto = constraint?.toString()?.lowercase(Locale.getDefault())?.trim() ?: ""

                // Si el texto está vacío, devolvemos toda la lista original
                val resultados: List<PlanEstudio> = if (texto.isEmpty()) {
                    listaOriginal
                } else {
                    listaOriginal.filter { plan ->
                        // Filtrar por cualquiera de estos campos:
                        plan.idPlanEstudio.toString().contains(texto) ||
                                plan.idCarrera.toString().contains(texto) ||
                                plan.idCurso.toString().contains(texto) ||
                                plan.anio.toString().contains(texto) ||
                                plan.ciclo.toString().contains(texto)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = resultados
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = (results?.values as? List<PlanEstudio>)?.toMutableList()
                    ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    // ================================
    // ViewHolder interno
    // ================================
    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val line1: TextView = itemView.findViewById(R.id.tvLine1)
        private val line2: TextView = itemView.findViewById(R.id.tvLine2)
        private val line3: TextView = itemView.findViewById(R.id.tvLine3)
        private val line4: TextView = itemView.findViewById(R.id.tvLine4)
        private val line5: TextView = itemView.findViewById(R.id.tvLine5)

        fun bind(plan: PlanEstudio) {
            line1.text = "ID: ${plan.idPlanEstudio}"
            line2.text = "Carrera: ${plan.idCarrera}"
            line3.text = "Curso: ${plan.idCurso}"
            line4.text = "Año: ${plan.anio}"
            line5.text = "Ciclo: ${plan.ciclo}"

            itemView.findViewById<TextView>(R.id.tvLine6)?.visibility = View.GONE

            itemView.setOnClickListener { onItemClick(plan) }
        }
    }
}

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
            .inflate(R.layout.item_plan_estudio, parent, false)
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
        private val tvIdPlan: TextView      = itemView.findViewById(R.id.tvIdPlan)
        private val tvCarreraPlan: TextView = itemView.findViewById(R.id.tvCarreraPlan)
        private val tvCursoPlan: TextView   = itemView.findViewById(R.id.tvCursoPlan)
        private val tvAnioPlan: TextView    = itemView.findViewById(R.id.tvAnioPlan)
        private val tvCicloPlan: TextView   = itemView.findViewById(R.id.tvCicloPlan)

        fun bind(plan: PlanEstudio) {
            tvIdPlan.text = "ID: ${plan.idPlanEstudio}"
            tvCarreraPlan.text = "Carrera: ${plan.idCarrera}"
            tvCursoPlan.text = "Curso: ${plan.idCurso}"
            tvAnioPlan.text = "Año: ${plan.anio}"
            tvCicloPlan.text = "Ciclo: ${plan.ciclo}"

            itemView.setOnClickListener {
                onItemClick(plan)
            }
        }
    }
}

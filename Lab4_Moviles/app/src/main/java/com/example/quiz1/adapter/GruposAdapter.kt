package com.example.quiz1.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.Grupo

class GruposAdapter(private val originalList: List<Grupo>) :
    RecyclerView.Adapter<GruposAdapter.GrupoVH>(), android.widget.Filterable {

    private var filteredList = originalList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrupoVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_default, parent, false)
        return GrupoVH(v)
    }
    override fun onBindViewHolder(holder: GrupoVH, position: Int) {
        holder.bind(filteredList[position])
    }
    override fun getItemCount() = filteredList.size

    inner class GrupoVH(item: View) : RecyclerView.ViewHolder(item) {
        private val line1: TextView = item.findViewById(R.id.tvLine1)
        private val line2: TextView = item.findViewById(R.id.tvLine2)
        private val line3: TextView = item.findViewById(R.id.tvLine3)
        private val line4: TextView = item.findViewById(R.id.tvLine4)
        private val line5: TextView = item.findViewById(R.id.tvLine5)
        private val line6: TextView = item.findViewById(R.id.tvLine6)

        fun bind(g: Grupo) {
            line1.text = "ID: ${g.idGrupo}"
            line2.text = "Ciclo: ${g.idCiclo}"
            line3.text = "Curso: ${g.idCurso}"
            line4.text = "Grupo #${g.numGrupo}"
            line5.text = g.horario
            line6.text = "Profesor: ${g.idProfesor}"
        }
    }

    override fun getFilter() = object: android.widget.Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val text = constraint?.toString()?.lowercase()?.trim() ?: ""
            val res = if (text.isEmpty()) {
                originalList
            } else {
                originalList.filter {
                    it.horario.lowercase().contains(text)
                            || it.idProfesor.lowercase().contains(text)
                }
            }
            return FilterResults().apply { values = res }
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = (results?.values as? List<Grupo>)?.toMutableList() ?: mutableListOf()
            notifyDataSetChanged()
        }
    }
}

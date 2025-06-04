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
            .inflate(R.layout.item_grupo, parent, false)
        return GrupoVH(v)
    }
    override fun onBindViewHolder(holder: GrupoVH, position: Int) {
        holder.bind(filteredList[position])
    }
    override fun getItemCount() = filteredList.size

    inner class GrupoVH(item: View) : RecyclerView.ViewHolder(item) {
        private val tvId      : TextView = item.findViewById(R.id.tvGrupoId)
        private val tvCiclo   : TextView = item.findViewById(R.id.tvGrupoCiclo)
        private val tvCurso   : TextView = item.findViewById(R.id.tvGrupoCurso)
        private val tvNum     : TextView = item.findViewById(R.id.tvGrupoNum)
        private val tvHorario : TextView = item.findViewById(R.id.tvGrupoHorario)
        private val tvProf    : TextView = item.findViewById(R.id.tvGrupoProfesor)

        fun bind(g: Grupo) {
            tvId.text      = "ID: ${g.idGrupo}"
            tvCiclo.text   = "Ciclo: ${g.idCiclo}"
            tvCurso.text   = "Curso: ${g.idCurso}"
            tvNum.text     = "Grupo #${g.numGrupo}"
            tvHorario.text = g.horario
            tvProf.text    = "Profesor: ${g.idProfesor}"
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

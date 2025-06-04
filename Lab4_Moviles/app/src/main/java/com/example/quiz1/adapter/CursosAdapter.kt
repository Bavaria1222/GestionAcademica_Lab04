package com.example.quiz1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.Curso
import java.util.*

class CursosAdapter(
    private val cursosOriginal: MutableList<Curso>,
    private val onItemClick: (Curso) -> Unit
) : RecyclerView.Adapter<CursosAdapter.CursoViewHolder>(), Filterable {

    private var cursosFiltrados = cursosOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curso, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        holder.bind(cursosFiltrados[position])
    }

    override fun getItemCount(): Int = cursosFiltrados.size

    fun getItem(position: Int): Curso = cursosFiltrados[position]

    fun actualizarLista(nuevaLista: List<Curso>) {
        cursosOriginal.clear()
        cursosOriginal.addAll(nuevaLista)
        cursosFiltrados = nuevaLista.toMutableList()
        Log.d("CursosAdapter", "Lista actualizada con ${nuevaLista.size} cursos")
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val texto = query?.toString()?.lowercase(Locale.getDefault()) ?: ""
                val resultados = if (texto.isEmpty()) {
                    cursosOriginal
                } else {
                    cursosOriginal.filter {
                        it.nombre.lowercase(Locale.getDefault()).contains(texto) ||
                                it.codigo.lowercase(Locale.getDefault()).contains(texto)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultados
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                cursosFiltrados = (results?.values as? List<Curso>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    inner class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIdCurso: TextView = itemView.findViewById(R.id.tvIdCurso)
        private val tvCodigoCurso: TextView = itemView.findViewById(R.id.tvCodigoCurso)
        private val tvNombreCurso: TextView = itemView.findViewById(R.id.tvNombreCurso)
        private val tvCreditosCurso: TextView = itemView.findViewById(R.id.tvCreditosCurso)
        private val tvHorasSemanales: TextView = itemView.findViewById(R.id.tvHorasSemanales)

        fun bind(curso: Curso) {
            tvIdCurso.text = "ID: ${curso.idCurso}"
            tvCodigoCurso.text = "Código: ${curso.codigo}"
            tvNombreCurso.text = "Nombre: ${curso.nombre}"
            tvCreditosCurso.text = "Créditos: ${curso.creditos}"
            tvHorasSemanales.text = "Horas semanales: ${curso.horasSemanales}"
            itemView.setOnClickListener { onItemClick(curso) }
        }
    }
}

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
            .inflate(R.layout.item_card_default, parent, false)
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
        private val line1: TextView = itemView.findViewById(R.id.tvLine1)
        private val line2: TextView = itemView.findViewById(R.id.tvLine2)
        private val line3: TextView = itemView.findViewById(R.id.tvLine3)
        private val line4: TextView = itemView.findViewById(R.id.tvLine4)
        private val line5: TextView = itemView.findViewById(R.id.tvLine5)

        fun bind(curso: Curso) {
            line1.text = "ID: ${curso.idCurso}"
            line2.text = "Código: ${curso.codigo}"
            line3.text = "Nombre: ${curso.nombre}"
            line4.text = "Créditos: ${curso.creditos}"
            line5.text = "Horas semanales: ${curso.horasSemanales}"

            itemView.findViewById<TextView>(R.id.tvLine6)?.visibility = View.GONE
            itemView.setOnClickListener { onItemClick(curso) }
        }
    }
}

package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.Matricula
import java.util.*
import kotlin.collections.ArrayList

class MatriculasAdapter(private val listaOriginal: MutableList<Matricula>) :
    RecyclerView.Adapter<MatriculasAdapter.MatriculaViewHolder>(), Filterable {

    private var listaFiltrada: MutableList<Matricula> = listaOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatriculaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_matricula, parent, false)
        return MatriculaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatriculaViewHolder, position: Int) {
        holder.bind(listaFiltrada[position])
    }

    override fun getItemCount(): Int = listaFiltrada.size

    fun getItem(pos: Int): Matricula = listaFiltrada[pos]

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val texto = query?.toString()?.lowercase()?.trim() ?: ""
                val resultados = if (texto.isEmpty()) {
                    listaOriginal
                } else {
                    listaOriginal.filter {
                        it.cedulaAlumno.lowercase(Locale.getDefault()).contains(texto) ||
                                it.idGrupo.toString().contains(texto) ||
                                it.idMatricula.toString().contains(texto)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultados
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = (results?.values as? List<Matricula>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    fun actualizarLista(nuevaLista: List<Matricula>) {
        listaOriginal.clear()
        listaOriginal.addAll(nuevaLista)
        listaFiltrada = nuevaLista.toMutableList()
        notifyDataSetChanged()
    }

    class MatriculaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIdMatricula: TextView = itemView.findViewById(R.id.tvIdMatricula)
        private val tvCedulaAlumno: TextView = itemView.findViewById(R.id.tvCedulaAlumno)
        private val tvIdGrupo: TextView = itemView.findViewById(R.id.tvIdGrupo)
        private val tvNota: TextView = itemView.findViewById(R.id.tvNota)

        fun bind(matricula: Matricula) {
            tvIdMatricula.text = "ID Matrícula: ${matricula.idMatricula}"
            tvCedulaAlumno.text = "Cédula: ${matricula.cedulaAlumno}"
            tvIdGrupo.text = "Grupo: ${matricula.idGrupo}"
            tvNota.text = "Nota: ${matricula.nota ?: "Sin nota"}"
        }
    }
}

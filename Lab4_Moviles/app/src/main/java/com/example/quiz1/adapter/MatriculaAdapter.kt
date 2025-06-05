package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.Matricula
import java.util.*

class MatriculaAdapter(
    private val matriculas: MutableList<Matricula>,
    private val onItemClick: (Matricula) -> Unit
) : RecyclerView.Adapter<MatriculaAdapter.MatriculaViewHolder>(), Filterable {

    private var matriculasFiltradas: MutableList<Matricula> = matriculas.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatriculaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_matricula, parent, false)
        return MatriculaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatriculaViewHolder, position: Int) {
        Log.d("MatriculaAdapter", "Renderizando posición $position con ${matriculasFiltradas[position].cedulaAlumno}")
        holder.bind(matriculasFiltradas[position])
    }

    override fun getItemCount(): Int = matriculasFiltradas.size

    fun getItem(pos: Int): Matricula = matriculasFiltradas[pos]

    fun actualizarLista(nuevaLista: List<Matricula>) {
        Log.d("MatriculaAdapter", "Actualizar lista con ${nuevaLista.size} elementos")
        matriculas.clear()
        matriculas.addAll(nuevaLista)
        matriculasFiltradas = nuevaLista.toMutableList()
        notifyDataSetChanged()
        Log.d("MatriculaAdapter", "Items en adapter: ${itemCount}")
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val texto = query?.toString()?.lowercase(Locale.getDefault()) ?: ""
                val resultado = if (texto.isEmpty()) {
                    matriculas
                } else {
                    matriculas.filter {
                        it.cedulaAlumno.lowercase(Locale.getDefault()).contains(texto) ||
                                it.idGrupo.toString().contains(texto) ||
                                it.idMatricula.toString().contains(texto)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultado
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                matriculasFiltradas = (results?.values as? List<Matricula>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    inner class MatriculaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIdMatricula: TextView = itemView.findViewById(R.id.tvIdMatricula)
        private val tvCedulaAlumno: TextView = itemView.findViewById(R.id.tvCedulaAlumno)
        private val tvIdGrupo: TextView = itemView.findViewById(R.id.tvIdGrupo)
        private val tvNota: TextView = itemView.findViewById(R.id.tvNota)

        fun bind(matricula: Matricula) {
            Log.d("MatriculaAdapter", "Bind matricula ID: ${matricula.idMatricula}, Alumno: ${matricula.cedulaAlumno}")
            tvIdMatricula.text = "ID: ${matricula.idMatricula}"
            tvCedulaAlumno.text = "Alumno: ${matricula.cedulaAlumno}"
            tvIdGrupo.text = "Grupo: ${matricula.idGrupo}"
            tvNota.text = if (matricula.nota != null) {
                // Mostramos la nota con dos decimales para mayor claridad
                "Nota: %.2f".format(Locale.US, matricula.nota)
            } else {
                "Nota: Sin nota"
            }

            itemView.setOnClickListener {
                onItemClick(matricula)
            }
        }
    }
}

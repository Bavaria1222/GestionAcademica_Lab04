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
import com.example.quiz1.model.Matricula
import java.util.*

class MatriculaAdapter(
    itemsIniciales: List<Matricula>,
    private val onItemClick: (Matricula) -> Unit
) : RecyclerView.Adapter<MatriculaAdapter.MatriculaViewHolder>(), Filterable {

    // ------------------------------------------------------------------------------------------------
    // En lugar de mantener una referencia directa a la lista del Fragmento,
    // copiamos los datos entrantes a dos listas independientes:
    //
    //  • matriculas: lista “completa” (sin filtrar) que el adaptador controla internamente.
    //  • matriculasFiltradas: lista que efectivamente se dibuja en pantalla (filtrada o no).
    // ------------------------------------------------------------------------------------------------
    private val matriculas: MutableList<Matricula> = itemsIniciales.toMutableList()
    private var matriculasFiltradas: MutableList<Matricula> = itemsIniciales.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatriculaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_matricula, parent, false)
        return MatriculaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatriculaViewHolder, position: Int) {
        // Mostramos un log para cada bind que se ejecute, así podemos verificar
        // si realmente se están “dibujando” los ítems en el RecyclerView.
        Log.d(
            "MatriculaAdapter",
            "Renderizando posición $position con ${matriculasFiltradas[position].cedulaAlumno}"
        )
        holder.bind(matriculasFiltradas[position])
    }

    override fun getItemCount(): Int = matriculasFiltradas.size

    fun getItem(pos: Int): Matricula = matriculasFiltradas[pos]

    /**
     * Actualiza la lista completa del adaptador (sin filtrar) y, a la vez,
     * repuebla la lista filtrada con todos los elementos de nuevaLista.
     *
     * IMPORTANTE: NO tocamos la lista del Fragmento, sino copiamos aquí los datos.
     */
    fun actualizarLista(nuevaLista: List<Matricula>) {
        Log.d("MatriculaAdapter", "Actualizar lista con ${nuevaLista.size} elementos")

        // 1) Reemplazamos la “lista completa” interna:
        matriculas.clear()
        matriculas.addAll(nuevaLista)

        // 2) Repoblamos la lista filtrada para que muestre TODO (sin filtro aplicado):
        matriculasFiltradas.clear()
        matriculasFiltradas.addAll(nuevaLista)

        // 3) Notificamos cambio de datos:
        notifyDataSetChanged()

        // 4) Verificación final: revisamos cuántos ítems hay en la lista filtrada
        Log.d("MatriculaAdapter", "Items en adapter (filtrados): $itemCount")
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val texto = query?.toString()?.lowercase(Locale.getDefault()) ?: ""
                val resultado: List<Matricula> = if (texto.isEmpty()) {
                    matriculas
                } else {
                    matriculas.filter {
                        it.cedulaAlumno.lowercase(Locale.getDefault()).contains(texto) ||
                                it.idGrupo.toString().contains(texto) ||
                                it.idMatricula.toString().contains(texto)
                    }
                }
                return FilterResults().apply {
                    values = resultado
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
            ) {
                matriculasFiltradas = (results?.values as? List<Matricula>)?.toMutableList()
                    ?: mutableListOf()
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
            Log.d(
                "MatriculaAdapter",
                "Bind matricula ID: ${matricula.idMatricula}, Alumno: ${matricula.cedulaAlumno}"
            )
            tvIdMatricula.text = "ID: ${matricula.idMatricula}"
            tvCedulaAlumno.text = "Alumno: ${matricula.cedulaAlumno}"
            tvIdGrupo.text = "Grupo: ${matricula.idGrupo}"
            tvNota.text = if (matricula.nota != null) {
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

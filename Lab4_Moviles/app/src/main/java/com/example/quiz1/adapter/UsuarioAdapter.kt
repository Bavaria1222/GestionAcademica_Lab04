// app/src/main/java/com/example/quiz1/adapter/UsuarioAdapter.kt
package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.Usuario
import java.util.*

class UsuarioAdapter(
    private val listaOriginal: MutableList<Usuario>
) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder>(), Filterable {

    // Esta es la copia que realmente se dibuja en pantalla
    private var listaFiltrada: MutableList<Usuario> = listaOriginal.toMutableList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val line1: TextView = itemView.findViewById(R.id.tvLine1)
        private val line2: TextView = itemView.findViewById(R.id.tvLine2)
        private val line3: TextView = itemView.findViewById(R.id.tvLine3)

        fun bind(usuario: Usuario) {
            line1.text = "Cédula: ${usuario.cedula}"
            line2.text = "Clave: ${usuario.clave}"
            line3.text = "Rol: ${usuario.rol}"

            itemView.findViewById<TextView>(R.id.tvLine4)?.visibility = View.GONE
            itemView.findViewById<TextView>(R.id.tvLine5)?.visibility = View.GONE
            itemView.findViewById<TextView>(R.id.tvLine6)?.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_default, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listaFiltrada[position])
    }

    override fun getItemCount(): Int = listaFiltrada.size

    /**  Para que el fragment pueda obtener el elemento swipado o pulsado  */
    fun getItem(position: Int): Usuario = listaFiltrada[position]

    /**
     * Este método lo llamamos desde el fragment cuando recibimos respuesta de la API.
     * Limpia la lista “original” y la vuelve a llenar con los registros recibidos,
     * asigna esa misma lista a `listaFiltrada` y dispara notifyDataSetChanged().
     */
    fun actualizarLista(nuevaLista: List<Usuario>) {
        listaOriginal.clear()
        listaOriginal.addAll(nuevaLista)
        listaFiltrada = nuevaLista.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val texto = query?.toString()?.lowercase(Locale.getDefault())?.trim() ?: ""
                val resultados: List<Usuario> = if (texto.isEmpty()) {
                    listaOriginal
                } else {
                    listaOriginal.filter { usuario ->
                        usuario.cedula.lowercase().contains(texto) ||
                                usuario.rol.lowercase().contains(texto)
                    }
                }
                return FilterResults().apply { values = resultados }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = (results?.values as? List<Usuario>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}

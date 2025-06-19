package com.example.quiz1.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionacademicaapp.model.Ciclo
import com.example.quiz1.R

class CiclosAdapter(private var listaOriginal: MutableList<Ciclo>) :
    RecyclerView.Adapter<CiclosAdapter.ViewHolder>(), Filterable {

    private var listaFiltrada = listaOriginal.toMutableList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val line1: TextView = itemView.findViewById(R.id.tvLine1)
        private val line2: TextView = itemView.findViewById(R.id.tvLine2)
        private val line3: TextView = itemView.findViewById(R.id.tvLine3)
        private val line4: TextView = itemView.findViewById(R.id.tvLine4)
        private val line5: TextView = itemView.findViewById(R.id.tvLine5)

        fun bind(c: Ciclo) {
            line1.text = "ID: ${c.idCiclo}"
            line2.text = "Año: ${c.anio}"
            line3.text = "Ciclo: ${c.numero}"
            line4.text = "Inicio: ${c.fechaInicio}"
            line5.text = "Fin: ${c.fechaFin}"

            itemView.findViewById<TextView>(R.id.tvLine6)?.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) = ViewHolder(
        LayoutInflater.from(p.context).inflate(R.layout.item_card_default, p, false)
    )

    override fun getItemCount() = listaFiltrada.size

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        h.bind(listaFiltrada[pos])
    }

    fun agregar(c: Ciclo) {
        listaOriginal.add(c)
        listaFiltrada = listaOriginal.toMutableList()
        notifyItemInserted(listaFiltrada.lastIndex)
    }
    fun eliminar(pos: Int) {
        listaOriginal.removeAt(pos)
        listaFiltrada = listaOriginal.toMutableList()
        notifyItemRemoved(pos)
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(q: CharSequence?) = FilterResults().apply {
            val t = q?.toString()?.lowercase() ?: ""
            values = if (t.isEmpty()) listaOriginal
            else listaOriginal.filter {
                it.anio.toString().contains(t)
                        || it.numero.toString().contains(t)
            }
        }
        override fun publishResults(q: CharSequence?, r: FilterResults?) {
            listaFiltrada = (r?.values as? List<Ciclo>)?.toMutableList() ?: mutableListOf()
            notifyDataSetChanged()
        }
    }
}

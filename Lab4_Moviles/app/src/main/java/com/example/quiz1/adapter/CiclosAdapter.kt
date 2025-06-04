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

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvId: TextView = v.findViewById(R.id.tvId)
        val tvAnio: TextView = v.findViewById(R.id.tvAnio)
        val tvNumero: TextView = v.findViewById(R.id.tvNumero)
        val tvInicio: TextView = v.findViewById(R.id.tvFechaInicio)
        val tvFin: TextView = v.findViewById(R.id.tvFechaFin)
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) = ViewHolder(
        LayoutInflater.from(p.context).inflate(R.layout.ciclo_item, p, false)
    )

    override fun getItemCount() = listaFiltrada.size

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val c = listaFiltrada[pos]
        h.tvId.text = "ID: ${c.idCiclo}"
        h.tvAnio.text = "Año: ${c.anio}"
        h.tvNumero.text = "Ciclo: ${c.numero}"
        h.tvInicio.text = "Inicio: ${c.fechaInicio}"
        h.tvFin.text = "Fin: ${c.fechaFin}"
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

package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.model.Matricula

class NotasAdapter(
    private val matriculas: MutableList<Matricula>,
    private val onGuardar: (Matricula, Float?) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.bind(matriculas[position])
    }

    override fun getItemCount(): Int = matriculas.size

    fun actualizarLista(nueva: List<Matricula>) {
        matriculas.clear()
        matriculas.addAll(nueva)
        notifyDataSetChanged()
    }

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCedula: TextView = itemView.findViewById(R.id.tvNotaCedula)
        private val edtNota: EditText = itemView.findViewById(R.id.edtNotaValor)
        private val btnGuardar: Button = itemView.findViewById(R.id.btnGuardarNota)

        fun bind(matricula: Matricula) {
            tvCedula.text = "Alumno: ${matricula.cedulaAlumno}"
            edtNota.setText(matricula.nota?.toString() ?: "")
            btnGuardar.setOnClickListener {
                val valor = edtNota.text.toString().toFloatOrNull()
                onGuardar(matricula, valor)
            }
        }
    }
}


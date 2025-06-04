package com.example.quiz1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R

class MenuAdapter(
    private val items: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOpcionMenu: TextView = itemView.findViewById(R.id.tvOpcionMenu)
        val icono: ImageView = itemView.findViewById(R.id.icono)

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.tvOpcionMenu.text = item

        // Asignar íconos según el nombre del menú
        when (item.lowercase()) {
            "alumnos" -> holder.icono.setImageResource(R.drawable.ic_person)
            "profesores" -> holder.icono.setImageResource(R.drawable.ic_person)
            "usuarios" -> holder.icono.setImageResource(R.drawable.ic_account_circle)
            "carreras" -> holder.icono.setImageResource(R.drawable.ic_school)
            "cursos" -> holder.icono.setImageResource(R.drawable.ic_menu_book)
            "ciclos" -> holder.icono.setImageResource(R.drawable.ic_school)
            "grupos" -> holder.icono.setImageResource(R.drawable.ic_school)
            "matrículas" -> holder.icono.setImageResource(R.drawable.ic_school)
            "plan de estudio" -> holder.icono.setImageResource(R.drawable.ic_school)
            "historial" -> holder.icono.setImageResource(R.drawable.ic_menu_book)
            "registro notas" -> holder.icono.setImageResource(R.drawable.ic_menu_book)
            else -> holder.icono.setImageResource(R.drawable.ic_school)
        }
    }

    override fun getItemCount(): Int = items.size
}

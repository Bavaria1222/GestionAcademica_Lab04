package com.example.quiz1.activity

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.adapter.MenuAdapter
import com.example.quiz1.fragment.*

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private val items = mutableListOf<Pair<String, androidx.fragment.app.Fragment>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        val cedula = prefs.getString("cedula", "")
        // El rol puede venir con espacios o en minúsculas.
        // Lo normalizamos a mayúsculas para evitar que el menú quede vacío.
        val rol = prefs.getString("rol", "")?.trim()?.uppercase()
        findViewById<TextView>(R.id.tvBienvenida).text = "Bienvenido, $cedula"

        // Configurar botón de cerrar sesión
        val btnLogout = findViewById<TextView>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseás cerrar sesión?")
                .setPositiveButton("Sí") { _, _ ->
                    prefs.edit().clear().apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        items.addAll(obtenerItemsPorRol(rol))

        val recyclerView = findViewById<RecyclerView>(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MenuAdapter(items.map { it.first }) { pos ->
            mostrarFragment(items[pos].second)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (items.isNotEmpty()) {
            mostrarFragment(items[0].second)
        }
    }

    private fun mostrarFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragmento, fragment)
            .commit()
    }

    private fun obtenerItemsPorRol(rol: String?): List<Pair<String, androidx.fragment.app.Fragment>> {
        return when (rol?.uppercase()) {
            "ADMINISTRADOR" -> listOf(
                "Alumnos" to AlumnosFragment(),
                "Profesores" to ProfesoresFragment(),
                "Usuarios" to UsuariosFragment(),
                "Carreras" to CarrerasFragment(),
                "Cursos" to CursosFragment(),
                "Ciclos" to CiclosFragment(),
                "Grupos" to GruposFragment(),
                "Matrículas" to MatriculaFragment(),
                "Plan de Estudio" to PlanEstudioFragment()
            )
            "MATRICULADOR" -> listOf("Matrículas" to MatriculaFragment())
            "PROFESOR" -> listOf(
                "Registro Notas" to RegistroNotasFragment()
            )
            "ALUMNO" -> listOf(
                "Historial" to HistorialFragment()
            )
            else -> emptyList()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}

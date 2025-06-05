package com.example.quiz1.activity.alumnoActivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.adapter.MatriculaAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.MatriculaApi
import com.example.quiz1.model.Matricula
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialAlumnoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatriculaAdapter
    private val lista = mutableListOf<Matricula>()
    private val api = ApiClient.retrofit.create(MatriculaApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_alumno)

        recyclerView = findViewById(R.id.recyclerViewHistorial)
        adapter = MatriculaAdapter(lista) { }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val cedula = intent.getStringExtra("cedula") ?: return
        cargarHistorial(cedula)
    }

    private fun cargarHistorial(cedula: String) {
        api.listarPorAlumno(cedula).enqueue(object : Callback<List<Matricula>> {
            override fun onResponse(call: Call<List<Matricula>>, response: Response<List<Matricula>>) {
                if (response.isSuccessful) {
                    lista.clear()
                    lista.addAll(response.body() ?: emptyList())
                    adapter.actualizarLista(lista)
                } else {
                    Toast.makeText(this@HistorialAlumnoActivity, "Error al cargar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Matricula>>, t: Throwable) {
                Toast.makeText(this@HistorialAlumnoActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

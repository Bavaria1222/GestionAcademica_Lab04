package com.example.quiz1.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.MatriculaApi
import com.example.quiz1.adapter.MatriculaAdapter
import com.example.quiz1.model.Matricula
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatriculaAdapter
    private val lista = mutableListOf<Matricula>()
    private val api = ApiClient.retrofit.create(MatriculaApi::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewHistorial)
        adapter = MatriculaAdapter(lista) { }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val prefs = requireActivity().getSharedPreferences("datos_usuario", MODE_PRIVATE)
        prefs.getString("cedula", null)?.let { cargarHistorial(it) }

        return view
    }

    private fun cargarHistorial(cedula: String) {
        api.listarPorAlumno(cedula).enqueue(object : Callback<List<Matricula>> {
            override fun onResponse(
                call: Call<List<Matricula>>,
                response: Response<List<Matricula>>
            ) {
                if (response.isSuccessful) {
                    val datos = response.body() ?: emptyList()
                    lista.clear()
                    lista.addAll(datos)
                    adapter.actualizarLista(datos)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Matricula>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


package com.example.quiz1.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionacademicaapp.model.Ciclo
import com.example.quiz1.R
import com.example.quiz1.activity.notaActivity.RegistrarNotaActivity
import com.example.quiz1.adapter.MatriculaAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.CicloApi
import com.example.quiz1.api.GrupoApi
import com.example.quiz1.api.MatriculaApi
import com.example.quiz1.model.Grupo
import com.example.quiz1.model.Matricula
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroNotasFragment : Fragment() {

    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatriculaAdapter
    private lateinit var launcherEditar: ActivityResultLauncher<Intent>

    private val listaGrupos = mutableListOf<Grupo>()
    private val listaMatriculas = mutableListOf<Matricula>()
    private val apiGrupo = ApiClient.retrofit.create(GrupoApi::class.java)
    private val apiMatricula = ApiClient.retrofit.create(MatriculaApi::class.java)
    private val apiCiclo = ApiClient.retrofit.create(CicloApi::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_registro_notas, container, false)
        spinner = view.findViewById(R.id.spinnerGrupos)
        recyclerView = view.findViewById(R.id.recyclerViewNotas)

        adapter = MatriculaAdapter(listaMatriculas) { matricula ->
            val intent = Intent(requireContext(), RegistrarNotaActivity::class.java)
            intent.putExtra("matricula", matricula)
            launcherEditar.launch(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        launcherEditar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                cargarMatriculas()
            }
        }

        cargarGruposProfesor()
        return view
    }

    private fun cargarGruposProfesor() {
        val prefs = requireActivity().getSharedPreferences("datos_usuario", MODE_PRIVATE)
        val cedula = prefs.getString("cedula", null) ?: return
        apiCiclo.listar().enqueue(object : Callback<List<Ciclo>> {
            override fun onResponse(call: Call<List<Ciclo>>, response: Response<List<Ciclo>>) {
                if (response.isSuccessful) {
                    val ciclos = response.body() ?: emptyList()
                    val cicloActual = ciclos.maxByOrNull { it.idCiclo }?.idCiclo ?: return
                    apiGrupo.listarPorProfesor(cedula, cicloActual).enqueue(object : Callback<List<Grupo>> {
                        override fun onResponse(call: Call<List<Grupo>>, response: Response<List<Grupo>>) {
                            if (response.isSuccessful) {
                                listaGrupos.clear()
                                listaGrupos.addAll(response.body() ?: emptyList())
                                val nombres = listaGrupos.map { "Grupo ${it.numGrupo} - Curso ${it.idCurso}" }
                                spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres).also {
                                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                }
                                spinner.setSelection(0, false)
                                spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                                        cargarMatriculas()
                                    }
                                    override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
                                })
                                if (listaGrupos.isNotEmpty()) cargarMatriculas()
                            } else {
                                Toast.makeText(requireContext(), "Error al cargar grupos", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<Grupo>>, t: Throwable) {
                            Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<List<Ciclo>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarMatriculas() {
        val pos = spinner.selectedItemPosition
        if (pos == android.widget.AdapterView.INVALID_POSITION || listaGrupos.isEmpty()) return
        val idGrupo = listaGrupos[pos].idGrupo
        apiMatricula.listarPorGrupo(idGrupo).enqueue(object : Callback<List<Matricula>> {
            override fun onResponse(call: Call<List<Matricula>>, response: Response<List<Matricula>>) {
                if (response.isSuccessful) {
                    listaMatriculas.clear()
                    listaMatriculas.addAll(response.body() ?: emptyList())
                    adapter.actualizarLista(listaMatriculas)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar matrículas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Matricula>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

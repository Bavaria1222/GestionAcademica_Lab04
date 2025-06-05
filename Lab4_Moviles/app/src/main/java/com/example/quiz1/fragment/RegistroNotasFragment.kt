package com.example.quiz1.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.quiz1.R
import com.example.quiz1.activity.notaActivity.RegistrarNotaActivity
import com.example.quiz1.adapter.MatriculaAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.GrupoApi
import com.example.quiz1.api.MatriculaApi
import com.example.quiz1.api.CursoApi
import com.example.quiz1.api.CicloApi
import com.example.quiz1.util.Constantes
import com.example.quiz1.model.Grupo
import com.example.quiz1.model.Matricula
import com.example.quiz1.model.Curso
import com.example.gestionacademicaapp.model.Ciclo
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
    private val apiCurso = ApiClient.retrofit.create(CursoApi::class.java)
    private val apiCiclo = ApiClient.retrofit.create(CicloApi::class.java)

    private var cursosMap: Map<Int, Curso> = emptyMap()
    private var ciclosMap: Map<Int, Ciclo> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("RegistroNotas", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_registro_notas, container, false)

        spinner = view.findViewById(R.id.spinnerGrupos)
        recyclerView = view.findViewById(R.id.recyclerViewNotas)

        // Inicializamos el adapter con una copia vacía de listaMatriculas
        adapter = MatriculaAdapter(listaMatriculas) { matricula ->
            val intent = Intent(requireContext(), RegistrarNotaActivity::class.java).apply {
                putExtra("matricula", matricula)
            }
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

    override fun onResume() {
        super.onResume()
        // Al volver al fragmento recargamos los grupos del profesor para
        // asegurar que la lista de notas esté actualizada
        Log.d("RegistroNotas", "onResume")
        cargarGruposProfesor()
    }

    private fun cargarGruposProfesor() {
        val prefs = requireActivity().getSharedPreferences("datos_usuario", MODE_PRIVATE)
        val cedula = prefs.getString("cedula", null) ?: return

        Log.d("RegistroNotas", "Cargando grupos para profesor $cedula")
        apiGrupo.listarPorProfesor(cedula, Constantes.CICLO_ACTUAL)
            .enqueue(object : Callback<List<Grupo>> {
                override fun onResponse(
                    call: Call<List<Grupo>>,
                    response: Response<List<Grupo>>
                ) {
                    if (response.isSuccessful) {
                        listaGrupos.clear()
                        listaGrupos.addAll(
                            response.body()
                                ?.filter { it.idProfesor == cedula }
                                ?: emptyList()
                        )

                        Log.d("RegistroNotas", "Grupos recibidos: ${listaGrupos.size}")

                        if (listaGrupos.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "No hay grupos asignados",
                                Toast.LENGTH_SHORT
                            ).show()
                            spinner.adapter = null
                            adapter.actualizarLista(emptyList())
                            return
                        }

                        cargarCursosYCiclos()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar grupos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Grupo>>, t: Throwable) {
                    Log.d("RegistroNotas", "Error grupos: ${t.message}")
                    Toast.makeText(
                        requireContext(),
                        "Fallo: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun cargarMatriculas() {
        val pos = spinner.selectedItemPosition
        if (pos == android.widget.AdapterView.INVALID_POSITION || listaGrupos.isEmpty()) return

        val idGrupo = listaGrupos[pos].idGrupo
        Log.d("RegistroNotas", "Cargar matrículas grupo $idGrupo")

        apiMatricula.listarPorGrupo(idGrupo)
            .enqueue(object : Callback<List<Matricula>> {
                override fun onResponse(
                    call: Call<List<Matricula>>,
                    response: Response<List<Matricula>>
                ) {
                    if (response.isSuccessful) {
                        listaMatriculas.clear()
                        listaMatriculas.addAll(response.body() ?: emptyList())

                        Log.d(
                            "RegistroNotas",
                            "Matrículas cargadas: ${listaMatriculas.size}"
                        )

                        // Llamamos a actualizarLista con la COPIA de la lista del fragmento
                        adapter.actualizarLista(listaMatriculas)
                        Log.d(
                            "RegistroNotas",
                            "ItemCount después de actualizar: ${adapter.itemCount}"
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar matrículas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Matricula>>, t: Throwable) {
                    Log.d("RegistroNotas", "Error matriculas: ${t.message}")
                    Toast.makeText(
                        requireContext(),
                        "Fallo: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun cargarCursosYCiclos() {
        Log.d("RegistroNotas", "Cargar cursos")
        apiCurso.listar()
            .enqueue(object : Callback<List<Curso>> {
                override fun onResponse(
                    call: Call<List<Curso>>,
                    response: Response<List<Curso>>
                ) {
                    if (response.isSuccessful) {
                        cursosMap = response.body()?.associateBy { it.idCurso } ?: emptyMap()
                        Log.d("RegistroNotas", "Cursos cargados: ${cursosMap.size}")
                        cargarCiclos()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar cursos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Curso>>, t: Throwable) {
                    Log.d("RegistroNotas", "Error cursos: ${t.message}")
                    Toast.makeText(
                        requireContext(),
                        "Fallo: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun cargarCiclos() {
        Log.d("RegistroNotas", "Cargar ciclos")
        apiCiclo.listar()
            .enqueue(object : Callback<List<Ciclo>> {
                override fun onResponse(
                    call: Call<List<Ciclo>>,
                    response: Response<List<Ciclo>>
                ) {
                    if (response.isSuccessful) {
                        ciclosMap = response.body()?.associateBy { it.idCiclo } ?: emptyMap()
                        Log.d("RegistroNotas", "Ciclos cargados: ${ciclosMap.size}")
                        actualizarSpinner()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar ciclos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Ciclo>>, t: Throwable) {
                    Log.d("RegistroNotas", "Error ciclos: ${t.message}")
                    Toast.makeText(
                        requireContext(),
                        "Fallo: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun actualizarSpinner() {
        val nombres = listaGrupos.map { grupo ->
            val nombreCurso = cursosMap[grupo.idCurso]?.nombre ?: "Curso ${grupo.idCurso}"
            val anio = ciclosMap[grupo.idCiclo]?.anio?.toString() ?: grupo.idCiclo.toString()
            "Grupo ${grupo.numGrupo} - $nombreCurso - Año $anio"
        }

        Log.d("RegistroNotas", "Actualizar spinner con ${nombres.size} grupos")
        spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            nombres
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner.setSelection(0, false)
        spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                cargarMatriculas()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }

        // Carga inicial de matrículas para el primer elemento
        cargarMatriculas()
    }
}

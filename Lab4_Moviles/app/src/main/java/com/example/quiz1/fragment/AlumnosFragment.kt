package com.example.quiz1.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.gestionacademicaapp.model.Alumno
import com.example.quiz1.R
import com.example.quiz1.activity.alumnoActivity.EditarAlumnoActivity
import com.example.quiz1.activity.alumnoActivity.InsertarAlumnoActivity
import com.example.quiz1.activity.alumnoActivity.HistorialAlumnoActivity
import com.example.quiz1.adapter.AlumnosAdapter
import com.example.quiz1.api.AlumnoApi
import com.example.quiz1.api.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlumnosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: AlumnosAdapter
    private lateinit var fab: FloatingActionButton

    private val listaAlumnos = mutableListOf<Alumno>()
    private val api = ApiClient.retrofit.create(AlumnoApi::class.java)

    // Lanzadores para recibir RESULT_OK y recargar
    private val launcherInsertar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) cargarAlumnosDesdeApi()
    }
    private val launcherEditar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) cargarAlumnosDesdeApi()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_alumnos, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewAlumnos)
        searchView    = view.findViewById(R.id.searchViewAlumnos)
        fab           = view.findViewById(R.id.fabAlumnos)

        adapter = AlumnosAdapter(listaAlumnos) { alumno ->
            val intent = Intent(requireContext(), HistorialAlumnoActivity::class.java)
            intent.putExtra("cedula", alumno.cedula)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // primer carga
        cargarAlumnosDesdeApi()

        // filtro
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // swipe left = delete, right = edit
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos    = vh.adapterPosition
                val alumno = listaAlumnos[pos]
                if (dir == ItemTouchHelper.LEFT) {
                    eliminarAlumnoApi(alumno.cedula, pos)
                } else {
                    val intent = Intent(requireContext(), EditarAlumnoActivity::class.java)
                    intent.putExtra("alumno", alumno)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos)
                }
            }
        }).attachToRecyclerView(recyclerView)

        // FAB lanza Insertar
        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarAlumnoActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    private fun cargarAlumnosDesdeApi() {
        api.listar().enqueue(object: Callback<List<Alumno>> {
            override fun onResponse(
                call: Call<List<Alumno>>,
                response: Response<List<Alumno>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() ?: emptyList()
                    adapter.setData(body)   // <--- aquí actualizas filtro y original
                } else {
                    Toast.makeText(requireContext(),
                        "Error al cargar alumnos", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<List<Alumno>>, t: Throwable) {
                Toast.makeText(requireContext(),
                    "Error: ${t.message}", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun eliminarAlumnoApi(cedula: String, pos: Int) {
        api.eliminar(cedula).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    adapter.eliminarItem(pos)
                    Toast.makeText(requireContext(),
                        "Alumno eliminado", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(),
                        "Error al eliminar", Toast.LENGTH_SHORT
                    ).show()
                    adapter.notifyItemChanged(pos)
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(),
                    "Fallo: ${t.message}", Toast.LENGTH_LONG
                ).show()
                adapter.notifyItemChanged(pos)
            }
        })
    }
}

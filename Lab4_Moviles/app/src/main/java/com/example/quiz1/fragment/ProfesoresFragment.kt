package com.example.quiz1.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.quiz1.R
import com.example.quiz1.activity.profesorActivity.EditarProfesorActivity
import com.example.quiz1.activity.profesorActivity.InsertarProfesorActivity
import com.example.quiz1.adapter.ProfesoresAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.model.Profesor
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfesoresFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ProfesoresAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var launcherInsertar: ActivityResultLauncher<Intent>
    private lateinit var launcherEditar: ActivityResultLauncher<Intent>

    private val listaProfesores = mutableListOf<Profesor>()
    private val api = ApiClient.retrofit.create(ProfesorApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Registramos launcher para InsertarProfesorActivity
        launcherInsertar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarProfesoresDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }

        // 2) Registramos launcher para EditarProfesorActivity
        launcherEditar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarProfesoresDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profesores, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewProfesores)
        searchView   = view.findViewById(R.id.searchViewProfesores)
        fab          = view.findViewById(R.id.fabProfesores)

        // 3) Creamos adapter vacío con lista mutable
        adapter = ProfesoresAdapter(listaProfesores) { prof ->
            // Al hacer click en un ítem, abrimos EditarProfesorActivity
            val intent = Intent(requireContext(), EditarProfesorActivity::class.java)
            intent.putExtra("profesor", prof)
            launcherEditar.launch(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 4) Swipe para eliminar / editar
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                val prof = listaProfesores[pos]
                if (dir == ItemTouchHelper.LEFT) {
                    eliminarProfesorApi(prof.cedula, pos)
                } else {
                    val intent = Intent(requireContext(), EditarProfesorActivity::class.java)
                    intent.putExtra("profesor", prof)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos)
                }
            }
        }).attachToRecyclerView(recyclerView)

        // 5) Filtro en caliente
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // 6) FAB para insertar nuevo profesor
        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarProfesorActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarProfesoresDesdeApi()
    }

    private fun cargarProfesoresDesdeApi() {
        api.listar().enqueue(object : Callback<List<Profesor>> {
            override fun onResponse(call: Call<List<Profesor>>, response: Response<List<Profesor>>) {
                if (response.isSuccessful) {
                    // Reemplazo entero de lista
                    adapter.actualizarLista(response.body() ?: emptyList())
                    adapter.filter.filter("") // forzar mostrar todo tras cargar
                } else {
                    Toast.makeText(requireContext(), "Error al cargar profesores", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Profesor>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarProfesorApi(cedula: String, pos: Int) {
        api.eliminar(cedula).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listaProfesores.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    adapter.filter.filter("")
                    Toast.makeText(requireContext(), "Profesor eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemChanged(pos)
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo al eliminar: ${t.message}", Toast.LENGTH_SHORT).show()
                adapter.notifyItemChanged(pos)
            }
        })
    }
}

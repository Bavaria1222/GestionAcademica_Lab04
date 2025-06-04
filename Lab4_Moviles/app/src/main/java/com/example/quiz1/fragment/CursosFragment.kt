package com.example.quiz1.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.quiz1.R
import com.example.quiz1.activity.cursoActivity.EditarCursoActivity
import com.example.quiz1.activity.cursoActivity.InsertarCursoActivity
import com.example.quiz1.adapter.CursosAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.CursoApi
import com.example.quiz1.model.Curso
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CursosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: CursosAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var launcherInsertar: ActivityResultLauncher<Intent>
    private lateinit var launcherEditar: ActivityResultLauncher<Intent>

    private val listaCursos = mutableListOf<Curso>()
    private val api = ApiClient.retrofit.create(CursoApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcherInsertar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                cargarCursosDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }

        launcherEditar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                cargarCursosDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cursos, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCursos)
        searchView = view.findViewById(R.id.searchViewCursos)
        fab = view.findViewById(R.id.fabCursos)

        adapter = CursosAdapter(listaCursos) { curso ->
            val intent = Intent(requireContext(), EditarCursoActivity::class.java)
            intent.putExtra("curso", curso)
            launcherEditar.launch(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Swipe para eliminar o editar
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                val curso = listaCursos[pos]
                if (dir == ItemTouchHelper.LEFT) {
                    eliminarCursoApi(curso.idCurso, pos)
                } else {
                    val intent = Intent(requireContext(), EditarCursoActivity::class.java)
                    intent.putExtra("curso", curso)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos)
                }
            }
        }).attachToRecyclerView(recyclerView)

        // Búsqueda en caliente
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // Botón para insertar
        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarCursoActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarCursosDesdeApi()
    }

    private fun cargarCursosDesdeApi() {
        api.listar().enqueue(object: Callback<List<Curso>> {
            override fun onResponse(call: Call<List<Curso>>, response: Response<List<Curso>>) {
                if (response.isSuccessful) {
                    listaCursos.clear()
                    listaCursos.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                    adapter.filter.filter("") // ← reset del filtro
                    Log.d("CursosFragment", "Cursos actualizados: ${listaCursos.size}")
                } else {
                    Toast.makeText(requireContext(), "Error al cargar cursos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Curso>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarCursoApi(id: Int, pos: Int) {
        api.eliminar(id).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listaCursos.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    adapter.filter.filter("") // ← actualiza la vista filtrada
                    Toast.makeText(requireContext(), "Curso eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemChanged(pos)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                adapter.notifyItemChanged(pos)
            }
        })
    }
}

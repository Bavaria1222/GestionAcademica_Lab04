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
import com.example.quiz1.activity.matriculaActivity.EditarMatriculaActivity
import com.example.quiz1.activity.matriculaActivity.InsertarMatriculaActivity
import com.example.quiz1.adapter.MatriculaAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.MatriculaApi
import com.example.quiz1.model.Matricula
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatriculaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: MatriculaAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var launcherInsertar: ActivityResultLauncher<Intent>
    private lateinit var launcherEditar: ActivityResultLauncher<Intent>

    private val listaMatriculas = mutableListOf<Matricula>()
    private val api = ApiClient.retrofit.create(MatriculaApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcherInsertar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarMatriculasDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }

        launcherEditar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarMatriculasDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_matriculas, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMatriculas)
        searchView   = view.findViewById(R.id.searchViewMatriculas)
        fab          = view.findViewById(R.id.fabMatriculas)

        adapter = MatriculaAdapter(listaMatriculas) { }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Swipe para eliminar / editar
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                val matricula = listaMatriculas[pos]
                if (dir == ItemTouchHelper.LEFT) {
                    eliminarMatriculaApi(matricula.idMatricula, pos)
                } else {
                    val intent = Intent(requireContext(), EditarMatriculaActivity::class.java)
                    intent.putExtra("matricula", matricula)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos)
                }
            }
        }).attachToRecyclerView(recyclerView)

        // Filtro en caliente
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // Botón para insertar
        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarMatriculaActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarMatriculasDesdeApi()
    }

    private fun cargarMatriculasDesdeApi() {
        api.listar().enqueue(object: Callback<List<Matricula>> {
            override fun onResponse(call: Call<List<Matricula>>, response: Response<List<Matricula>>) {
                if (response.isSuccessful) {
                    listaMatriculas.clear()
                    listaMatriculas.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                    adapter.filter.filter("")
                } else {
                    Toast.makeText(requireContext(), "Error al cargar matrículas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Matricula>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarMatriculaApi(id: Int, pos: Int) {
        api.eliminar(id).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listaMatriculas.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    adapter.filter.filter("")
                    Toast.makeText(requireContext(), "Matrícula eliminada", Toast.LENGTH_SHORT).show()
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

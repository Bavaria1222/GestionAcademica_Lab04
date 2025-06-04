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
import com.example.quiz1.activity.grupoActivity.EditarGrupoActivity
import com.example.quiz1.activity.grupoActivity.InsertarGrupoActivity
import com.example.quiz1.adapter.GruposAdapter
import com.example.quiz1.api.GrupoApi
import com.example.quiz1.api.ApiClient
import com.example.quiz1.model.Grupo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GruposFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: GruposAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var launcherInsertar: ActivityResultLauncher<Intent>
    private lateinit var launcherEditar: ActivityResultLauncher<Intent>

    private val listaGrupos = mutableListOf<Grupo>()
    private val api = ApiClient.retrofit.create(GrupoApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Registramos ambos launchers para recargar al volver
        launcherInsertar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarGruposDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }

        launcherEditar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarGruposDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_grupos, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewGrupos)
        searchView   = view.findViewById(R.id.searchViewGrupos)
        fab          = view.findViewById(R.id.fabGrupos)

        adapter = GruposAdapter(listaGrupos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 2) Swipe para eliminar / editar
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
                val grp = listaGrupos[pos]
                if (dir == ItemTouchHelper.LEFT) {
                    eliminarGrupoApi(grp.idGrupo, pos)
                } else {
                    val intent = Intent(requireContext(), EditarGrupoActivity::class.java)
                    intent.putExtra("grupo", grp)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos)
                }
            }
        }).attachToRecyclerView(recyclerView)

        // 3) Filtro de búsqueda en caliente
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // 4) Botón “+” para insertar
        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarGrupoActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // 5) Aquí recargamos siempre que el fragment vuelve a mostrarse
        cargarGruposDesdeApi()
    }

    private fun cargarGruposDesdeApi() {
        api.listar().enqueue(object: Callback<List<Grupo>> {
            override fun onResponse(call: Call<List<Grupo>>, response: Response<List<Grupo>>) {
                if (response.isSuccessful) {
                    listaGrupos.clear()
                    listaGrupos.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                    //  ← reset del filtro para que muestre TODO
                    adapter.filter.filter("")
                } else {
                    Toast.makeText(requireContext(), "Error al cargar grupos", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Grupo>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun eliminarGrupoApi(id: Int, pos: Int) {
        api.eliminar(id).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listaGrupos.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    // ← reset del filtro tras eliminar
                    adapter.filter.filter("")
                    Toast.makeText(requireContext(), "Grupo eliminado", Toast.LENGTH_SHORT).show()
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

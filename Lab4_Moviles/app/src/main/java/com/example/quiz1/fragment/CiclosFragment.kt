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
import com.example.gestionacademicaapp.model.Ciclo
import com.example.quiz1.R
import com.example.quiz1.activity.cicloActivity.EditarCicloActivity
import com.example.quiz1.activity.cicloActivity.InsertarCicloActivity
import com.example.quiz1.adapter.CiclosAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.CicloApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CiclosFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: CiclosAdapter

    private lateinit var launcherInsertar: ActivityResultLauncher<Intent>
    private lateinit var launcherEditar: ActivityResultLauncher<Intent>

    private val listaCiclos = mutableListOf<Ciclo>()
    private val api = ApiClient.retrofit.create(CicloApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcherInsertar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) cargarCiclosDesdeApi()
        }

        launcherEditar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) cargarCiclosDesdeApi()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_ciclos, container, false)
        rv = view.findViewById(R.id.recyclerViewCiclos)
        searchView = view.findViewById(R.id.searchViewCiclos)
        fab = view.findViewById(R.id.fabCiclos)

        adapter = CiclosAdapter(listaCiclos)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // carga inicial
        cargarCiclosDesdeApi()

        // filtro
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // swipe
        val swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val ciclo = listaCiclos[pos]
                if (direction == ItemTouchHelper.LEFT) {
                    eliminarCicloApi(ciclo.idCiclo, pos)
                } else {
                    val intent = Intent(requireContext(), EditarCicloActivity::class.java)
                    intent.putExtra("ciclo", ciclo)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos)
                }
            }
        })
        swipeHelper.attachToRecyclerView(rv)

        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarCicloActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    private fun cargarCiclosDesdeApi() {
        api.listar().enqueue(object : Callback<List<Ciclo>> {
            override fun onResponse(call: Call<List<Ciclo>>, response: Response<List<Ciclo>>) {
                if (response.isSuccessful) {
                    listaCiclos.clear()
                    listaCiclos.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                    // <-- Forzamos al filtro a aplicar cadena vacía y mostrar todo:
                    adapter.filter.filter("")
                } else {
                    Toast.makeText(requireContext(), "Error al cargar ciclos", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Ciclo>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun eliminarCicloApi(id: Int, pos: Int) {
        api.eliminar(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listaCiclos.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    // Reaplicar filtro vacío para refrescar la vista sin filtros:
                    adapter.filter.filter("")
                    Toast.makeText(requireContext(), "Ciclo eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemChanged(pos)
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                adapter.notifyItemChanged(pos)
            }
        })
    }


    override fun onResume() {
        super.onResume()
        cargarCiclosDesdeApi()
    }
}

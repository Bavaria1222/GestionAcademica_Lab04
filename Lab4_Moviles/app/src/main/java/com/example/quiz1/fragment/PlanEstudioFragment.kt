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
import com.example.quiz1.activity.planEstudioActivity.EditarPlanEstudioActivity
import com.example.quiz1.activity.planEstudioActivity.InsertarPlanEstudioActivity
import com.example.quiz1.adapter.PlanEstudioAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.PlanEstudioApi
import com.example.quiz1.model.PlanEstudio
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanEstudioFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: PlanEstudioAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var launcherInsertar: ActivityResultLauncher<Intent>
    private lateinit var launcherEditar: ActivityResultLauncher<Intent>

    // Lista de planes que alimenta el adapter
    private val listaPlanes = mutableListOf<PlanEstudio>()
    private val api = ApiClient.retrofit.create(PlanEstudioApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registramos el launcher para InsertarPlanEstudio
        launcherInsertar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarPlanesDesdeApi()
            } else {
                // Si el usuario canceló, refrescamos igual la lista (por si algo cambió localmente)
                adapter.notifyDataSetChanged()
            }
        }

        // Registramos el launcher para EditarPlanEstudio
        launcherEditar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarPlanesDesdeApi()
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_plan_estudio, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewPlanes)
        searchView = view.findViewById(R.id.searchViewPlanes)
        fab = view.findViewById(R.id.fabPlanes)

        // 1) Configuramos el adapter con la lista vacía inicialmente
        adapter = PlanEstudioAdapter(listaPlanes) { plan ->
            // Cuando hacen click en un ítem, abrimos EditarPlanEstudioActivity
            val intent = Intent(requireContext(), EditarPlanEstudioActivity::class.java)
            intent.putExtra("plan", plan)
            launcherEditar.launch(intent)
        }

        // 2) RecyclerView en LinearLayoutManager vertical
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 3) Swipe para eliminar/editar: swipe izquierda = eliminar, swipe derecha = editar
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                val plan = listaPlanes[pos]

                if (dir == ItemTouchHelper.LEFT) {
                    // Si swapea a la izquierda, eliminamos
                    eliminarPlanApi(plan.idPlanEstudio, pos)
                } else {
                    // Si swapea a la derecha, abrimos pantalla de edición
                    val intent = Intent(requireContext(), EditarPlanEstudioActivity::class.java)
                    intent.putExtra("plan", plan)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos) // revertimos swipe visual
                }
            }
        }).attachToRecyclerView(recyclerView)

        // 4) SearchView: búsqueda en caliente
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // 5) FAB: abrir pantalla de inserción
        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarPlanEstudioActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Cada vez que el fragmento vuelve a mostrarse, recargamos la lista
        cargarPlanesDesdeApi()
    }

    private fun cargarPlanesDesdeApi() {
        api.listar().enqueue(object : Callback<List<PlanEstudio>> {
            override fun onResponse(call: Call<List<PlanEstudio>>, response: Response<List<PlanEstudio>>) {
                if (response.isSuccessful) {
                    // Actualizamos el adapter con el nuevo listado
                    adapter.actualizarLista(response.body() ?: emptyList())
                    adapter.filter.filter("") // forzar mostrar todo tras cargar
                } else {
                    Toast.makeText(requireContext(), "Error al cargar planes", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<PlanEstudio>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarPlanApi(id: Int, pos: Int) {
        api.eliminar(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listaPlanes.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    adapter.filter.filter("") // forzar recarga del filtro
                    Toast.makeText(requireContext(), "Plan eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemChanged(pos) // revertir swipe
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo al eliminar: ${t.message}", Toast.LENGTH_SHORT).show()
                adapter.notifyItemChanged(pos)
            }
        })
    }
}

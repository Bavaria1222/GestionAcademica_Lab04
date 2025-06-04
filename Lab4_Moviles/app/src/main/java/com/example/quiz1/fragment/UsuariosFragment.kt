// app/src/main/java/com/example/quiz1/fragment/UsuariosFragment.kt
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
import com.example.quiz1.activity.usuarioActivity.EditarUsuarioActivity
import com.example.quiz1.activity.usuarioActivity.InsertarUsuarioActivity
import com.example.quiz1.adapter.UsuarioAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.UsuarioApi
import com.example.quiz1.model.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.*

class UsuariosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView:   SearchView
    private lateinit var adapter:      UsuarioAdapter
    private lateinit var fab:          FloatingActionButton

    private lateinit var launcherInsertar: ActivityResultLauncher<Intent>
    private lateinit var launcherEditar:   ActivityResultLauncher<Intent>

    private val listaUsuarios = mutableListOf<Usuario>()
    private val api = ApiClient.retrofit.create(UsuarioApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Registrar launcher para InsertarUsuarioActivity
        launcherInsertar =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Si se insertó correctamente, recargamos la lista entera
                    cargarUsuariosDesdeApi()
                } else {
                    adapter.notifyDataSetChanged()
                }
            }

        // 2) Registrar launcher para EditarUsuarioActivity
        launcherEditar =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Si se editó correctamente, recargamos la lista
                    cargarUsuariosDesdeApi()
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_usuarios, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewUsuarios)
        searchView   = view.findViewById(R.id.searchViewUsuarios)
        fab          = view.findViewById(R.id.fabUsuarios)

        adapter = UsuarioAdapter(listaUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 3) Swipe para eliminar / editar
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
                val usuario = adapter.getItem(pos)
                if (dir == ItemTouchHelper.LEFT) {
                    eliminarUsuarioApi(usuario.cedula, pos)
                } else {
                    // Swipe derecha → abrir pantalla de edición
                    val intent = Intent(requireContext(), EditarUsuarioActivity::class.java)
                    intent.putExtra("usuario", usuario)
                    launcherEditar.launch(intent)
                    adapter.notifyItemChanged(pos) // Revertir animación si no se editó
                }
            }
        }).attachToRecyclerView(recyclerView)

        // 4) SearchView para filtrar en “caliente”
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        // 5) FAB para insertar un nuevo usuario
        fab.setOnClickListener {
            val intent = Intent(requireContext(), InsertarUsuarioActivity::class.java)
            launcherInsertar.launch(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Siempre recargamos al volver a mostrar el fragment (por si cambió algo)
        cargarUsuariosDesdeApi()
    }

    /**
     * Llama al endpoint GET /api/usuarios y, si es exitoso,
     * actualiza completamente la lista via adapter.actualizarLista(...)
     */
    private fun cargarUsuariosDesdeApi() {
        api.listar().enqueue(object: Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    adapter.actualizarLista(response.body() ?: emptyList())
                    adapter.filter.filter("")  // Forzamos que se muestre TODO (filtro "reset")
                } else {
                    Toast.makeText(requireContext(), "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Llama al endpoint DELETE /api/usuarios/{cedula}. Si es exitoso,
     * elimina el elemento de la lista local y notifica al adapter.
     */
    private fun eliminarUsuarioApi(cedula: String, pos: Int) {
        api.eliminar(cedula).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listaUsuarios.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    adapter.filter.filter("")  // Forzar reset del filtro
                    Toast.makeText(requireContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar usuario", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemChanged(pos) // revertir swipe
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                adapter.notifyItemChanged(pos)
            }
        })
    }
}

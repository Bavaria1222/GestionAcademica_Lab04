package com.example.quiz1.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.gestionacademicaapp.model.Carrera
import com.example.quiz1.R
import com.example.quiz1.activity.carreraActivity.EditarCarreraActivity
import com.example.quiz1.adapter.CarrerasAdapter
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.CarreraApi
import com.example.quiz1.activity.carreraActivity.InsertarCarreraActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarrerasFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CarrerasAdapter
    private val lista = mutableListOf<Carrera>()
    private val api = ApiClient.retrofit.create(CarreraApi::class.java)

    private val launcherInsert = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) cargarCarreras()
    }
    private val launcherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) cargarCarreras()
    }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_carreras, c, false)
        recycler = v.findViewById(R.id.recyclerCarreras)
        adapter = CarrerasAdapter(lista)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(r: RecyclerView, h: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder)=false
            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int){
                val pos = h.adapterPosition
                val carrera = lista[pos]
                if(dir==ItemTouchHelper.LEFT){
                    api.eliminar(carrera.idCarrera).enqueue(object: Callback<Void>{
                        override fun onResponse(c: Call<Void>, r: Response<Void>) {
                            if(r.isSuccessful){
                                lista.removeAt(pos)
                                adapter.notifyItemRemoved(pos)
                                Toast.makeText(requireContext(),"Carrera eliminada",Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(),"Error al eliminar",Toast.LENGTH_SHORT).show()
                                adapter.notifyItemChanged(pos)
                            }
                        }
                        override fun onFailure(c: Call<Void>, t: Throwable) {
                            Toast.makeText(requireContext(),"Fallo: ${t.message}",Toast.LENGTH_LONG).show()
                            adapter.notifyItemChanged(pos)
                        }
                    })
                } else {
                    val i = Intent(requireContext(), EditarCarreraActivity::class.java)
                    i.putExtra("carrera", carrera)
                    launcherEdit.launch(i)
                    adapter.notifyItemChanged(pos)
                }
            }
        }).attachToRecyclerView(recycler)

        v.findViewById<View>(R.id.fabCarreras).setOnClickListener {
            launcherInsert.launch(Intent(requireContext(), InsertarCarreraActivity::class.java))
        }

        cargarCarreras()
        return v
    }

    private fun cargarCarreras() {
        api.listar().enqueue(object: Callback<List<Carrera>> {
            override fun onResponse(c: Call<List<Carrera>>, r: Response<List<Carrera>>) {
                if(r.isSuccessful){
                    lista.clear()
                    lista.addAll(r.body()?:emptyList())
                    adapter.notifyDataSetChanged()
                } else Toast.makeText(requireContext(),"Error al cargar",Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(c: Call<List<Carrera>>, t: Throwable) {
                Toast.makeText(requireContext(),"Fallo: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })
    }
}

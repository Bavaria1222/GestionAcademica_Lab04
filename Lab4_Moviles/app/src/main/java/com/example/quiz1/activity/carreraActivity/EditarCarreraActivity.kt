package com.example.quiz1.activity.carreraActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionacademicaapp.model.Carrera
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.CarreraApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarCarreraActivity : AppCompatActivity() {

    private lateinit var edtCodigo: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtTitulo: EditText
    private lateinit var btnActualizar: Button
    private val api = ApiClient.retrofit.create(CarreraApi::class.java)
    private var idCarrera = 0

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_editar_carrera)
        edtCodigo = findViewById(R.id.edtCodigoCarrera)
        edtNombre = findViewById(R.id.edtNombreCarrera)
        edtTitulo = findViewById(R.id.edtTituloCarrera)
        btnActualizar = findViewById(R.id.btnActualizarCarrera)

        val c = intent.getSerializableExtra("carrera") as? Carrera
        c?.let {
            idCarrera = it.idCarrera
            edtCodigo.setText(it.codigo)
            edtNombre.setText(it.nombre)
            edtTitulo.setText(it.titulo)
        }

        btnActualizar.setOnClickListener { actualizar() }
    }

    private fun actualizar(){
        val c = Carrera(
            idCarrera,
            codigo = edtCodigo.text.toString(),
            nombre = edtNombre.text.toString(),
            titulo = edtTitulo.text.toString()
        )
        api.modificar(c).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, r: Response<Void>) {
                if(r.isSuccessful){
                    Toast.makeText(this@EditarCarreraActivity,"Carrera actualizada", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@EditarCarreraActivity,"Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EditarCarreraActivity,"Fallo: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
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

class InsertarCarreraActivity : AppCompatActivity() {

    private lateinit var edtIdCarrera: EditText
    private lateinit var edtCodigo: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtTitulo: EditText
    private lateinit var btnGuardar: Button

    private val api = ApiClient.retrofit.create(CarreraApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_carrera)

        // 1) Vincular vistas
        edtIdCarrera = findViewById(R.id.edtIdCarrera)
        edtCodigo    = findViewById(R.id.edtCodigo)
        edtNombre    = findViewById(R.id.edtNombre)
        edtTitulo    = findViewById(R.id.edtTitulo)
        btnGuardar   = findViewById(R.id.btnGuardar)

        // 2) Hacer el campo de ID no editable:
        edtIdCarrera.isEnabled = false
        edtIdCarrera.hint = "Calculando ID..."

        // 3) Cargar todas las carreras para obtener el siguiente ID
        api.listar().enqueue(object : Callback<List<Carrera>> {
            override fun onResponse(call: Call<List<Carrera>>, response: Response<List<Carrera>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    // calcula max+1
                    val siguienteId = (lista.maxOfOrNull { it.idCarrera } ?: 0) + 1
                    edtIdCarrera.setText(siguienteId.toString())
                } else {
                    Toast.makeText(this@InsertarCarreraActivity,
                        "Error al calcular ID", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Carrera>>, t: Throwable) {
                Toast.makeText(this@InsertarCarreraActivity,
                    "Fallo al obtener IDs: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        // 4) Lógica de guardar
        btnGuardar.setOnClickListener {
            val carrera = Carrera(
                idCarrera = edtIdCarrera.text.toString().toIntOrNull() ?: 0,
                codigo = edtCodigo.text.toString(),
                nombre = edtNombre.text.toString(),
                titulo = edtTitulo.text.toString()
            )
            api.insertar(carrera).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@InsertarCarreraActivity,
                            "Carrera insertada", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@InsertarCarreraActivity,
                            "Error al insertar", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@InsertarCarreraActivity,
                        "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
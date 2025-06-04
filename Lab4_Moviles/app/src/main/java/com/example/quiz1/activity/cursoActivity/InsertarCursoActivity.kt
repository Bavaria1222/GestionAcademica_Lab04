package com.example.quiz1.activity.cursoActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.CursoApi
import com.example.quiz1.model.Curso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertarCursoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_curso)

        val edtCodigo = findViewById<EditText>(R.id.edtCodigoCurso)
        val edtNombre = findViewById<EditText>(R.id.edtNombreCurso)
        val edtCreditos = findViewById<EditText>(R.id.edtCreditosCurso)
        val edtHoras = findViewById<EditText>(R.id.edtHorasCurso)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCurso)

        btnGuardar.setOnClickListener {
            val nuevoCurso = Curso(
                0,
                edtCodigo.text.toString(),
                edtNombre.text.toString(),
                edtCreditos.text.toString().toIntOrNull() ?: 0,
                edtHoras.text.toString().toIntOrNull() ?: 0
            )

            val call = ApiClient.retrofit.create(CursoApi::class.java).insertar(nuevoCurso)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(this@InsertarCursoActivity, "Curso insertado", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@InsertarCursoActivity, "Error al insertar", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

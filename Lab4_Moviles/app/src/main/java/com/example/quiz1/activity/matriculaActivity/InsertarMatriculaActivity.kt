package com.example.quiz1.activity.matriculaActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.MatriculaApi
import com.example.quiz1.model.Matricula
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertarMatriculaActivity : AppCompatActivity() {

    private lateinit var edtCedulaAlumno: EditText
    private lateinit var edtIdGrupo: EditText
    private lateinit var edtNota: EditText
    private lateinit var btnGuardar: Button

    private val api = ApiClient.retrofit.create(MatriculaApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_matricula)

        edtCedulaAlumno = findViewById(R.id.edtCedulaAlumno)
        edtIdGrupo = findViewById(R.id.edtIdGrupo)
        edtNota = findViewById(R.id.edtNota)
        btnGuardar = findViewById(R.id.btnGuardarMatricula)

        btnGuardar.setOnClickListener {
            insertarMatricula()
        }
    }

    private fun insertarMatricula() {
        val matricula = Matricula(
            idMatricula = 0,
            cedulaAlumno = edtCedulaAlumno.text.toString(),
            idGrupo = edtIdGrupo.text.toString().toIntOrNull() ?: 0,
            nota = edtNota.text.toString().toFloatOrNull()
        )

        api.insertar(matricula).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@InsertarMatriculaActivity, "Matrícula insertada correctamente", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@InsertarMatriculaActivity, "Error al insertar matrícula", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@InsertarMatriculaActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
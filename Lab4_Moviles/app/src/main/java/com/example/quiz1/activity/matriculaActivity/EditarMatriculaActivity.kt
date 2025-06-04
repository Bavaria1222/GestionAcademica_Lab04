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

class EditarMatriculaActivity : AppCompatActivity() {

    private lateinit var edtCedulaAlumno: EditText
    private lateinit var edtIdGrupo: EditText
    private lateinit var edtNota: EditText
    private lateinit var btnActualizar: Button

    private val api = ApiClient.retrofit.create(MatriculaApi::class.java)
    private var idMatricula = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_matricula)

        edtCedulaAlumno = findViewById(R.id.edtCedulaAlumno)
        edtIdGrupo = findViewById(R.id.edtIdGrupo)
        edtNota = findViewById(R.id.edtNota)
        btnActualizar = findViewById(R.id.btnActualizarMatricula)

        val matricula = intent.getSerializableExtra("matricula") as? Matricula
        matricula?.let {
            idMatricula = it.idMatricula
            edtCedulaAlumno.setText(it.cedulaAlumno)
            edtIdGrupo.setText(it.idGrupo.toString())
            edtNota.setText(it.nota?.toString() ?: "")
        }

        btnActualizar.setOnClickListener {
            val matriculaEditada = Matricula(
                idMatricula = idMatricula,
                cedulaAlumno = edtCedulaAlumno.text.toString(),
                idGrupo = edtIdGrupo.text.toString().toIntOrNull() ?: 0,
                nota = edtNota.text.toString().toFloatOrNull()
            )
            api.modificar(matriculaEditada).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarMatriculaActivity, "Matrícula actualizada", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditarMatriculaActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EditarMatriculaActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
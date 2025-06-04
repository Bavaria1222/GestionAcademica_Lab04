package com.example.quiz1.activity.notaActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.MatriculaApi
import com.example.quiz1.model.Matricula
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarNotaActivity : AppCompatActivity() {

    private lateinit var tvCedula: TextView
    private lateinit var tvGrupo: TextView
    private lateinit var edtNota: EditText
    private lateinit var btnGuardar: Button

    private val api = ApiClient.retrofit.create(MatriculaApi::class.java)
    private var matricula: Matricula? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_nota)

        tvCedula = findViewById(R.id.tvCedulaAlumno)
        tvGrupo = findViewById(R.id.tvIdGrupo)
        edtNota = findViewById(R.id.edtNota)
        btnGuardar = findViewById(R.id.btnGuardar)

        matricula = intent.getSerializableExtra("matricula") as? Matricula
        matricula?.let {
            tvCedula.text = "Alumno: ${it.cedulaAlumno}"
            tvGrupo.text = "Grupo: ${it.idGrupo}"
            edtNota.setText(it.nota?.toString() ?: "")
        }

        btnGuardar.setOnClickListener { guardarNota() }
    }

    private fun guardarNota() {
        val nota = edtNota.text.toString().toFloatOrNull()
        val mat = matricula ?: return
        val nueva = Matricula(mat.idMatricula, mat.cedulaAlumno, mat.idGrupo, nota)
        api.modificar(nueva).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegistrarNotaActivity, "Nota guardada", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@RegistrarNotaActivity, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegistrarNotaActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}

package com.example.quiz1.activity.grupoActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.GrupoApi
import com.example.quiz1.model.Grupo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarGrupoActivity : AppCompatActivity() {
    private val api = ApiClient.retrofit.create(GrupoApi::class.java)
    private var idGrupo = 0

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_editar_grupo)

        val edtCiclo   = findViewById<EditText>(R.id.edtIdCiclo)
        val edtCurso   = findViewById<EditText>(R.id.edtIdCurso)
        val edtNum     = findViewById<EditText>(R.id.edtNumGrupo)
        val edtHorario = findViewById<EditText>(R.id.edtHorario)
        val edtProf    = findViewById<EditText>(R.id.edtIdProfesor)
        val btnAct     = findViewById<Button>(R.id.btnActualizar)

        val g = intent.getSerializableExtra("grupo") as? Grupo
        g?.let {
            idGrupo = it.idGrupo
            edtCiclo.setText(it.idCiclo.toString())
            edtCurso.setText(it.idCurso.toString())
            edtNum.setText(it.numGrupo.toString())
            edtHorario.setText(it.horario)
            edtProf.setText(it.idProfesor)
        }

        btnAct.setOnClickListener {
            val actualizado = Grupo(
                idGrupo = idGrupo,
                idCiclo = edtCiclo.text.toString().toIntOrNull() ?: 0,
                idCurso = edtCurso.text.toString().toIntOrNull() ?: 0,
                numGrupo = edtNum.text.toString().toIntOrNull() ?: 0,
                horario = edtHorario.text.toString(),
                idProfesor = edtProf.text.toString()
            )
            api.modificar(actualizado).enqueue(object: Callback<Void> {
                override fun onResponse(c: Call<Void>, r: Response<Void>) {
                    if (r.isSuccessful) {
                        Toast.makeText(this@EditarGrupoActivity,
                            "Grupo actualizado", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditarGrupoActivity,
                            "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(c: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EditarGrupoActivity,
                        "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
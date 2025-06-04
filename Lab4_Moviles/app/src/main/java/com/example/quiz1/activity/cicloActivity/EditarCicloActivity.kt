package com.example.quiz1.activity.cicloActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionacademicaapp.model.Ciclo
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.CicloApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarCicloActivity : AppCompatActivity() {
    private lateinit var edtId: EditText
    private lateinit var edtAnio: EditText
    private lateinit var edtNumero: EditText
    private lateinit var edtInicio: EditText
    private lateinit var edtFin: EditText
    private lateinit var btnActualizar: Button
    private val api = ApiClient.retrofit.create(CicloApi::class.java)
    private var idCiclo = 0

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_editar_ciclo)

        edtId        = findViewById(R.id.edtIdCiclo)
        edtAnio      = findViewById(R.id.edtAnio)
        edtNumero   = findViewById(R.id.edtNumero)
        edtInicio   = findViewById(R.id.edtFechaInicio)
        edtFin        = findViewById(R.id.edtFechaFin)
        btnActualizar = findViewById(R.id.btnActualizar)

        val c = intent.getSerializableExtra("ciclo") as? Ciclo
        c?.let {
            idCiclo = it.idCiclo
            edtId.setText(it.idCiclo.toString())
            edtAnio.setText(it.anio.toString())
            edtNumero.setText(it.numero.toString())
            edtInicio.setText(it.fechaInicio)
            edtFin.setText(it.fechaFin)
        }

        btnActualizar.setOnClickListener {
            val mod = Ciclo(
                idCiclo = idCiclo,
                anio = edtAnio.text.toString().toIntOrNull() ?: 0,
                numero = edtNumero.text.toString().toIntOrNull() ?: 0,
                fechaInicio = edtInicio.text.toString(),
                fechaFin = edtFin.text.toString()
            )
            api.modificar(mod).enqueue(object: Callback<Void> {
                override fun onResponse(c: Call<Void>, r: Response<Void>) {
                    if (r.isSuccessful) {
                        Toast.makeText(this@EditarCicloActivity,"Ciclo actualizado", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK); finish()
                    } else Toast.makeText(this@EditarCicloActivity,"Error al actualizar", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EditarCicloActivity,"Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
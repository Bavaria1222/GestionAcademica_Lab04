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

class InsertarCicloActivity : AppCompatActivity() {
    private lateinit var edtAnio: EditText
    private lateinit var edtNumero: EditText
    private lateinit var edtInicio: EditText
    private lateinit var edtFin: EditText
    private lateinit var btnGuardar: Button
    private val api = ApiClient.retrofit.create(CicloApi::class.java)

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_insertar_ciclo)
        edtAnio    = findViewById(R.id.edtAnio)
        edtNumero = findViewById(R.id.edtNumero)
        edtInicio  = findViewById(R.id.edtFechaInicio)
        edtFin     = findViewById(R.id.edtFechaFin)
        btnGuardar = findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val c = Ciclo(
                anio = edtAnio.text.toString().toIntOrNull() ?: 0,
                numero = edtNumero.text.toString().toIntOrNull() ?: 0,
                fechaInicio = edtInicio.text.toString(),
                fechaFin = edtFin.text.toString()
            )
            api.insertar(c).enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, resp: Response<Void>) {
                    if (resp.isSuccessful) {
                        Toast.makeText(this@InsertarCicloActivity,"Ciclo insertado", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK); finish()
                    } else Toast.makeText(this@InsertarCicloActivity,"Error al insertar", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(c: Call<Void>, t: Throwable) {
                    Toast.makeText(this@InsertarCicloActivity,"Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
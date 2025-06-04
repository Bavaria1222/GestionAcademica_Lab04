package com.example.quiz1.activity.planEstudioActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.PlanEstudioApi
import com.example.quiz1.model.PlanEstudio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertarPlanEstudioActivity : AppCompatActivity() {

    private lateinit var etCarrera: EditText
    private lateinit var etCurso: EditText
    private lateinit var etAnio: EditText
    private lateinit var etCiclo: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_plan_estudio)

        etCarrera = findViewById(R.id.etCarrera)
        etCurso = findViewById(R.id.etCurso)
        etAnio = findViewById(R.id.etAnio)
        etCiclo = findViewById(R.id.etCiclo)
        btnGuardar = findViewById(R.id.btnGuardarPlan)

        btnGuardar.setOnClickListener {
            val nuevoPlan = PlanEstudio(
                0,
                etCarrera.text.toString().toInt(),
                etCurso.text.toString().toInt(),
                etAnio.text.toString().toInt(),
                etCiclo.text.toString().toInt()
            )

            ApiClient.retrofit.create(PlanEstudioApi::class.java).insertar(nuevoPlan)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@InsertarPlanEstudioActivity, "Insertado con éxito", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@InsertarPlanEstudioActivity, "Error al insertar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@InsertarPlanEstudioActivity, "Error de red", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}

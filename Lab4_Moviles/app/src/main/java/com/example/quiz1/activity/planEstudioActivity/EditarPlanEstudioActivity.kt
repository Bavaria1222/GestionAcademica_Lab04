package com.example.quiz1.activity.planEstudioActivity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.PlanEstudioApi
import com.example.quiz1.model.PlanEstudio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarPlanEstudioActivity : AppCompatActivity() {

    private lateinit var etCarrera: EditText
    private lateinit var etCurso: EditText
    private lateinit var etAnio: EditText
    private lateinit var etCiclo: EditText
    private lateinit var btnActualizar: Button

    private lateinit var planOriginal: PlanEstudio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_plan_estudio)

        // Referencias UI
        etCarrera = findViewById(R.id.etCarreraEditar)
        etCurso = findViewById(R.id.etCursoEditar)
        etAnio = findViewById(R.id.etAnioEditar)
        etCiclo = findViewById(R.id.etCicloEditar)
        btnActualizar = findViewById(R.id.btnActualizarPlan)

        // Obtenemos el plan recibido
        planOriginal = intent.getSerializableExtra("plan") as PlanEstudio

        // Rellenar campos
        etCarrera.setText(planOriginal.idCarrera.toString())
        etCurso.setText(planOriginal.idCurso.toString())
        etAnio.setText(planOriginal.anio.toString())
        etCiclo.setText(planOriginal.ciclo.toString())

        // Acción actualizar
        btnActualizar.setOnClickListener {
            val planActualizado = PlanEstudio(
                idPlanEstudio = planOriginal.idPlanEstudio,
                idCarrera = etCarrera.text.toString().toInt(),
                idCurso = etCurso.text.toString().toInt(),
                anio = etAnio.text.toString().toInt(),
                ciclo = etCiclo.text.toString().toInt()
            )

            ApiClient.retrofit.create(PlanEstudioApi::class.java)
                .actualizar(planActualizado)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditarPlanEstudioActivity, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditarPlanEstudioActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@EditarPlanEstudioActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}

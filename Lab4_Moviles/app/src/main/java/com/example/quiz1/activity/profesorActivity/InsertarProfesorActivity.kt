package com.example.quiz1.activity.profesorActivity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.model.Profesor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertarProfesorActivity : AppCompatActivity() {

    private val api = ApiClient.retrofit.create(ProfesorApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_profesor)

        val edtCedula   = findViewById<EditText>(R.id.edtCedulaProfesor)
        val edtNombre   = findViewById<EditText>(R.id.edtNombreProfesor)
        val edtTelefono = findViewById<EditText>(R.id.edtTelefonoProfesor)
        val edtEmail    = findViewById<EditText>(R.id.edtEmailProfesor)
        val btnGuardar  = findViewById<Button>(R.id.btnGuardarProfesor)

        btnGuardar.setOnClickListener {
            val nuevo = Profesor(
                cedula   = edtCedula.text.toString(),
                nombre   = edtNombre.text.toString(),
                telefono = edtTelefono.text.toString(),
                email    = edtEmail.text.toString()
            )
            api.insertar(nuevo).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@InsertarProfesorActivity, "Profesor insertado", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@InsertarProfesorActivity, "Error al insertar", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@InsertarProfesorActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

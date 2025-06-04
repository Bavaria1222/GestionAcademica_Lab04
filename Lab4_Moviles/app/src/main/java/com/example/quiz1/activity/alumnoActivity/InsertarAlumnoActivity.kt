package com.example.quiz1.activity.alumnoActivity

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionacademicaapp.model.Alumno
import com.example.quiz1.R
import com.example.quiz1.api.AlumnoApi
import com.example.quiz1.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertarAlumnoActivity : AppCompatActivity() {

    private lateinit var edtCedula: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtTelefono: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtFechaNacimiento: EditText
    private lateinit var edtIdCarrera: EditText
    private lateinit var btnGuardar: Button

    private val api = ApiClient.retrofit.create(AlumnoApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_alumno)

        edtCedula = findViewById(R.id.edtCedula)
        edtNombre = findViewById(R.id.edtNombre)
        edtTelefono = findViewById(R.id.edtTelefono)
        edtEmail = findViewById(R.id.edtEmail)
        edtFechaNacimiento = findViewById(R.id.edtFechaNacimiento)
        edtIdCarrera = findViewById(R.id.edtIdCarrera)
        btnGuardar = findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener { insertarAlumno() }
    }

    private fun insertarAlumno() {
        val alumno = Alumno(
            idAlumno = 0,
            cedula = edtCedula.text.toString(),
            nombre = edtNombre.text.toString(),
            telefono = edtTelefono.text.toString(),
            email = edtEmail.text.toString(),
            fechaNacimiento = edtFechaNacimiento.text.toString(),
            idCarrera = edtIdCarrera.text.toString().toIntOrNull() ?: 0
        )
        api.insertar(alumno).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@InsertarAlumnoActivity,
                        "Alumno insertado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this@InsertarAlumnoActivity,
                        "Error al insertar alumno",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@InsertarAlumnoActivity,
                    "Fallo: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}

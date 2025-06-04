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

class EditarAlumnoActivity : AppCompatActivity() {

    private lateinit var edtCedula: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtTelefono: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtFechaNacimiento: EditText
    private lateinit var edtIdCarrera: EditText
    private lateinit var btnActualizar: Button

    private val api = ApiClient.retrofit.create(AlumnoApi::class.java)
    private var idAlumno = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_alumno)

        edtCedula = findViewById(R.id.edtCedula)
        edtNombre = findViewById(R.id.edtNombre)
        edtTelefono = findViewById(R.id.edtTelefono)
        edtEmail = findViewById(R.id.edtEmail)
        edtFechaNacimiento = findViewById(R.id.edtFechaNacimiento)
        edtIdCarrera = findViewById(R.id.edtIdCarrera)
        btnActualizar = findViewById(R.id.btnActualizar)

        val alumno = intent.getSerializableExtra("alumno") as? Alumno
        alumno?.let {
            idAlumno = it.idAlumno
            edtCedula.setText(it.cedula)
            edtNombre.setText(it.nombre)
            edtTelefono.setText(it.telefono)
            edtEmail.setText(it.email)
            edtFechaNacimiento.setText(it.fechaNacimiento)
            edtIdCarrera.setText(it.idCarrera.toString())
        }

        btnActualizar.setOnClickListener {
            val alumnoEditado = Alumno(
                idAlumno = idAlumno,
                cedula = edtCedula.text.toString(),
                nombre = edtNombre.text.toString(),
                telefono = edtTelefono.text.toString(),
                email = edtEmail.text.toString(),
                fechaNacimiento = edtFechaNacimiento.text.toString(),
                idCarrera = edtIdCarrera.text.toString().toIntOrNull() ?: 0
            )
            api.modificar(alumnoEditado).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditarAlumnoActivity,
                            "Alumno actualizado",
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditarAlumnoActivity,
                            "Error al actualizar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@EditarAlumnoActivity,
                        "Fallo: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}

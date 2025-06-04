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

class EditarProfesorActivity : AppCompatActivity() {

    private val api = ApiClient.retrofit.create(ProfesorApi::class.java)
    private lateinit var profesorOriginal: Profesor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_profesor)

        val edtCedula   = findViewById<EditText>(R.id.edtCedulaProfesor)
        val edtNombre   = findViewById<EditText>(R.id.edtNombreProfesor)
        val edtTelefono = findViewById<EditText>(R.id.edtTelefonoProfesor)
        val edtEmail    = findViewById<EditText>(R.id.edtEmailProfesor)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarProfesor)

        profesorOriginal = intent.getSerializableExtra("profesor") as Profesor
        // Rellenamos campos con los datos existentes
        edtCedula.setText(profesorOriginal.cedula)
        edtNombre.setText(profesorOriginal.nombre)
        edtTelefono.setText(profesorOriginal.telefono)
        edtEmail.setText(profesorOriginal.email)

        // Deshabilitamos edición de cédula (es la PK)
        edtCedula.isEnabled = false

        btnActualizar.setOnClickListener {
            val actualizado = Profesor(
                cedula   = profesorOriginal.cedula,  // misma cédula
                nombre   = edtNombre.text.toString(),
                telefono = edtTelefono.text.toString(),
                email    = edtEmail.text.toString()
            )
            api.modificar(actualizado).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarProfesorActivity, "Profesor actualizado", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditarProfesorActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EditarProfesorActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

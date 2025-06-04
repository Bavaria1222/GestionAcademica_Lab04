package com.example.quiz1.activity.usuarioActivity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.UsuarioApi
import com.example.quiz1.model.Usuario
import retrofit2.*

class InsertarUsuarioActivity : AppCompatActivity() {

    private lateinit var edtCedula: EditText
    private lateinit var edtClave:  EditText
    private lateinit var edtRol:    EditText
    private lateinit var btnGuardar: Button

    private val api = ApiClient.retrofit.create(UsuarioApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_usuario)

        edtCedula  = findViewById(R.id.edtCedulaUsuario)
        edtClave   = findViewById(R.id.edtClaveUsuario)
        edtRol     = findViewById(R.id.edtRolUsuario)
        btnGuardar = findViewById(R.id.btnGuardarUsuario)

        btnGuardar.setOnClickListener {
            val usuario = Usuario(
                cedula = edtCedula.text.toString().trim(),
                clave  = edtClave.text.toString(),
                rol    = edtRol.text.toString().trim()
            )

            api.insertar(usuario).enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@InsertarUsuarioActivity,
                            "Usuario insertado correctamente",
                            Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@InsertarUsuarioActivity,
                            "Error al insertar usuario",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@InsertarUsuarioActivity,
                        "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}

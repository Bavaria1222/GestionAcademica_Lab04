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

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var edtCedula: EditText
    private lateinit var edtClave:  EditText
    private lateinit var edtRol:    EditText
    private lateinit var btnActualizar: Button

    private val api = ApiClient.retrofit.create(UsuarioApi::class.java)
    private var cedulaOriginal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        edtCedula  = findViewById(R.id.edtCedulaUsuario)
        edtClave   = findViewById(R.id.edtClaveUsuario)
        edtRol     = findViewById(R.id.edtRolUsuario)
        btnActualizar = findViewById(R.id.btnActualizarUsuario)

        // Recibir el Usuario pasado desde el fragment
        val usuarioRecibido = intent.getSerializableExtra("usuario") as? Usuario
        usuarioRecibido?.let {
            cedulaOriginal = it.cedula
            edtCedula.setText(it.cedula)
            edtClave.setText(it.clave)
            edtRol.setText(it.rol)
        }

        btnActualizar.setOnClickListener {
            val usuarioEditado = Usuario(
                cedula = edtCedula.text.toString().trim(),
                clave  = edtClave.text.toString(),
                rol    = edtRol.text.toString().trim()
            )
            api.modificar(usuarioEditado).enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarUsuarioActivity,
                            "Usuario actualizado",
                            Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditarUsuarioActivity,
                            "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EditarUsuarioActivity,
                        "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}

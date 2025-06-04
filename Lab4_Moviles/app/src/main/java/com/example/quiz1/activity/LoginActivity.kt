package com.example.quiz1.activity

import android.content.Intent
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R
import com.example.quiz1.api.ApiClient
import com.example.quiz1.api.LoginApi
import com.example.quiz1.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etCedula = findViewById<EditText>(R.id.etCedula)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegistro = findViewById<TextView>(R.id.tvRegistro)

        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        val api = ApiClient.retrofit.create(LoginApi::class.java)

        btnLogin.setOnClickListener {
            val cedula = etCedula.text.toString()
            val clave = etContrasena.text.toString()
            if (cedula.isBlank() || clave.isBlank()) {
                Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = Usuario(cedula, clave, "")
                api.login(usuario).enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful) {
                            val user = response.body() ?: return
                            // Guardamos el rol en mayúsculas para que el menú
                            // pueda evaluarlo sin depender del formato que
                            // envíe el API.
                            prefs.edit()
                                .putString("cedula", user.cedula)
                                .putString("rol", user.rol.uppercase())
                                .apply()
                            startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        tvRegistro.setOnClickListener {
            startActivity(Intent(this, InscriptionActivity::class.java))
        }
    }
}

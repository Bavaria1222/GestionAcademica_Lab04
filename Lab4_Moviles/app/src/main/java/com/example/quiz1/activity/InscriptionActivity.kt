package com.example.quiz1.activity

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz1.R

class InscriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCedula = findViewById<EditText>(R.id.etCedula)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etCarrera = findViewById<EditText>(R.id.etCarrera)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnInscribirme = findViewById<Button>(R.id.btnInscribirme)

        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)

        btnInscribirme.setOnClickListener {
            val nombre = etNombre.text.toString()
            val cedula = etCedula.text.toString()
            val correo = etCorreo.text.toString()
            val carrera = etCarrera.text.toString()
            val contrasena = etContrasena.text.toString()

            if (nombre.isBlank() || cedula.isBlank() || correo.isBlank() || carrera.isBlank() || contrasena.isBlank()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                prefs.edit()
                    .putString("usuario", correo)
                    .putString("clave", contrasena)
                    .putBoolean("logueado", true)
                    .apply()

                Toast.makeText(this, "Inscripción exitosa", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }
        }
    }
}

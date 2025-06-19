package com.example.quiz1.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.quiz1.R

class AyudaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ayuda, container, false)
        view.findViewById<TextView>(R.id.tvContenidoAyuda).movementMethod = LinkMovementMethod.getInstance()
        return view
    }
}

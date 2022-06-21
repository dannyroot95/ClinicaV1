package com.aukde.clinica.UI.Credentials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.aukde.clinica.Domain.Auth.AuthProvider
import com.aukde.clinica.Models.Paciente
import com.aukde.clinica.R
import com.aukde.clinica.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import java.util.HashMap
import java.util.jar.Attributes


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

         binding.buttonRegistro.setOnClickListener{

             val name = binding.editTextNombres
             val email = binding.editTextCorreo
             val dni= binding.editTextDni
             val password= binding.editTextContraseAReg
             val last_name= binding.editTextApellidos
             val phone = binding.editTextCelular
             val progressBar = binding.rgProgressBar

             val user = Paciente("",
                 dni.text.toString(),
                 name.text.toString(),
                 last_name.text.toString(),
                 email.text.toString(),
                 phone.text.toString(),
                 "Paciente")

       if(user.email !="" && password.toString() != "" && user.name !="" && user.last_name != ""
           && user.email != "" && user.phone != ""){
                 AuthProvider().register(this,progressBar,user,password.toString())
             }
             else{
                 Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show()
             }
         }
    }
}

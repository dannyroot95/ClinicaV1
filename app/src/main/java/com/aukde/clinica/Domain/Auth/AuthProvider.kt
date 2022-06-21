package com.aukde.clinica.Domain.Auth

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.aukde.clinica.Domain.Network
import com.aukde.clinica.Models.Paciente
import com.aukde.clinica.UI.Credentials.LoginActivity
import com.aukde.clinica.UI.Credentials.RegisterActivity
import com.aukde.clinica.Utils.Constants
import com.aukde.clinica.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions

class  AuthProvider {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(activity : LoginActivity , dni : String ,password : String,bindingProgressBar: ProgressBar){

        var count = 0

        bindingProgressBar.visibility = View.VISIBLE

        Network().firestore().collection(Constants.USERS).whereEqualTo("dni",dni).get().addOnSuccessListener { document ->

            if(document != null){
                for (Query : QueryDocumentSnapshot in document){
                    if (Query.exists()){
                        count++
                        val typeUser = Query.data["type_user"].toString()
                        val email = Query.data["email"].toString()
                        if(typeUser == Constants.PATIENT){

                            Toast.makeText(activity, "Usted es paciente!",Toast.LENGTH_SHORT).show()

                            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(activity,"Logueado",Toast.LENGTH_SHORT).show()
                                    activity.startActivity(Intent(activity,LoginActivity::class.java))
                                    bindingProgressBar.visibility = View.GONE
                                }
                                else{
                                    Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                    bindingProgressBar.visibility = View.GONE
                                }
                            }

                        }else{
                            Toast.makeText(activity, "Usuario no permitido!",Toast.LENGTH_SHORT).show()
                            bindingProgressBar.visibility = View.GONE
                        }
                    }else{
                        Toast.makeText(activity, "Error!",Toast.LENGTH_SHORT).show()
                        bindingProgressBar.visibility = View.GONE
                    }
                }
                if (count == 0){
                    Toast.makeText(activity, "No existe este usuario!",Toast.LENGTH_SHORT).show()
                    bindingProgressBar.visibility = View.GONE
                }
            }
        }.addOnCanceledListener {
            Toast.makeText(activity, "Error!",Toast.LENGTH_SHORT).show()
            bindingProgressBar.visibility = View.GONE
        }.addOnFailureListener {
            Toast.makeText(activity, "Error!",Toast.LENGTH_SHORT).show()
            bindingProgressBar.visibility = View.GONE
        }
    }


    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid

        }
        return currentUserID
    }

    fun logout(loginActivity: Activity){

        val logout: FirebaseAuth = FirebaseAuth.getInstance()
        logout.signOut()
        loginActivity.startActivity(Intent(loginActivity,LoginActivity::class.java))
        loginActivity.finish()

    }

    fun register (activity : RegisterActivity , progressBar: ProgressBar, user : Paciente , password: String){

        progressBar.visibility = View.VISIBLE

        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val id = task.result.user!!.uid
                updateUser(activity,progressBar,id, user)
                Toast.makeText(activity, "REGISTRADO", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "NO REGISTRADO", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        }
    }

   private fun updateUser(activity: RegisterActivity, progressBar: ProgressBar, id: String, user: Paciente) {

       user.id = id
        //CREATE DATA CLASS USER
       Network().firestore().collection("users").document(id).set(user,
            SetOptions.merge()).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Toast.makeText(activity, "ACTUALIZADO", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                firebaseAuth.signOut()
            }
            else{
                Toast.makeText(activity,"No se pudo regsitrar",Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }

        }.addOnCanceledListener {
            Toast.makeText(activity,"Error al actualizar datos",Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE

        }.addOnFailureListener {
            Toast.makeText(activity,"Error al actualizar datos",Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        }
    }

}









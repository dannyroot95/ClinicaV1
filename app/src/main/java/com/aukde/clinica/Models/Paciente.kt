package com.aukde.clinica.Models

data class Paciente( var id:String="",
                     var date_register : Long = 0,
                     var dni : String,
                     var name:String="",
                     var last_name:String,
                     var email: String,
                     var phone :String,
                     var type_user:String )

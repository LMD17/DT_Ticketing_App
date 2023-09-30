package com.example.ticketingapplication

//Imports
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

//Class activity_logout
class activity_logout : AppCompatActivity(){

    //The initialization and creation of variables
    private lateinit var ticketDatabaseHelper: ticket_database_helper

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ticketDatabaseHelper = ticket_database_helper(this)
        ticketDatabaseHelper.setLoggedInStatus("0", intent.getIntExtra("USERID", -1))
        val i = Intent(this, activity_login::class.java)
        startActivity(i)
    }
}
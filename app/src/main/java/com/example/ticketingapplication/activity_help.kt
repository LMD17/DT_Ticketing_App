package com.example.ticketingapplication

//Imports
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

//Class activity_help
class activity_help : AppCompatActivity() {

    //The initialization and creation of variables
    private var CLIENT_ID: Int = -1
    private lateinit var share_preferences: SharedPreferences
    private lateinit var containerRL: ConstraintLayout

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_help)    //set content view

        //Declare variable and initialise
        val button_menu= findViewById<Button>(R.id.ticket_button_menu_icon)

        CLIENT_ID = intent.getIntExtra("USERID", -1)    //get user_id from intent

        //onClickListener for menu button
        button_menu.setOnClickListener{
            //Redirect user to menu page
            val intent = Intent(this, activity_menu::class.java)
            intent.putExtra("USERID", CLIENT_ID)    //pass user)id in intent
            startActivity(intent)
        }
    }
}
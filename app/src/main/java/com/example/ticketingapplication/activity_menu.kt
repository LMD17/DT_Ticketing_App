package com.example.ticketingapplication

//Imports
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

//Class activity_menu
class activity_menu : AppCompatActivity() {

    //The initialization and creation of variables
    private lateinit var share_preferences: SharedPreferences
    private lateinit var containerRL: ConstraintLayout
    private var CLIENT_ID: Int = -1
    private var dbHandler: ticket_database_helper? = null
    private lateinit var ticketDatabaseHelper: ticket_database_helper

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_menu)    //set content view

        //link variable to ticket_database_helper
        dbHandler = ticket_database_helper(this)

        CLIENT_ID = intent.getIntExtra("USERID", -1)//get user_id from intent

        //link variable to button
        val profile_btn = findViewById<Button>(R.id.button_profile)
        //onClickListener for profile button
        profile_btn.setOnClickListener{
            //Redirect user to home page
            val intent = Intent(this, activity_profile::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //link variable to button
        val home_btn = findViewById<Button>(R.id.button_home)
        //onClickListener for home button
        home_btn.setOnClickListener{
            //Redirect user to home page
            val intent = Intent(this, activity_home::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //link variable to button
        val troubleshoot_btn= findViewById<Button>(R.id.button_trooubleshoot)
        //onClickListener for troubleshoot button
        troubleshoot_btn.setOnClickListener{
            //Redirect user to troubleshoot page
            val intent = Intent(this, activity_troubleshoot::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //link variable to button
        val help_btn= findViewById<Button>(R.id.button_help)
        //onClickListener for help button
        help_btn.setOnClickListener{
            //redirect to activity help page
            val intent = Intent(this, activity_help::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //link variable to button
        val logout_btn= findViewById<Button>(R.id.button_logout)
        //onClickListener for help button
        logout_btn.setOnClickListener{
            ticketDatabaseHelper = ticket_database_helper(this)
            ticketDatabaseHelper.setLoggedInStatus("0", CLIENT_ID)   //set logged in status
            //redirect to activity logout page
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
        }
    }
}
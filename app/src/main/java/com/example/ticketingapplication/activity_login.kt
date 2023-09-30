package com.example.ticketingapplication

//Imports
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout

//Class activity_login
class activity_login : AppCompatActivity() {

    //The initialization and creation of variables
    private lateinit var share_preferences: SharedPreferences
    private lateinit var button_login_signin: Button
    private lateinit var button_login_signup: Button
    private lateinit var edittext_email: EditText
    private lateinit var edittext_password: EditText
    private var dbHandler: ticket_database_helper? = null

    //disable android back button
    var onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                // Disable the back button functionality
                // To enable it again, set isEnabled to true or remove this callback
            }
        }

    //Override onCreate function
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)   //set content view
        setTitle("DT Ticketing")

        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        share_preferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE) //get sharePreferences
        val dark_mode = share_preferences.getBoolean("dark_mode", false)    //get dark_mode preference

        //check if dark mode is enabled
        if(dark_mode){  //if true, set app to dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)  //set night mode on
        }else{  //else set app to not dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  //set night mode on
        }

        //link variables to views
        button_login_signin = findViewById(R.id.button_login)
        edittext_email = findViewById(R.id.edittext_login_email)
        edittext_password = findViewById(R.id.edittext_password)
        button_login_signup = findViewById(R.id.button_sign_up)

        //link variable to ticket_database_helper
        dbHandler = ticket_database_helper(this)

        //onClickListener for login button
        button_login_signin.setOnClickListener {
            //check that user actually entered data
            if (edittext_email.text.isBlank() || edittext_password.text.isBlank()){
                Toast.makeText(applicationContext, "Please enter your details.", Toast.LENGTH_SHORT).show()  //display error message
            }else{
                //get the user record for the specific email input
                try {
                    //declare and initialise user model
                    val users: user_model = dbHandler!!.getUser(edittext_email.text.toString())

                    //check if user for the email input exists
                    if (users.user_id != -1) {
                        //Encrypt the password so as to ensure the password would be correctly compared.
                        val password = dbHandler!!.encryptThisString(edittext_password.text.toString())
                        //check that the password input matches the password for the specific user
                        if (users.password == password) {
                            dbHandler!!.setLoggedInStatus("1", users.user_id)   //set logged in status

                            //redirect to activity_home
                            val i = Intent(applicationContext, activity_home::class.java)
                            i.putExtra("USERID", users.user_id) //pass the user_id with intent
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(i)    //start activity
                        }
                        else{
                            Toast.makeText(applicationContext, "Incorrect email or password! Please try again.", Toast.LENGTH_SHORT).show()  //display error message
                        }
                    }else{
                        Toast.makeText(applicationContext, "Incorrect email or password! Please try again.", Toast.LENGTH_SHORT).show()  //display error message
                    }
                }catch (e:NullPointerException)
                {
                    Toast.makeText(applicationContext, "Something went wrong, please try again!!!", Toast.LENGTH_LONG).show()   //display error message
                }
            }
        }
        //onClickListener for sign up button
        button_login_signup.setOnClickListener {
            //redirect to activity_create_account
            val intent = Intent(this, activity_create_account::class.java)
            startActivity(intent)   //start activity
        }
    }
}
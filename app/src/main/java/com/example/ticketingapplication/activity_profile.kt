package com.example.ticketingapplication

//Imports
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

//Class activity_profile
class activity_profile : AppCompatActivity() {

    //The initialization and creation of variables
    private lateinit var share_preferences: SharedPreferences
    private lateinit var button_menu: Button
    private lateinit var switch_dark_mode: SwitchCompat
    private lateinit var button_edit_user: Button
    private lateinit var button_delete_user: Button
    private lateinit var textview_first_name_data: TextView
    private lateinit var textview_last_name_data: TextView
    private lateinit var textview_email_data: TextView
    private lateinit var edittext_password: TextView
    private lateinit var textview_department_data: TextView
    private lateinit var textview_cell_num_data: TextView
    private var dbHandler: ticket_database_helper? = null
    private var CLIENT_ID: Int = -1

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_profile) //set content view

        //link variable to view
        switch_dark_mode = findViewById<SwitchCompat>(R.id.switch_dark_mode)

        share_preferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE) //get sharePreferences
        val editor = share_preferences.edit()    //create editor to edit sharePreferences
        val dark_mode = share_preferences.getBoolean("dark_mode", false)    //get dark_mode preference

        switch_dark_mode.isChecked = dark_mode  //set switch to value of dark_mode (true/false)

        CLIENT_ID = intent.getIntExtra("USERID", -1)    //Get userID from intent

        //link variable to view
        button_menu = findViewById<Button>(R.id.button_menu_icon)
        button_edit_user = findViewById(R.id.button_edit)
        button_delete_user = findViewById(R.id.button_delete)
        textview_first_name_data = findViewById(R.id.textview_first_name_data)
        textview_last_name_data = findViewById(R.id.textview_last_name_data)
        textview_email_data = findViewById(R.id.textview_email_data)
        edittext_password = findViewById(R.id.edittext_password)
        textview_department_data = findViewById(R.id.textview_department_data)
        textview_cell_num_data = findViewById(R.id.textview_cell_num_data)
        edittext_password.setEnabled(false)

        //link variable to ticket_database_helper
        dbHandler = ticket_database_helper(this)

        //get the user_id for the specific user logged in
        val users: user_model = dbHandler!!.getUser(intent.getIntExtra("USERID", -1))

        //Must populate textview fields when the user is updating their account/profile
        textview_first_name_data.setText(users.first_name)
        textview_last_name_data.setText(users.last_name)
        textview_email_data.setText(users.email)
        edittext_password.setText(users.password)
        textview_department_data.setText(users.department)
        textview_cell_num_data.setText(users.cell_num)

        //onClickListener for menu button
        button_menu.setOnClickListener {
            //Redirect user to menu page
            val intent = Intent(this, activity_menu::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //onClickListener for edit_user button
        button_edit_user.setOnClickListener {
            //Redirect user to create account page
            val i = Intent(applicationContext, activity_create_account::class.java)
            i.putExtra("Mode", "E") //set the mode to 'E' for edit
            i.putExtra("USERID", users.user_id) //pass the user_id that is to be edited
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)    //start activity
        }

        //onClickListener for delete button
        button_delete_user.setOnClickListener {
            //alert dialog for user to confirm deletion
            val dialog = AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("YES") { dialog, i ->
                    val success = dbHandler?.delete_user(
                        intent.getIntExtra(
                            "USERID",
                            -1
                        )
                    ) as Boolean   //get user_id for specific profile to be deleted

                    editor.clear()  //clear the user preferences
                    editor.apply()  //apply edits to share_preferences
                    if (success)
                        finish()
                    dialog.dismiss()
                    //Redirect to login page once profile is deleted
                    val i = Intent(applicationContext, activity_login::class.java)
                    startActivity(i)    //start activity
                }
                .setNegativeButton("NO") { dialog, i ->
                    dialog.dismiss()
                }
            dialog.show()
        }

        //onClickListener for delete button
        switch_dark_mode.setOnClickListener{
            if (switch_dark_mode.isChecked) {
                // Toggle is in the "on" state
                editor.putBoolean("dark_mode", true)    //save dark_mode = on in share_preferences
                editor.apply()  //apply edits to share_preferences
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)  //set night mode on

            } else {
                // Toggle is in the "off" state
                editor.putBoolean("dark_mode", false)    //save dark_mode = off in share_preferences
                editor.apply()  //apply edits to share_preferences
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  //set night mode off
            }
        }
    }
}
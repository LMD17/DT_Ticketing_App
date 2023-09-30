package com.example.ticketingapplication

//Imports
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isEmpty

//Class activity_create_ticket
class activity_create_ticket  : AppCompatActivity() {

    //The initialization and creation of variables
    private lateinit var textview_create_ticket: TextView
    private lateinit var button_create_ticket: Button
    private lateinit var button_cancel: Button
    private lateinit var edittext_title: EditText
    private lateinit var spinner_ticket_priority: Spinner
    private lateinit var edittext_description: EditText
    private var dbHandler: ticket_database_helper? = null

    // Function to get the index of a given value in a spinner
    private fun getIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0
    }

    //Override onCreate function
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_create_ticket)   //set content view

        //link variables to views
        textview_create_ticket = findViewById(R.id.textview_create_ticket)
        button_create_ticket = findViewById(R.id.button_create_ticket)
        button_cancel = findViewById(R.id.button_cancel)
        edittext_title = findViewById(R.id.edittext_title)
        spinner_ticket_priority = findViewById(R.id.spinner_priority)
        edittext_description = findViewById(R.id.edittext_description)

        //link variable to ticket_database_helper
        dbHandler = ticket_database_helper(this)

        //Declare variable and initialise
        var isEditMode: Boolean = false

        //if Mode 'E' then user selected to edit data and we must update data in the database
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            //if 'E', then user selected to edit data and must update data
            isEditMode = true
            textview_create_ticket.text = "Update Ticket"
            button_create_ticket.text = "Update Ticket"

            //get the ticket_id for the specific ticket selected
            val tickets: ticket_model = dbHandler!!.getTicket(intent.getIntExtra("TICKETID", -1))

            //Populate editText fields and spinner when user is updating a ticket
            edittext_title.setText(tickets.ticket_title)
            spinner_ticket_priority.setSelection(getIndex(spinner_ticket_priority, tickets.ticket_priority))
            edittext_description.setText(tickets.ticket_description)

        } else {    //else user selected to create ticket and we must insert data
            isEditMode = false  //set edit mode to true
            //change title and button text
            textview_create_ticket.text = "Create Ticket"
            button_create_ticket.text = "Create Ticket"
        }

        //onClickListener for cancel button
        button_cancel.setOnClickListener{
            //redirect to activity_home
            val i = Intent(applicationContext, activity_home::class.java)
            //pass the user_id with intent
            i.putExtra("USERID", intent.getIntExtra("USERID", -1))
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)    //start activity
            finish()    //to end this activity
        }

        //onClickListener for create_ticket button
        button_create_ticket.setOnClickListener {
            //Declare and initialise variables
            var success: Boolean = false
            val tickets: ticket_model = ticket_model()

            //check that user actually entered data
            if (edittext_title.text.isBlank() || spinner_ticket_priority.isEmpty() || edittext_description.text.isBlank()){
                Toast.makeText(applicationContext, "Please make sure to complete each field.", Toast.LENGTH_LONG).show()  //display error message
            }else{
                //check if user wants to insert or edit?
                if (isEditMode) {   //user is updating a ticket

                    tickets.ticket_id = intent.getIntExtra("TICKETID", -1) //get id of this ticket

                    //Update record with data in editText fields
                    tickets.ticket_title = edittext_title.text.toString()
                    tickets.ticket_priority = spinner_ticket_priority.selectedItem.toString()
                    tickets.ticket_description = edittext_description.text.toString()
                    tickets.user_id = intent.getIntExtra("USERID", -1) //get id of this ticket

                    success = dbHandler?.updateRecord(tickets) as Boolean   //Returns true if the data was updated successfully
                    //addRecord method calls the method in the ticket_database_helper to update the data to the database.

                } else {    //else user is creating a ticket
                    //Insert ticket in database
                    tickets.ticket_title = edittext_title.text.toString()
                    tickets.ticket_priority = spinner_ticket_priority.selectedItem.toString()
                    tickets.ticket_status = "Open"  //status is automatically set to 'open' when ticket is created
                    tickets.ticket_description = edittext_description.text.toString()
                    tickets.user_id = intent.getIntExtra("USERID", -1) //get id of this ticket

                    success = dbHandler?.addRecord(tickets) as Boolean  //Returns true if the data was inserted successfully
                    //addRecord method calls the method in the ticket_database_helper to add the data to the database.
                }
                //If success true --> redirect user to activity_home
                if (success) {
                    //redirect to activity_home
                    val i = Intent(applicationContext, activity_home::class.java)
                    //pass the user_id with intent
                    i.putExtra("USERID", tickets.user_id)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)    //start activity
                    finish()    //to end this activity
                } else {
                    //notify user that an error occurred when creating account
                    Toast.makeText(
                        applicationContext,
                        "Something went wrong when inserting user details!!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
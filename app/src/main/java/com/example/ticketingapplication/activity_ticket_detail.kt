package com.example.ticketingapplication

//Imports
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

//Class activity_ticket_detail
class activity_ticket_detail : AppCompatActivity() {

    //The initialization and creation of variables
    lateinit var button_back: Button
    lateinit var button_edit_ticket: Button
    lateinit var button_delete_ticket: Button
    lateinit var textview_ticket_num: TextView
    lateinit var textview_title: TextView
    lateinit var textview_ticket_status: TextView
    lateinit var textview_ticket_priority: TextView
    lateinit var textview_technician: TextView
    lateinit var textview_description: TextView
    private var dbHandler: ticket_database_helper? = null

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_ticket_detail)   //set content view

        //get values from the
        button_back = findViewById(R.id.button_back)
        button_edit_ticket = findViewById(R.id.button_edit)
        button_delete_ticket = findViewById(R.id.button_delete)
        textview_ticket_num = findViewById(R.id.textview_ticket_num)
        textview_title = findViewById(R.id.textview_title_data)
        textview_ticket_status = findViewById(R.id.textview_status_data)
        textview_ticket_priority = findViewById(R.id.textview_priority_data)
        textview_technician = findViewById(R.id.textview_technician_data)
        textview_description = findViewById(R.id.textview_description_data)

        //link variable to ticket_database_helper
        dbHandler = ticket_database_helper(this)

        //get the ticket_id for the specific ticket selected
        val tickets: ticket_model = dbHandler!!.getTicket(intent.getIntExtra("TICKETID", 0))

        //Must populate textview fields when the user is viewing a ticket
        textview_ticket_num.setText(tickets.ticket_id.toString())
        textview_title.setText(tickets.ticket_title)
        textview_ticket_status.setText(tickets.ticket_status)
        textview_ticket_priority.setText(tickets.ticket_priority)
        textview_technician.setText(tickets.ticket_technician)
        textview_description.setText(tickets.ticket_description)

        //onClickListener for back button
        button_back.setOnClickListener{
            //redirect to activity_home
            val i = Intent(applicationContext, activity_home::class.java)
            //pass the user_id with intent
            i.putExtra("USERID", intent.getIntExtra("USERID", -1))  //pass user_id
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)    //start activity
            finish()    //to end this activity
        }

        //onClickListener for edit ticket button
        button_edit_ticket.setOnClickListener{
            //navigate to CreateTicket Activity
            val i = Intent(applicationContext, activity_create_ticket::class.java)
            i.putExtra("Mode", "E") //set the mode to 'E' for edit
            i.putExtra("TICKETID", tickets.ticket_id) //pass the ticket_id that is to be edited
            i.putExtra("USERID", intent.getIntExtra("USERID", -1))  //pass user_id
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)    //start activity
        }

        //onClickListener for delete ticket button
        button_delete_ticket.setOnClickListener {
            //alert dialog for user to confirm deletion
            val dialog = AlertDialog.Builder(this).setTitle("Info")
                .setMessage("Are you sure you want to delete this ticket?")
                .setPositiveButton("YES") { dialog, i ->
                    val success = dbHandler?.delete_ticket(intent.getIntExtra("TICKETID", 0)) as Boolean   //get ticket_id for specific ticket to be deleted
                    if (success)
                        finish()
                    dialog.dismiss()
                    //Redirect to home page once ticket is deleted
                    val i = Intent(applicationContext, activity_home::class.java)
                    i.putExtra("USERID", intent.getIntExtra("USERID", -1))    //pass user_id in intent
                    startActivity(i)    //start activity
                }
                .setNegativeButton("NO") { dialog, i ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }
}
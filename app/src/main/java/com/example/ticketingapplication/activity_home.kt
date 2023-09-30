package com.example.ticketingapplication

//Imports
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

//Class activity_home
class activity_home : AppCompatActivity() {

    //The initialization and creation of variables
    private lateinit var share_preferences: SharedPreferences
    private lateinit var containerRL: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private var adapter: ticket_adapter? = null
    private lateinit var ticket_search: EditText
    private lateinit var search_button: Button
    private lateinit var button_priority: Button
    private lateinit var button_menu: Button
    private lateinit var button_date: Button
    private lateinit var createticket_button: Button
    private lateinit var troubleshoot_button: Button
    private var buttonVisible: Boolean = false
    private lateinit var ticketDatabaseHelper: ticket_database_helper
    private var CLIENT_ID: Int = -1

    //Override onCreate function
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        //The following two lines of code opens the current tickets or home page.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_home)    //set content view

        CLIENT_ID = intent.getIntExtra("USERID", -1)   //get user_id from intent

        //link variables to views
        button_menu= findViewById<Button>(R.id.button_menu_icon)
        createticket_button= findViewById<Button>(R.id.button_create_ticket)
        troubleshoot_button= findViewById<Button>(R.id.button_troubleshoot)
        search_button= findViewById<Button>(R.id.button_sortlist)
        button_priority= findViewById<Button>(R.id.button_priority)
        button_date= findViewById<Button>(R.id.button_date)


        //The below functions are called since they have instances of different page items.
        initView()
        initRecyclerView()

        //link variable to ticket_database_helper
        ticketDatabaseHelper = ticket_database_helper(this)

        //The following function call will populate the recyclerview in the current tickets page.
        getTicket()

        //Set search methods in place to make it possible to easily search for items
        ticket_search.addTextChangedListener(object : TextWatcher {
            //The following function will run when enter is pressed whilst on the search bar.
            //This function will as such update the content in the recycler view.
            override fun afterTextChanged(s: Editable) {
                val tickList = ticketDatabaseHelper.get_all_searched_tickets(s.trim(),CLIENT_ID.toString())
                adapter?.addItems(tickList)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            //The following function will run when the user is typing within the search bar.
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val tickList = ticketDatabaseHelper.get_all_searched_tickets(s.trim(),CLIENT_ID.toString())
                adapter?.addItems(tickList)
            }
        })

        //onClickListener for menu button
        button_menu.setOnClickListener{
            //Redirect user to menu page
            val intent = Intent(this, activity_menu::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //onClickListener for create_ticket button
        //This on click listener will re-direct the user to the create ticket page when the create ticket button is selected.
        createticket_button.setOnClickListener{
            //Redirect user to create_ticket page
            val intent = Intent(this, activity_create_ticket::class.java)
            intent.putExtra("USERID", CLIENT_ID)    //pass user_id in intent
            startActivity(intent)   //start activity
        }

        //onClickListener for troubleshoot button
        //This on click listener will re-direct the user to the troubleshoot page when the troubleshoot button is selected.
        troubleshoot_button.setOnClickListener{
            //Redirect user to troubleshoot page
            val intent = Intent(this, activity_troubleshoot::class.java)
            intent.putExtra("USERID", CLIENT_ID)    //pass user_id in intent
            startActivity(intent)   //start activity
        }

        //onClickListener for search button
        search_button.setOnClickListener{
            if(buttonVisible == true)
            {
                buttonVisible = false
                button_priority.visibility = View.INVISIBLE
                button_date.visibility = View.INVISIBLE
            }
            else
            {
                buttonVisible = true
                button_priority.visibility = View.VISIBLE
                button_date.visibility = View.VISIBLE
            }
        }

        //onClickListener for priority sort button
        button_priority.setOnClickListener{
            val tickList = ticketDatabaseHelper.get_priority_sorted_tickets(CLIENT_ID.toString())

            //Display data in recycler view
            adapter?.addItems(tickList)
        }

        //onClickListener for date sort button
        button_date.setOnClickListener{
            val tickList = ticketDatabaseHelper.get_date_sorted_tickets(CLIENT_ID.toString())

            //Display data in recycler view
            adapter?.addItems(tickList)
        }

    }
    //This function will populate the recycler view with all user tickets.
    private fun getTicket()
    {
        try {
            val tickList = ticketDatabaseHelper.get_all_tickets(CLIENT_ID.toString())

            //Display data in recycler view
            adapter?.addItems(tickList)
        }catch (e: IOException)
        {
            Log.e("Error", "Error fetching tickets")
        }

    }

    //This function calls the recycler view and adapter. This function is then usable within the main function.
    //Meaning that the main function is able to display and alter recycler view and adapter elements by calling this function.
    private fun initRecyclerView()
    {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ticket_adapter(applicationContext)
        recyclerView.adapter = adapter
    }

    //The function below is used to call the recycler view and search bar elements within the home layout.
    //This function may as such be called within the main function within this class to alter and display the required elements from the
    //database
    private fun initView()
    {
        //btnView = findViewById(R.id.btnView)
        recyclerView = findViewById(R.id.recyclerView)
        ticket_search = findViewById(R.id.search_bar)
    }
}
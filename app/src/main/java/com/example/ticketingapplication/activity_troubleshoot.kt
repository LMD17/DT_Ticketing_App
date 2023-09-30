package com.example.ticketingapplication

//Imports
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

//Class activity_troubleshoot
class activity_troubleshoot : AppCompatActivity() {

    //The initialization and creation of variables
    private var adapter: troubleshooting_adapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var button_menu: Button
    private lateinit var button_help: Button
    private lateinit var troubleshooting_search: EditText
    private var CLIENT_ID: Int = -1

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_troubleshoot)    //set content view

        val troubleshoot_options = ArrayList<String>()  //declare ArrayList
        val filteredList = ArrayList<String>()  //declare ArrayList

        //Add elements to ArrayList
        troubleshoot_options.add("Have you tried restarting?")
        troubleshoot_options.add("Have you tried updating your devices' operating system?")
        troubleshoot_options.add("Is your device connected to the internet?")
        troubleshoot_options.add("Clear your cache.")
        troubleshoot_options.add("Run a Antivirus scan.")
        troubleshoot_options.add("Check for error messages.")

        //The below functions are called since they have instances of different page items.
        initView()
        initRecyclerView()
        //initialize button
        button_menu= findViewById<Button>(R.id.button_menu)
        button_help= findViewById<Button>(R.id.button_help)

        //Get user_id from intent
        CLIENT_ID = intent.getIntExtra("USERID", -1)

        //set menu ONClickListener
        button_menu.setOnClickListener{
            //redirect to activity menu page
            val intent = Intent(this, activity_menu::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //set menu ONClickListener
        button_help.setOnClickListener{
            //redirect to activity menu page
            val intent = Intent(this, activity_help::class.java)
            intent.putExtra("USERID", CLIENT_ID)
            startActivity(intent)
        }

        //Set search methods in place to make it possible to easily search for items
        troubleshooting_search.addTextChangedListener(object : TextWatcher {
            //The following function will run when enter is pressed whilst on the search bar.
            //This function will as such update the content in the recycler view.
            override fun afterTextChanged(s: Editable) {
                filteredList.clear()
                troubleshoot_options.filter { it.contains(s.trim()) }
                    .forEach { filteredList.add(it) }
                val troubleshoot = filteredList
                adapter?.addItems(troubleshoot)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            //The following function will run when the user is typing within the search bar.
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                filteredList.clear()
                troubleshoot_options.filter { it.contains(s.trim()) }
                    .forEach { filteredList.add(it) }
                val troubleshoot = filteredList
                adapter?.addItems(troubleshoot)
            }
        })
        //try-catch for error handling
        try {
            //Display data in recycler view
            adapter?.addItems(troubleshoot_options)
        }catch (e: IOException)
        {
            Log.e("Error", "Error adding troubleshoot items")
        }
    }

    //
    private fun initRecyclerView()
    {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = troubleshooting_adapter()
        recyclerView.adapter = adapter
    }
    //
    private fun initView()
    {
        //btnView = findViewById(R.id.btnView)
        recyclerView = findViewById(R.id.recyclerView)
        troubleshooting_search = findViewById(R.id.search_bar)
    }
}
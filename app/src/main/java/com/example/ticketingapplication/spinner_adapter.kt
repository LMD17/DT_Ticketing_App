package com.example.ticketingapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

//Class spinner_adapter
class spinner_adapter : AppCompatActivity() {

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_create_ticket)   //set content view

        // Create an ArrayAdapter using a string array and a default spinner layout
        val PriorityAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_values_priority, android.R.layout.simple_spinner_item)

        // Specify the layout to use when the list of choices appears
        PriorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    }
}
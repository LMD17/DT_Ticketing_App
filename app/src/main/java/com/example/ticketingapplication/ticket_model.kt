package com.example.ticketingapplication

//Class ticket_model
//This class is used to create ticket objects and store/retrieve ticket information within the ticketing application
// It provides a structured representation of a ticket and its associated data
class ticket_model {
    var ticket_id: Int = 0
    var ticket_title: String = ""
    var ticket_status: String = ""
    var ticket_priority: String = ""
    var ticket_priority_num: Int = 0
    var ticket_description: String = ""
    var ticket_technician: String = ""
    var ticket_date_created: String = ""
    var user_id: Int = 0
}
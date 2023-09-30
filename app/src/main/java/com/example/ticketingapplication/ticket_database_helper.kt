package com.example.ticketingapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import com.example.ticketingapplication.ticket_model
import com.example.ticketingapplication.user_model
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.math.log

class ticket_database_helper (context: Context) : SQLiteOpenHelper(context,
    ticket_database_helper.DATABASE_NAME, null,
    ticket_database_helper.DATABASE_VERSION
)
{

    //This will run when the MyDatabaseHelper class is created
    companion object
    {
        //Define database name and version
        private const val DATABASE_NAME = "DreamTeam.db"
        private const val DATABASE_VERSION = 28

        //Ticket table: Define table and column names
        private const val TICKET_TABLE_NAME = "ticket"
        private const val TICKET_COLUMN_ID = "ticketNumber"
        private const val TICKET_COLUMN_TITLE = "tickeTitle"
        private const val TICKET_COLUMN_STATUS = "ticketStatus"
        private const val TICKET_COLUMN_PRIORITY = "ticketPriority"
        private const val TICKET_COLUMN_TECHNICIAN = "technicianID"
        private const val TICKET_COLUMN_DESCRIPTION = "ticketsDescription"
        private const val TICKET_COLUMN_DATECREATED = "ticketCreationDate"
        private const val TICKETPRIORITYNUM = "ticketPriorityNum"
        private const val USER_COLUMNID = "userId"

        private const val CURRENTUSERID = "userID"

        //User table: Define table and column names
        private const val USER_TABLE_NAME = "user"
        private const val USER_COLUMN_ID = "userID"
        private const val USER_COLUMN_FIRST_NAME = "firstName"
        private const val USER_COLUMN_LAST_NAME = "lastName"
        private const val USER_COLUMN_EMAIL = "email"
        private const val USER_COLUMN_PASSWORD = "password"
        private const val USER_COLUMN_DEPARTMENT = "department"
        private const val USER_COLUMN_CELL_NUM = "cellphoneNumber"
        private const val USER_LOGGED_STATUS = "loggedStatus" //1 is online and 0 is offline


        private const val TECHNICIAN = "Technician"
        private const val TECHNICIAN_ID = "technicianID"
        private const val TECHNICIAN_FIRST_NAME = "firstName"
        private const val TECHNICIAN_LAST_NAME = "lastName"
        private const val TECHNICIAN_EMAIL = "email"
        private const val TECHNICIAN_PASS = "password"
        private const val TECHNICIAN_CELL = "cellphoneNumber"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // below is a sqlite query, where column names
        // along with their data types is given
        //
        val createTblTechnician = ("CREATE TABLE $TECHNICIAN ("
                + "$TECHNICIAN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$TECHNICIAN_FIRST_NAME TEXT, "
                + "$TECHNICIAN_LAST_NAME TEXT, "
                + "$TECHNICIAN_EMAIL TEXT, "
                + "$TECHNICIAN_PASS TEXT, "
                + "$TECHNICIAN_CELL TEXT);")

        val createTblUser = ("CREATE TABLE $USER_TABLE_NAME ($USER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$USER_COLUMN_FIRST_NAME TEXT, " +
                "$USER_COLUMN_LAST_NAME TEXT, " +
                "$USER_COLUMN_EMAIL TEXT, " +
                "$USER_COLUMN_PASSWORD TEXT, " +
                "$USER_COLUMN_DEPARTMENT TEXT, " +
                "$USER_COLUMN_CELL_NUM TEXT, "+
                "$USER_LOGGED_STATUS TEXT);")

        val createTblTicket = ("CREATE TABLE " + TICKET_TABLE_NAME + " ("
                + "$TICKET_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$TICKET_COLUMN_TITLE TEXT, "
                + "$TICKET_COLUMN_TECHNICIAN TEXT,"
                + "$TICKET_COLUMN_PRIORITY TEXT, "
                + "$TICKET_COLUMN_DATECREATED TEXT, "
                + "$TICKETPRIORITYNUM INTEGER, "
                + "$TICKET_COLUMN_STATUS TEXT, "
                + "$TICKET_COLUMN_DESCRIPTION TEXT, "
                // + "FOREIGN KEY($TICKET_COLUMN_TECHNICIAN) REFERENCES $TECHNICIAN($TECHNICIAN_ID), "
                // + "FOREIGN KEY($TICKET_COLUMN_TECHNICIAN) REFERENCES $USER_TABLE_NAME($USER_COLUMN_ID), "
                + "$USER_COLUMNID TEXT);")
        //Method for executing our create table queries.
        db?.execSQL(createTblUser)
        db?.execSQL(createTblTechnician)
        db?.execSQL(createTblTicket)
        //Technicians are added here because the technicians are pre-existing and
        //the admin would create technicians and since we are not creating the admin side
        //we have added technicians below.
        val contentValues = ContentValues()
        contentValues.put(ticket_database_helper.TECHNICIAN_FIRST_NAME, "Jake")
        contentValues.put(ticket_database_helper.TECHNICIAN_LAST_NAME, "Ronalds")
        contentValues.put(ticket_database_helper.TECHNICIAN_EMAIL, "jr@gamil.com")
        contentValues.put(ticket_database_helper.TECHNICIAN_PASS, "Ronalds123#")
        contentValues.put(ticket_database_helper.TECHNICIAN_CELL, "Ronalds")

        val contentValues1 = ContentValues()
        contentValues1.put(ticket_database_helper.TECHNICIAN_FIRST_NAME, "Jake")
        contentValues1.put(ticket_database_helper.TECHNICIAN_LAST_NAME, "Ronalds")
        contentValues1.put(ticket_database_helper.TECHNICIAN_EMAIL, "jr@gamil.com")
        contentValues1.put(ticket_database_helper.TECHNICIAN_PASS, "Ronalds123#")
        contentValues1.put(ticket_database_helper.TECHNICIAN_CELL, "Ronalds")
        db?.insert(ticket_database_helper.TECHNICIAN, null,contentValues)
        db?.insert(ticket_database_helper.TECHNICIAN, null,contentValues1)

    }

    //When the version number of the database gets changed/incremented, the following function will run.
    //This will ensure compatibility between the old and new versions of the database.
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQueryUser = "DROP TABLE IF EXISTS ${ticket_database_helper.USER_TABLE_NAME}"    //drop or delete user table
        val dropTableQueryTicket = "DROP TABLE IF EXISTS ${ticket_database_helper.TICKET_TABLE_NAME}"    //drop or delete ticket table
        val dropTableQueryTechnician = "DROP TABLE IF EXISTS ${ticket_database_helper.TECHNICIAN}"    //drop or delete technician table

        db?.execSQL(dropTableQueryUser)
        db?.execSQL(dropTableQueryTicket)
        db?.execSQL(dropTableQueryTechnician)
        onCreate(db)
    }

    //The following function will get the user password, encrypt it and return the encrypted password.
    fun encryptThisString(pass: String): String {

        return try {

            //Hashing the passed password
            val md = MessageDigest.getInstance("SHA-1")
            val messageDigest = md.digest(pass.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"

            }

            //Passing the hashed password back wherever this function is called.
            hashtext

        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    fun main(args: Array<String>) {
        println("HashCode Generated by SHA-1 for: ")
        val s1 = "knowledgefactory.net"
        println("$s1 : ${encryptThisString(s1)}"
        )
    }

//Not applicable at this point
    //-----------------------------Technician table FUNCTIONS-----------------------------

    //Function: addUser to user table
    //Future project
//    fun add_technician(users : user_model): Boolean{
//        val db = this.writableDatabase
//        val values = ContentValues()
//        values.put(ticket_database_helper.USER_COLUMN_FIRST_NAME, users.first_name)
//        values.put(ticket_database_helper.USER_COLUMN_LAST_NAME, users.last_name)
//        values.put(ticket_database_helper.USER_COLUMN_EMAIL, users.email)
//        values.put(ticket_database_helper.USER_COLUMN_PASSWORD, users.password)
//        values.put(ticket_database_helper.USER_COLUMN_DEPARTMENT, users.department)
//        values.put(ticket_database_helper.USER_COLUMN_CELL_NUM, users.cell_num)
//
//        val _success = db.insert(ticket_database_helper.USER_TABLE_NAME, null, values)  //insert data into table
//
//        db.close()  //close datbase object
//        return (Integer.parseInt("$_success") != -1)
//    }

//-----------------------------USER table FUNCTIONS-----------------------------


    //Function: getAllUser fom user table
    @SuppressLint("Range")
    fun getAllUser(): List<user_model>{
        val userList = ArrayList<user_model>()
        val db = writableDatabase
        //Get user data
        val selectQuery = "SELECT * FROM ${ticket_database_helper.USER_TABLE_NAME}"

        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    //Pass user data to user model
                    val users = user_model()
                    users.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        ticket_database_helper.USER_COLUMN_ID
                    )))
                    users.first_name = cursor.getString(cursor.getColumnIndex(ticket_database_helper.USER_COLUMN_FIRST_NAME))
                    users.last_name = cursor.getString(cursor.getColumnIndex(ticket_database_helper.USER_COLUMN_LAST_NAME))
                    users.email = cursor.getString(cursor.getColumnIndex(ticket_database_helper.USER_COLUMN_EMAIL))
                    users.password = cursor.getString(cursor.getColumnIndex(ticket_database_helper.USER_COLUMN_PASSWORD))
                    users.department = cursor.getString(cursor.getColumnIndex(ticket_database_helper.USER_COLUMN_DEPARTMENT))
                    users.cell_num = cursor.getString(cursor.getColumnIndex(ticket_database_helper.USER_COLUMN_CELL_NUM))

                    userList.add(users) //add to userList list
                }while (cursor.moveToNext())
            }
        }
        cursor.close()  //close database object
        return userList //return UserModel object
    }

    //Function: addUser to user table
    fun addRecord(users : user_model): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        //Get inserted values
        var password = encryptThisString(users.password)
        values.put(ticket_database_helper.USER_COLUMN_FIRST_NAME, users.first_name)
        values.put(ticket_database_helper.USER_COLUMN_LAST_NAME, users.last_name)
        values.put(ticket_database_helper.USER_COLUMN_EMAIL, users.email)
        values.put(ticket_database_helper.USER_COLUMN_PASSWORD, password)
        values.put(ticket_database_helper.USER_COLUMN_DEPARTMENT, users.department)
        values.put(ticket_database_helper.USER_COLUMN_CELL_NUM, users.cell_num)

        //Create user
        val _success = db.insert(ticket_database_helper.USER_TABLE_NAME, null, values)  //insert data into table

        db.close()  //close datbase object
        return (Integer.parseInt("$_success") != -1)
    }

    //getUser overload with email parameter
    //Function: getUser to select data of a particular user_id
    fun getUser(_id: Int) : user_model{
        val users = user_model()
        val db = writableDatabase
        //Get specific user data
        val selectQuery = "SELECT * FROM ${ticket_database_helper.USER_TABLE_NAME} WHERE ${ticket_database_helper.USER_COLUMN_ID} = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst()
                //Pass user data to user model
                users.user_id =
                    Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_ID)))
                users.first_name =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_FIRST_NAME))
                users.last_name =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_LAST_NAME))
                users.email =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_EMAIL))
                users.password =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_PASSWORD))
                users.department =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_DEPARTMENT))
                users.cell_num =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_CELL_NUM))
            }
        }
        cursor.close()
        return users    //return UserModel object
    }

    //getUser overload with email parameter
    //Function: getUser to select data of a particular email
    fun getUser(_email: String) : user_model{
        val users = user_model()
        val db = writableDatabase
        //Get user with specific email
        val selectQuery = "SELECT * FROM ${ticket_database_helper.USER_TABLE_NAME} WHERE ${ticket_database_helper.USER_COLUMN_EMAIL} = '$_email'"
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor != null){
            if (cursor.moveToFirst()){
                //Pass user data to user model
                users.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_ID)))
                users.first_name = cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_FIRST_NAME))
                users.last_name = cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_LAST_NAME))
                users.email = cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_EMAIL))
                users.password = cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_PASSWORD))
                users.department = cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_DEPARTMENT))
                users.cell_num = cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_COLUMN_CELL_NUM))
            }
        }
        cursor.close()
        return users    //return UserModel object
    }

    //Function: deleteUser
    fun delete_user(_id: Int) : Boolean{
        val db = this.writableDatabase
        //Delete user with passed id
        val _success = db.delete(ticket_database_helper.USER_TABLE_NAME, "${ticket_database_helper.USER_COLUMN_ID}=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1 //Check if deletion was successful
    }

    //Function: updateUser
    fun updateRecord(users: user_model) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        //Encrypt the inserted password.
        var password = encryptThisString(users.password)
        //Insert new user data into user model
        values.put(ticket_database_helper.USER_COLUMN_FIRST_NAME, users.first_name)
        values.put(ticket_database_helper.USER_COLUMN_LAST_NAME, users.last_name)
        values.put(ticket_database_helper.USER_COLUMN_EMAIL, users.email)
        values.put(ticket_database_helper.USER_COLUMN_PASSWORD, password)
        values.put(ticket_database_helper.USER_COLUMN_DEPARTMENT, users.department)
        values.put(ticket_database_helper.USER_COLUMN_CELL_NUM, users.cell_num)

        //Update user data
        val _success = db.update(ticket_database_helper.USER_TABLE_NAME, values, "${ticket_database_helper.USER_COLUMN_ID}=?", arrayOf(users.user_id.toString())).toLong()

        db.close()
        return Integer.parseInt("$_success") != -1 //Check if update was successful
    }

    //This function will update the current user logged in status depending on if the user is logging in
    //If the user logged in status is set to true - logged in, false - not logged in
    fun setLoggedInStatus(status: String, user: Int) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ticket_database_helper.USER_LOGGED_STATUS, status)

        val _success = db.update(ticket_database_helper.USER_TABLE_NAME, values, "${ticket_database_helper.USER_COLUMN_ID}=?", arrayOf(user.toString())).toLong()

        db.close()
        return Integer.parseInt("$_success") != -1
    }

    //Check if user is logged in - if not user will be re-directed to login page
    fun getLoggedInStatus(user: Int) : Boolean{
        val users = user_model()
        val db = writableDatabase
        var logged_in = false
        var user_status = ""
        val selectQuery = "SELECT * FROM ${ticket_database_helper.USER_TABLE_NAME} WHERE ${ticket_database_helper.USER_COLUMN_ID} = '$user'"
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor != null){
            if (cursor.moveToFirst()){
                user_status = cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.USER_LOGGED_STATUS))
                if(user_status == "1"){
                    logged_in = true
                }

            }
        }
        cursor.close()
        return logged_in    //return user status object
    }


    //-----------------------------TICKET table FUNCTIONS-----------------------------



    //This function will get all tickets that have the inserted text, in the title column rows,
    // from the search bar in the current ticket page.
    fun get_all_searched_tickets(dataItem : CharSequence, CURRENTUSERID1 : String): ArrayList<ticket_model> {
        //The creation of the array list that will be returned to the recycler view.
        val ticket_list = ArrayList<ticket_model>()
        val ticket_title_string = dataItem.toString()
        //Get the tickets in a sorted format.
        val sorted_tickets = get_sorted_tickets(CURRENTUSERID1)
        val index = 0
        //The following loop loops through the sorted ticket id's.
        sorted_tickets.forEach {
            //The following if statement checks to see if any tickets have been inserted into the list that was
            //passed from the get sorted tickets function. If no tickets have been passed the user will receive the
            //following error message.
            if(sorted_tickets.size == 1 && it == 1011)
            {
                val tickets = ticket_model()
                tickets.ticket_priority =""
                tickets.ticket_title = "No Tickets Found"
                ticket_list.add(tickets)
                return ticket_list
            }
            else
            {
                //The below query will get the ticket record that matches the current ticket id
                //and the inserted text.
                //This record is then passed to the array that will populate the recyclerview.
                val db = writableDatabase
                val selectQuery = "SELECT * FROM $TICKET_TABLE_NAME WHERE $TICKET_COLUMN_ID == $it" +
                        " AND $TICKET_COLUMN_TITLE LIKE '%$ticket_title_string%' AND $USER_COLUMNID == $CURRENTUSERID1"
                val cursor = db.rawQuery(selectQuery, null)
                if(cursor != null)
                {
                    if(cursor.moveToFirst())
                    {
                        do {
                            val tickets = ticket_model()

                            //The following statements gets the row data specific to the column name as seen below.
                            tickets.ticket_id =
                                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_ID)))
                            tickets.ticket_priority =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_PRIORITY))
                            tickets.ticket_status =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_STATUS))
                            tickets.ticket_title =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_TITLE))
                            tickets.user_id =
                                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMNID)))
                            ticket_list.add(tickets)
                        }while(cursor.moveToNext())


                    }
                }
                else
                {
                    //This error will occur when no tickets where found.
                    val tickets = ticket_model()
                    tickets.ticket_priority =""
                    tickets.ticket_title = "No Tickets Found"
                    ticket_list.add(tickets)
                }
                cursor.close()
            }
        }
        //The list of tickets will be returned where this function is called with the below line of code.
        return ticket_list

    }


    //Function: addUser to user table
    @RequiresApi(Build.VERSION_CODES.O)
    fun addRecord(tickets : ticket_model): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        //get the priority number
        var ticket_prioritynum = 0
        if(tickets.ticket_priority == "High")
        {
            ticket_prioritynum = 1
        }
        else if(tickets.ticket_priority == "Medium")
        {
            ticket_prioritynum = 2
        }
        else if(tickets.ticket_priority == "Low")
        {
            ticket_prioritynum = 3
        }
        // Query to retrieve a random record
        val query = "SELECT * FROM ${ticket_database_helper.TECHNICIAN} ORDER BY RANDOM() LIMIT 1"
        val cursor = db.rawQuery(query, null)

        val current_date = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current_date.format(formatter)
//        Log.e("ppp", tickets.user_id.toString() + " - Update Ticket")
        values.put(ticket_database_helper.TICKET_COLUMN_TITLE, tickets.ticket_title)
        values.put(ticket_database_helper.TICKET_COLUMN_STATUS, tickets.ticket_status)
        values.put(ticket_database_helper.TICKET_COLUMN_PRIORITY, tickets.ticket_priority)
        values.put(ticket_database_helper.USER_COLUMNID, tickets.user_id)
        values.put(ticket_database_helper.TICKET_COLUMN_DATECREATED, formatted.toString())
        values.put(ticket_database_helper.TICKETPRIORITYNUM, ticket_prioritynum)
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                values.put(
                    ticket_database_helper.TICKET_COLUMN_TECHNICIAN,
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.TICKET_COLUMN_TECHNICIAN))
                )
            }
        }
        values.put(ticket_database_helper.TICKET_COLUMN_DESCRIPTION, tickets.ticket_description)

        val _success = db.insert(ticket_database_helper.TICKET_TABLE_NAME, null, values)  //insert data into table

        db.close()  //close datbase object
        return (Integer.parseInt("$_success") != -1)
    }



    //Function: getTicket to fetch the record of a particular user_id
    fun getTicket(_id: Int) : ticket_model{
        val tickets = ticket_model()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM ${ticket_database_helper.TICKET_TABLE_NAME} WHERE ${ticket_database_helper.TICKET_COLUMN_ID} = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                tickets.ticket_id =
                    Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.TICKET_COLUMN_ID)))
                tickets.ticket_title =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.TICKET_COLUMN_TITLE))
                tickets.ticket_status =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.TICKET_COLUMN_STATUS))
                tickets.ticket_priority =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.TICKET_COLUMN_PRIORITY))
                tickets.ticket_technician =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.TICKET_COLUMN_TECHNICIAN))
                tickets.ticket_description =
                    cursor.getString(cursor.getColumnIndexOrThrow(ticket_database_helper.TICKET_COLUMN_DESCRIPTION))
            }
        }

        cursor.close()
        return tickets    //return Ticket model object
    }



    //Function: update ticket
    fun updateRecord(tickets: ticket_model) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        var ticket_prioritynum = 0
        if(tickets.ticket_priority == "High")
        {
            ticket_prioritynum = 1
        }
        else if(tickets.ticket_priority == "Medium")
        {
            ticket_prioritynum = 2
        }
        else if(tickets.ticket_priority == "Low")
        {
            ticket_prioritynum = 3
        }


        values.put(TICKET_COLUMN_TITLE, tickets.ticket_title)
        values.put(TICKET_COLUMN_PRIORITY, tickets.ticket_priority)
        values.put(TICKETPRIORITYNUM, ticket_prioritynum)
        values.put(TICKET_COLUMN_DESCRIPTION, tickets.ticket_description)

        val _success = db.update(TICKET_TABLE_NAME, values, "$TICKET_COLUMN_ID=?", arrayOf(tickets.ticket_id.toString())).toLong()

        return Integer.parseInt("$_success") != -1
       // return success
    }

    //Function: deleteTicket
    fun delete_ticket(_id: Int) : Boolean{
        val db = this.writableDatabase
        val _success = db.delete(TICKET_TABLE_NAME, "$TICKET_COLUMN_ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    //The below class will get the sorted list of tickets, then use that list to insert the tickets in the sorted
    //format. This list will then be used to update the landing pages' recyclerview with all active tickets.
    fun get_all_tickets(CURRENTUSERID1 : String): ArrayList<ticket_model> {
        //The following array will be used to update the recyclerview
        val ticketList = ArrayList<ticket_model>()

        //The below query will get the ticket record that matches the current ticket id.
        //This record is then passed to the array that will populate the recyclerview.
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TICKET_TABLE_NAME WHERE $USER_COLUMNID == $CURRENTUSERID1"

        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do{
                    val tickets = ticket_model()
//
                    //The following statements gets the row data specific to the column name as seen below.
                    tickets.ticket_id =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_ID)))
                    tickets.ticket_priority =
                        cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_PRIORITY))
                    tickets.ticket_title =
                        cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_TITLE))
                    tickets.ticket_status =
                        cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_STATUS))
                    tickets.user_id =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMNID)))

                    ticketList.add(tickets)
                }while(cursor.moveToNext())
            }
        }
        else
        {
            //This error will occur when no tickets where found.
            val tickets = ticket_model()
            tickets.ticket_priority =""
            tickets.ticket_title = "No Tickets Found"
            ticketList.add(tickets)
        }
        cursor.close()

        return ticketList
    }

    //The below class will get the sorted list of tickets, then use that list to insert the tickets in the sorted
    //format. This list will then be used to update the landing pages' recyclerview with all active tickets.
    fun get_priority_sorted_tickets(CURRENTUSERID1 : String): ArrayList<ticket_model> {
        //The following array will be used to update the recyclerview
        val ticketList = ArrayList<ticket_model>()

        //The following value gets the sorted list of tickets from the get sorted tickets function.
        val sorted_tickets = get_sorted_tickets(CURRENTUSERID1)

        //The following loop loops through the sorted ticket id's.
        sorted_tickets.forEach {
            //The following if statement checks to see if any tickets have been inserted into the list that was
            //passed from the get sorted tickets function. If no tickets have been passed the user will receive the
            //following error message.
            if(sorted_tickets.size == 1 && it == 1011)
            {
                val tickets = ticket_model()
                tickets.ticket_priority =""
                tickets.ticket_title = "No Tickets Found"
                ticketList.add(tickets)
                return ticketList
            }
            else
            {
                //The below query will get the ticket record that matches the current ticket id.
                //This record is then passed to the array that will populate the recyclerview.
                val db = writableDatabase
                val selectQuery = "SELECT * FROM $TICKET_TABLE_NAME WHERE $TICKET_COLUMN_ID == $it AND $USER_COLUMNID == $CURRENTUSERID1"
                val cursor = db.rawQuery(selectQuery, null)
                if(cursor != null)
                {
                    if(cursor.moveToFirst())
                    {
                        do {
                            val tickets = ticket_model()
                            //The following statements gets the row data specific to the column name as seen below.
                            tickets.ticket_id =
                                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_ID)))
                            tickets.ticket_priority =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_PRIORITY))
                            tickets.ticket_title =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_TITLE))
                            tickets.ticket_status =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_STATUS))
                            tickets.user_id =
                                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(
                                    USER_COLUMNID)))
                            ticketList.add(tickets)
                        }while(cursor.moveToNext())
                    }
                }
                else
                {
                    //This error will occur when no tickets where found.
                    val tickets = ticket_model()
                    tickets.ticket_priority =""
                    tickets.ticket_title = "No Tickets Found"
                    ticketList.add(tickets)
                }
                cursor.close()
            }
        }
        //The list of tickets will be returned where this function is called with the below line of code.
        return ticketList
    }

    //This function will insert all the ticket numbers into a list and then pass that list to the bubble sort
    //function.
    fun get_sorted_tickets(CURRENTUSERID1 : String): java.util.ArrayList<Int> {
        //The creation of the array lists that will be passed to the bubble sort algorithm
        val priorityList = ArrayList<Int>()
        val ticketId = ArrayList<Int>()
        val db = writableDatabase
        //Get all tickets in database
        val selectQuery = "SELECT * FROM $TICKET_TABLE_NAME WHERE $USER_COLUMNID == $CURRENTUSERID1"

        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    var tickets1 = 0
                    var tickets2 = 0
                    //Insert ticket priority numbers and ticket id's into array list.
                    tickets1 =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_ID)))
                    tickets2 =
                        cursor.getInt(cursor.getColumnIndexOrThrow(TICKETPRIORITYNUM))

                    priorityList.add(tickets2)
                    ticketId.add(tickets1)
                }while(cursor.moveToNext())
            }
            //As can be seen below, the list of tickets will then be passed to the bubble sort algorithm to be sorted.
            return bubble_sort_tickets(priorityList,ticketId)
        }
        else
        {
            ticketId.add(1011)
        }

        cursor?.close()
        return ticketId
    }

    //The following bubble sort algorithm will sort the list of priority numbers in an descending order.
    fun bubble_sort_tickets(ticket_priority_num : ArrayList<Int>, ticket_id : ArrayList<Int>): ArrayList<Int> {
        val length = ticket_priority_num.size
        for (i in 0 until (length - 1)) {
            for (j in 0 until (length - i - 1)) {
                //This if statement will compare the current and next numbers and the placements will be
                //switched if necessary.
                if (ticket_priority_num[j].compareTo(ticket_priority_num[j + 1]) > 0) {
                    //sort priority list
                    val temp = ticket_priority_num[j]
                    ticket_priority_num[j] = ticket_priority_num[j + 1]
                    ticket_priority_num[j + 1] = temp
                    //as priority list gets sorted the ticket id list then gets sorted, the ticket id
                    //list will be used to insert the data correctly
                    val temp1 = ticket_id[j]
                    ticket_id[j] = ticket_id[j + 1]
                    ticket_id[j + 1] = temp1
                }
            }
        }
        return ticket_id
    }

    //The below class will get the sorted list of tickets, then use that list to insert the tickets in the sorted
    //format. This list will then be used to update the landing pages' recyclerview with all active tickets.
    @RequiresApi(Build.VERSION_CODES.O)
    fun get_date_sorted_tickets(CURRENTUSERID1 : String): ArrayList<ticket_model> {
        //The following array will be used to update the recyclerview
        val ticketList = ArrayList<ticket_model>()

        //The following value gets the sorted list of tickets from the get sorted tickets function.
        val sorted_tickets = get_date_tickets(CURRENTUSERID1)

        //The following loop loops through the sorted ticket id's.
        sorted_tickets.forEach {
            //The following if statement checks to see if any tickets have been inserted into the list that was
            //passed from the get sorted tickets function. If no tickets have been passed the user will receive the
            //following error message.
            if(sorted_tickets.size == 1 && it == 1011)
            {
                val tickets = ticket_model()
                tickets.ticket_priority =""
                tickets.ticket_title = "No Tickets Found"
                ticketList.add(tickets)
                return ticketList
            }
            else
            {
                //The below query will get the ticket record that matches the current ticket id.
                //This record is then passed to the array that will populate the recyclerview.
                val db = writableDatabase
                val selectQuery = "SELECT * FROM $TICKET_TABLE_NAME WHERE $TICKET_COLUMN_ID == $it AND $USER_COLUMNID == $CURRENTUSERID1"
                val cursor = db.rawQuery(selectQuery, null)
                if(cursor != null)
                {
                    if(cursor.moveToFirst())
                    {
                        do {
                            val tickets = ticket_model()
                            //The following statements gets the row data specific to the column name as seen below.
                            tickets.ticket_id =
                                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_ID)))
                            tickets.ticket_priority =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_PRIORITY))
                            tickets.ticket_title =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_TITLE))
                            tickets.ticket_status =
                                cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_STATUS))
                            ticketList.add(tickets)
                            tickets.user_id =
                                Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(
                                    USER_COLUMNID)))
                        }while(cursor.moveToNext())
                    }
                }
                else
                {
                    //This error will occur when no tickets where found.
                    val tickets = ticket_model()
                    tickets.ticket_priority =""
                    tickets.ticket_title = "No Tickets Found"
                    ticketList.add(tickets)
                }
                cursor.close()
            }
        }
        //The list of tickets will be returned where this function is called with the below line of code.
        return ticketList

    }

    //This function will insert all the ticket numbers into a list and then pass that list to the bubble sort
    //function.
    @RequiresApi(Build.VERSION_CODES.O)
    fun get_date_tickets(CURRENTUSERID1 : String): java.util.ArrayList<Int> {
        //The creation of the array lists that will be passed to the bubble sort algorithm
        val priorityList = ArrayList<Date>()
        val ticketId = ArrayList<Int>()
        val db = writableDatabase
        //Get all tickets in database
        val selectQuery = "SELECT * FROM $TICKET_TABLE_NAME WHERE $USER_COLUMNID == $CURRENTUSERID1"
        val pattern = "yyyy-MM-dd"
       // val formatter = DateTimeFormatter.ofPattern(pattern)
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    var tickets1 = 0
                    var tickets2 = ""
                    //Insert ticket priority numbers and ticket id's into array list.
                    tickets1 =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_ID)))
                    tickets2 =
                        cursor.getString(cursor.getColumnIndexOrThrow(TICKET_COLUMN_DATECREATED))

                    priorityList.add(formatter.parse(tickets2))
                    ticketId.add(tickets1)
                }while(cursor.moveToNext())
            }
            //As can be seen below, the list of tickets will then be passed to the bubble sort algorithm to be sorted.
            return bubble_sort_ticket_dates(priorityList,ticketId)
        }
        else
        {
            ticketId.add(1011)
        }

        cursor?.close()
        return ticketId
    }

    //The following bubble sort algorithm will sort the list of priority numbers in an descending order.
    fun bubble_sort_ticket_dates(date_values : ArrayList<Date>, ticket_id : ArrayList<Int>): ArrayList<Int> {
        val length = date_values.size
        for (i in 0 until (length - 1)) {
            for (j in 0 until (length - i - 1)) {
                //This if statement will compare the current and next numbers and the placements will be
                //switched if necessary.
                if (date_values[j] < date_values[j + 1]) {
                    //sort date list
                    val temp = date_values[j]
                    date_values[j] = date_values[j + 1]
                    date_values[j + 1] = temp
                    //as date list gets sorted the ticket id list then gets sorted, the ticket id
                    //list will be used to insert the data correctly
                    val temp1 = ticket_id[j]
                    ticket_id[j] = ticket_id[j + 1]
                    ticket_id[j + 1] = temp1
                }
            }
        }
        return ticket_id
    }
}
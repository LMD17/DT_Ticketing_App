package com.example.ticketingapplication

//Imports
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

//Class activity_create_account
class activity_create_account : AppCompatActivity() {

    //The initialization and creation of variables
    private lateinit var textview_create_account: TextView
    private lateinit var button_signup: Button
    private lateinit var button_login: Button
    private lateinit var button_back: Button
    private lateinit var edittext_first_name: EditText
    private lateinit var edittext_last_name: EditText
    private lateinit var edittext_email: EditText
    private lateinit var edittext_password: EditText
    private lateinit var edittext_confirm_password: EditText
    private lateinit var edittext_department: EditText
    private lateinit var edittext_cell_num: EditText
    private lateinit var create_account: TextView
    private lateinit var share_preferences: SharedPreferences
    private lateinit var containerRL: ConstraintLayout
    private var dbHandler: ticket_database_helper? = null

    //Function to check if a valid email address has been entered
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")    //create regular expression
        return emailRegex.matches(email)    //return result true/false
    }

    //Function to check if a valid cell number has been entered
    fun isValidCellNum(cell: String): Boolean {
        val regex = Regex("^\\+[2][7]\\d{9}\$") //number starts with +27 followed by 9 digits
        return regex.matches(cell)
    }

    //Function to check if a valid password has been entered
    fun isValidPassword(password: String): Boolean {
        //Declare and implement variables
        val minLength = 8
        val maxLength = 15

        //check length of password
        if (password.length !in minLength..maxLength) {
            return false
        }

        val containsUppercase = password.any { it.isUpperCase() }   //check that password includes uppercase
        val containsLowercase = password.any { it.isLowerCase() }   //check that password includes lowercase
        val containsDigit = password.any { it.isDigit() }   //check that password include digit

        return (containsUppercase && containsLowercase && containsDigit)    //return result true/false
    }

    //Function to create a dialog
    fun showOkDialog(context: Context, title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)    //set title
            .setMessage(message)    //set message
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()    //dismiss dialog when OK is clicked
            }
            .create()   //create dialog

        alertDialog.show()  //show dialog
    }

    //Override onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_create_account)  //set content view

        //link variables to views
        button_signup = findViewById(R.id.button_sign_up)
        button_login = findViewById(R.id.button_login)
        textview_create_account = findViewById(R.id.textview_create_account)
        edittext_first_name = findViewById(R.id.edittext_first_name)
        edittext_last_name = findViewById(R.id.edittext_last_name)
        edittext_email = findViewById(R.id.edittext_email)
        edittext_password = findViewById(R.id.edittext_password)
        edittext_confirm_password = findViewById(R.id.edittext_confirm_password)
        edittext_department = findViewById(R.id.edittext_department)
        edittext_cell_num = findViewById(R.id.edittext_cell_num)
        create_account = findViewById(R.id.textview_already_have_account)
        button_back = findViewById(R.id.button_back)

        //link variable to ticket_database_helper
        dbHandler = ticket_database_helper(this)

        //Declare variable and initialise
        var isEditMode: Boolean = false

        //if Mode 'E' then user selected to edit data and we must update data in the database
        if (intent != null && intent.getStringExtra("Mode") == "E") {

            //set visibility of certain views
            create_account.visibility = View.INVISIBLE
            button_login.visibility = View.INVISIBLE
            button_back.visibility = View.VISIBLE

            //onClickListener for back button
            button_back.setOnClickListener {
                //redirect to activity_login
                val i = Intent(this, activity_profile::class.java)
                i.putExtra("USERID", intent.getIntExtra("USERID", -1))    //pass user_id in intent
                startActivity(i)   //start activity
            }

            //set edit mode to true
            isEditMode = true

            //change title and button text
            textview_create_account.text = "Update Account"
            button_signup.text = "Update"

            //get user account details using getUser function in ticket_database_helper
            val users: user_model = dbHandler!!.getUser(intent.getIntExtra("USERID", -1))

            //Populate editText fields when user is updating their account/profile
            edittext_first_name.setText((users.first_name))
            edittext_last_name.setText((users.last_name))
            edittext_email.setText((users.email))
            //edittext_password.setText((users.password))
            //edittext_confirm_password.setText((users.password))
            edittext_department.setText((users.department))
            edittext_cell_num.setText((users.cell_num))

        } else {
            //else user selected to signup and we must insert data
            isEditMode = false  //set edit mode to true
            //change title and button text
            textview_create_account.text = "Create Account"
            button_signup.text = "Sign-Up"
        }

        //onClickListener for signup button
        button_signup.setOnClickListener {
            //Declare and initialise variables
            var success: Boolean = false
            val users: user_model = user_model()

            //check that user actually entered data
            if (edittext_first_name.text.isBlank() || edittext_last_name.text.isBlank() || edittext_email.text.isBlank()
                || edittext_password.text.isBlank() || edittext_confirm_password.text.isBlank()
                || edittext_department.text.isBlank() || edittext_cell_num.text.isBlank()) {

                showOkDialog(this, "Alert", "Please make sure to complete each field.") //display dialog with alert message
            } else if (edittext_password.text.toString() != edittext_confirm_password.text.toString()) {  //check that password and confirm password fields match
                showOkDialog(this, "Alert", "Password fields must match.")  //display dialog with alert message
            } else if (!isValidEmail(edittext_email.text.toString())) {  //check if a valid email has been entered
                showOkDialog(this, "Alert", "Invalid email entered.\n\n" +
                        "Please enter a valid email such as \n" +
                        "abc@gmail.com")  //display dialog with alert message
            } else if (!isValidPassword(edittext_password.text.toString())) {  //check if a valid password has been entered
                showOkDialog(this, "Alert", "Invalid password entered.\n\n" +
                        "Your password must meet the following minimum requirements:\n" +
                        "- 8-15 characters\n" +
                        "- Uppercase\n" +
                        "- Lowercase\n" +
                        "- Digit")  //display dialog with alert message
            }else if (!isValidCellNum(edittext_cell_num.text.toString())) {
                showOkDialog(this, "Alert", "Invalid cell number entered.\n\n" +
                        "Phone number must start with +27 followed by 9 digits.")  //display dialog with alert message
            }else {

                //check if user wants to insert or edit?
                if (isEditMode) {   //user is updating their account

                    users.user_id = intent.getIntExtra("USERID", -1) //get id of this user

                    //Update record with data in editText fields
                    users.first_name = edittext_first_name.text.toString()
                    users.last_name = edittext_last_name.text.toString()
                    users.email = edittext_email.text.toString()
                    users.password = edittext_password.text.toString()
                    users.department = edittext_department.text.toString()
                    users.cell_num = edittext_cell_num.text.toString()
                    button_login.visibility = View.INVISIBLE
                    create_account.visibility = View.INVISIBLE

                    success = dbHandler?.updateRecord(users) as Boolean //Returns true if the data was update successfully
                    //this updateRecord method calls the method in the ticket_database_helper to update the data in the database.

                    //If success true --> redirect to activity_login
                    if (success) {
                        //notify user that account was created
                        Toast.makeText(applicationContext, "Account Updated", Toast.LENGTH_SHORT).show()
                        val i = Intent(applicationContext, activity_login::class.java)
                        startActivity(i)
                        finish()    //to end this activity
                    } else {
                        //notify user that an error occurred when creating account
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong when processing user details!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {    //else user is creating an account
                    //Insert user details in database
                    users.first_name = edittext_first_name.text.toString()
                    users.last_name = edittext_last_name.text.toString()
                    users.email = edittext_email.text.toString()
                    users.password = edittext_password.text.toString()
                    users.department = edittext_department.text.toString()
                    users.cell_num = edittext_cell_num.text.toString()

                    success = dbHandler?.addRecord(users) as Boolean //Returns true if the data was inserted successfully
                    //addRecord method calls the method in the ticket_database_helper to add the data to the database.
                    //If success true --> redirect to activity_login
                    if (success) {
                        //notify user that account was created
                        Toast.makeText(applicationContext, "Account Created", Toast.LENGTH_SHORT).show()
                        val i = Intent(applicationContext, activity_login::class.java)
                        startActivity(i)
                        finish()    //to end this activity
                    } else {
                        //notify user that an error occurred when creating account
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong when processing user details!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }

        //onClickListener for login button
        button_login.setOnClickListener {
            //redirect to activity_login
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)   //start activity
        }
    }
}
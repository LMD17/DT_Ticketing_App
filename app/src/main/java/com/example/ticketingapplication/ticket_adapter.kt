package com.example.ticketingapplication

//Imports
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Class ticket_adapter
class ticket_adapter(internal var context : Context) : RecyclerView.Adapter<ticket_adapter.TicketViewHolder>(){
    //Initiate ticket fields required in recycler view
    private var tickList: ArrayList<ticket_model> = ArrayList()
    private var onClickItem:((ticket_model)->Unit)? = null
    private var onClickDeleteItem:((ticket_model)->Unit)? = null

    //Add tickets to list
    fun addItems(items: ArrayList<ticket_model>){
        this.tickList = items
        notifyDataSetChanged()
    }

    //When this class is called, the below function will run.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ticket_line_items, parent, false)
        return TicketViewHolder(view)
    }

    //This will be the holder for each of the tickets that gets displayed
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickList[position]
        holder.bindView(ticket)

        //This button on click listener re-directs the user to the ticket detail page
        holder.ticketBtn.setOnClickListener{
            val i = Intent(context, activity_ticket_detail::class.java)
            i.putExtra("TICKETID", ticket.ticket_id)    //pass ticket_id
            i.putExtra("USERID", ticket.user_id)    //pass user_id
            Log.e("ppp", ticket.user_id.toString() + " - Update Ticket")
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    //get the count of items in the ticketList
    override fun getItemCount(): Int {
        return tickList.size
    }

    //The below class sets the variables in the ticket line items equal to the data that is grabbed form the database
    class TicketViewHolder(var view: View): RecyclerView.ViewHolder(view)
    {
        //link variables to views and button
        var ticketBtn = view.findViewById<Button>(R.id.button_ticket)
        private var title = view.findViewById<TextView>(R.id.textview_ticket_title)
        private var priority = view.findViewById<TextView>(R.id.textview_ticket_priority)
        private var status = view.findViewById<TextView>(R.id.textview_ticket_status)


        //bind data from the database to the different views and button
        fun bindView(tick:ticket_model)
        {
            //Set the tickets that get displayed to have specific data from database
            ticketBtn.text = tick.ticket_id.toString()
            title.text = tick.ticket_title
            priority.text = tick.ticket_priority
            status.text = tick.ticket_status
        }
    }
}
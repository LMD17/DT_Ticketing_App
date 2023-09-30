package com.example.ticketingapplication

//Imports
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Class troubleshooting_adapter
class troubleshooting_adapter : RecyclerView.Adapter<troubleshooting_adapter.TicketViewHolder>() {
    //Initiate ticket fields required in recycler view
    private var troubleshooting_options: ArrayList<String> = ArrayList()
    private var onClickItem:((ticket_model)->Unit)? = null
    private var onClickDeleteItem:((ticket_model)->Unit)? = null

    //Add troubleshooting options to list
    fun addItems(items: ArrayList<String>){
        this.troubleshooting_options = items
        notifyDataSetChanged()
    }

    //When this class is called, the below function will run.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.troubleshooting_options, parent, false)
        return TicketViewHolder(view)
    }

    //This will be the holder for each of the troubleshooting options that gets displayed
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val trouble = troubleshooting_options[position]
        holder.bindView(trouble)

    }

    //get the count of items in the ticketList
    override fun getItemCount(): Int {
        return troubleshooting_options.size
    }

    //The below function sets the variables in the ticket line items equal to the data that is grabbed form the database
    class TicketViewHolder(var view: View): RecyclerView.ViewHolder(view)
    {
        //link variable to view
        private var option = view.findViewById<TextView>(R.id.troubleshoot_option)

        //bind data from the database to the view
        fun bindView(tick:String)
        {
            option.text = tick
        }
    }
}
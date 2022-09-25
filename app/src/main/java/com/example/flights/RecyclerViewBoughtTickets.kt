package com.example.flights

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.flights.databinding.BoughtTicketsItemBinding

class RecyclerViewBoughtTickets: RecyclerView.Adapter<RecyclerViewBoughtTickets.TicketsHolder2>() {
    var ticketList=ArrayList<Ticket1>()
    class TicketsHolder2(item: View): RecyclerView.ViewHolder(item) {
        val binding = BoughtTicketsItemBinding.bind(item)
        @RequiresApi(Build.VERSION_CODES.O)
//        @SuppressLint("SetTextI18n")
        fun bind(Ticket: Ticket1) = with(binding){
            text21.text=Ticket.companyTicket
            text22.text=Ticket.dataText
            text23.text=Ticket.timeText
            text24.text=Ticket.humansText
            text25.text=Ticket.priceText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsHolder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bought_tickets_item, parent, false)
        return TicketsHolder2(view)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TicketsHolder2, position: Int) {
        holder.bind(ticketList[position])
    }
    override fun getItemCount(): Int {
        return ticketList.size
    }
    fun addTicket(ticket: Ticket1){
        ticketList.add(ticket)
        notifyDataSetChanged()
    }
    fun removeTickets(){
        ticketList=ArrayList<Ticket1>()
    }
}
data class Ticket1(
    val companyTicket: String,
    val dataText: String,
    val timeText: String,
    val humansText:String,
    val priceText: String,
)
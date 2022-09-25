package com.example.flights

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.flights.databinding.TicketItemBinding
import org.json.JSONArray

class RecyclerViewTickets: RecyclerView.Adapter<RecyclerViewTickets.TicketsHolder>() {
    var ticketList=ArrayList<Ticket>()
    class TicketsHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = TicketItemBinding.bind(item)
//
//        init {
//            binding.button5.setOnClickListener { listener?.invoke(holder[adapterPosition]) }
//        }
//
//        private var listener: ((item: RecyclerViewTickets) -> Unit)? = null
//
//        fun setOnItemClickListener(listener: (item: RecyclerViewTickets) -> Unit) {
//            this.listener = listener
//        }

        @SuppressLint("SetTextI18n")
        fun bind(Ticket: Ticket) = with(binding){
            button5.setOnClickListener {
                ScrollingActivity().BuyTicket(Ticket.idFlight, Ticket.humans, Ticket.flightClass, Ticket.id_user, Ticket.appContext, Ticket.context)
            }
            companyText.text="Компания: ${Ticket.companyTicket}"
            fromCountryText.text="Вылет: ${Ticket.fromCountryText}"
            fromCityText.text="Откуда: ${Ticket.fromCityText}"
            fromAirportText.text="Аэропорт: ${Ticket.fromAirportText}"
            timeFlightText.text="Время полета: ${Ticket.timeFlightText}"
            toCountryText.text="Прилет: ${Ticket.toCountryText}"
            toCityText.text="Куда: ${Ticket.toCityText}"
            toAirportText.text="Аэропорт: ${Ticket.toAirportText}"
            weightText.text="Допустимый вес ручной клади: ${Ticket.weightText} кг"
            priceTicketText.text="Цена за человека: ${Ticket.priceTicketText}р"
            button5.text="Купить за ${Ticket.priceTicketText.toDouble()*Ticket.humans.toInt()}р"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_item, parent, false)
        return TicketsHolder(view)
    }

    override fun onBindViewHolder(holder: TicketsHolder, position: Int) {
        holder.bind(ticketList[position])
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }
    fun addTicket(ticket: Ticket){
        ticketList.add(ticket)
        notifyDataSetChanged()
    }
    fun removeTickets(){
        ticketList=ArrayList<Ticket>()
        notifyDataSetChanged()
    }
}
data class Ticket(
    val context: Context,
    val appContext: Context,
    val id_user: String?,
    val flightClass: String,
    val companyTicket: String,
    val fromCountryText: String,
    val fromCityText: String,
    val fromAirportText:String,
    val timeFlightText: String,
    val toCountryText:String,
    val toCityText:String,
    val toAirportText:String,
    val weightText:String,
    val priceTicketText: String,
    val idFlight: String,
    val humans: String)
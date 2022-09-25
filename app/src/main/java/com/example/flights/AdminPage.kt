package com.example.flights

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.flights.databinding.ActivityAdminPageBinding

class AdminPage : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAdminPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.NavMenu.menu.findItem(R.id.addFlight).isVisible=true
        binding.NavMenu.menu.findItem(R.id.profile).isEnabled=true
        binding.NavMenu.menu.findItem(R.id.aut_page).isEnabled=false
        binding.NavMenu.menu.findItem(R.id.reg_page).isEnabled=false

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Добавление нового рейса"
        binding.NavMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.main_page -> {
                    startActivity(Intent(this, ScrollingActivity::class.java))
                    binding.drawer.closeDrawer(GravityCompat.END)
                }
                R.id.close -> {
//                    startActivity(Intent(this, ScrollingActivity::class.java))
                    binding.drawer.closeDrawer(GravityCompat.END)
                    this.finishAffinity()
                }
                R.id.profileExit -> {
                    UpdateDB(this).deleteDB(null,null)
                    startActivity(Intent(this, ScrollingActivity::class.java))
//                    this.recreate()
                }
                R.id.profile ->{
                    startActivity(Intent(this, ProfileActivity::class.java))
                    binding.drawer.closeDrawer(GravityCompat.END)
                }
            }
            true
        }
        val button: Button = findViewById(R.id.button3)
        button.setOnClickListener(this)
    }
    override fun onBackPressed() {
//        moveTaskToBack(true)
        startActivity(Intent(this, ScrollingActivity::class.java))
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> startActivity(Intent(this, ScrollingActivity::class.java))//moveTaskToBack(true)//finish()
            R.id.nav -> {
                if(binding.drawer.isDrawerOpen(GravityCompat.END))
                    binding.drawer.closeDrawer(GravityCompat.END)
                else binding.drawer.openDrawer(GravityCompat.END)
            }
        }
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.button3 -> validate()
        }
    }

    private fun validate(){
        val Departure_Date: EditText = findViewById(R.id.editTextTextPersonName5)
        val Arrival_Date: EditText = findViewById(R.id.editTextTextPersonName14)
        val Departure_Time: EditText = findViewById(R.id.editTextTextPersonName15)
        val Arrival_Time: EditText = findViewById(R.id.editTextTextPersonName16)
        val Departure_City: EditText = findViewById(R.id.editTextTextPersonName17)
        val Arrival_City: EditText = findViewById(R.id.editTextTextPersonName18)
        val Departure_Airport: EditText = findViewById(R.id.editTextTextPersonName19)
        val Hand_Luggage_Weight: EditText = findViewById(R.id.editTextTextPersonName20)
        val Company: EditText = findViewById(R.id.editTextTextPersonName21)
        val Price: EditText = findViewById(R.id.editTextTextPersonName22)
        val Business_Class_Price: EditText = findViewById(R.id.editTextTextPersonName23)
        val Max_Passengers: EditText = findViewById(R.id.editTextTextPersonName24)
        val Flight_Time: EditText = findViewById(R.id.editTextTextPersonName25)
        val Arrival_Country: EditText = findViewById(R.id.editTextTextPersonName26)
        val Departure_Country: EditText = findViewById(R.id.editTextTextPersonName27)
        val Arrival_Airport: EditText = findViewById(R.id.editTextTextPersonName28)
        when {
            Company.length()==0 -> { Company.setError("Поле не может быть пустым"); Company.requestFocus() }
            Price.length()==0 -> { Price.setError("Поле не может быть пустым"); Price.requestFocus() }
            Business_Class_Price.length()==0 -> { Business_Class_Price.setError("Поле не может быть пустым"); Business_Class_Price.requestFocus() }
            Max_Passengers.length()==0 -> { Max_Passengers.setError("Поле не может быть пустым"); Max_Passengers.requestFocus() }
            Flight_Time.length()==0 -> { Flight_Time.setError("Поле не может быть пустым"); Flight_Time.requestFocus() }
            Arrival_Country.length()==0 -> { Arrival_Country.setError("Поле не может быть пустым"); Arrival_Country.requestFocus() }
            Departure_Country.length()==0 -> { Departure_Country.setError("Поле не может быть пустым"); Departure_Country.requestFocus() }
            Arrival_Airport.length()==0 -> { Arrival_Airport.setError("Поле не может быть пустым"); Arrival_Airport.requestFocus() }
            Departure_Airport.length()==0 -> { Departure_Airport.setError("Поле не может быть пустым"); Departure_Airport.requestFocus() }
            Arrival_City.length()==0 -> { Arrival_City.setError("Поле не может быть пустым"); Arrival_City.requestFocus() }
            Departure_City.length()==0 -> { Departure_City.setError("Поле не может быть пустым"); Departure_City.requestFocus() }
            Departure_Date.length()==0 -> { Departure_Date.setError("Поле не может быть пустым"); Departure_Date.requestFocus() }
            Arrival_Date.length()==0 -> { Arrival_Date.setError("Поле не может быть пустым"); Arrival_Date.requestFocus() }
            Departure_Time.length()==0 -> { Departure_Time.setError("Поле не может быть пустым"); Departure_Time.requestFocus() }
            Arrival_Time.length()==0 -> { Arrival_Time.setError("Поле не может быть пустым"); Arrival_Time.requestFocus() }
            Hand_Luggage_Weight.length()==0 -> { Hand_Luggage_Weight.setError("Поле не может быть пустым"); Hand_Luggage_Weight.requestFocus() }
            else -> NewFlight()
        }
    }
    private fun NewFlight() {
        val Departure_Date: EditText = findViewById(R.id.editTextTextPersonName5)
        val Arrival_Date: EditText = findViewById(R.id.editTextTextPersonName14)
        val Departure_Time: EditText = findViewById(R.id.editTextTextPersonName15)
        val Arrival_Time: EditText = findViewById(R.id.editTextTextPersonName16)
        val Departure_City: EditText = findViewById(R.id.editTextTextPersonName17)
        val Arrival_City: EditText = findViewById(R.id.editTextTextPersonName18)
        val Departure_Airport: EditText = findViewById(R.id.editTextTextPersonName19)
        val Hand_Luggage_Weight: EditText = findViewById(R.id.editTextTextPersonName20)
        val Company: EditText = findViewById(R.id.editTextTextPersonName21)
        val Price: EditText = findViewById(R.id.editTextTextPersonName22)
        val Business_Class_Price: EditText = findViewById(R.id.editTextTextPersonName23)
        val Max_Passengers: EditText = findViewById(R.id.editTextTextPersonName24)
        val Flight_Time: EditText = findViewById(R.id.editTextTextPersonName25)
        val Arrival_Country: EditText = findViewById(R.id.editTextTextPersonName26)
        val Departure_Country: EditText = findViewById(R.id.editTextTextPersonName27)
        val Arrival_Airport: EditText = findViewById(R.id.editTextTextPersonName28)
        val queue = Volley.newRequestQueue(applicationContext)
        println(this)
        val url = "https://thisistesttoo.000webhostapp.com/createflights.php"
        val stringRequest = object: StringRequest(
            Request.Method.POST, url,
            { response ->
                println(response)
                if(response.toString()=="true") {
                    Toast.makeText(this, "Рейс добавлен", Toast.LENGTH_LONG).show()
                    Price.setText("")
                    Company.setText("")
                    Business_Class_Price.setText("")
                    Max_Passengers.setText("")
                    Flight_Time.setText("")
                    Arrival_Country.setText("")
                    Departure_Country.setText("")
                    Arrival_Airport.setText("")
                    Departure_Airport.setText("")
                    Arrival_City.setText("")
                    Departure_City.setText("")
                    Departure_Date.setText("")
                    Arrival_Date.setText("")
                    Departure_Time.setText("")
                    Arrival_Time.setText("")
                    Hand_Luggage_Weight.setText("")
                }
                else
                {
                    Toast.makeText(this, "Не удалось добавить рейс", Toast.LENGTH_LONG).show()
                }
            },
            { Toast.makeText(this, "Подключение к серверу отсутсвует", Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Price"]= Price.text.toString()
                headers["Company"]= Company.text.toString()
                headers["Business_Class_Price"]= Business_Class_Price.text.toString()
                headers["Max_Passengers"]= Max_Passengers.text.toString()
                headers["Flight_Time"]= Flight_Time.text.toString()
                headers["Arrival_Country"]= Arrival_Country.text.toString()
                headers["Departure_Country"]= Departure_Country.text.toString()
                headers["Arrival_Airport"]= Arrival_Airport.text.toString()
                headers["Departure_Airport"]= Departure_Airport.text.toString()
                headers["Arrival_City"]= Arrival_City.text.toString()
                headers["Departure_City"]= Departure_City.text.toString()
                headers["Departure_Date"]= Departure_Date.text.toString()
                headers["Arrival_Date"]= Arrival_Date.text.toString()
                headers["Departure_Time"]= Departure_Time.text.toString()
                headers["Arrival_Time"]= Arrival_Time.text.toString()
                headers["Hand_Luggage_Weight"]= Hand_Luggage_Weight.text.toString()
                return headers
            }
        }
        queue.add(stringRequest)
    }
}
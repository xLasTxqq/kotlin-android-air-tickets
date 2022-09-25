package com.example.flights

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.flights.databinding.ActivityScrollingBinding
import java.sql.*
import android.view.View
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.content.Intent
import android.widget.*
import androidx.core.view.GravityCompat
import org.json.JSONArray
import java.util.*
import kotlin.collections.HashMap
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.DialogInterface




class ScrollingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityScrollingBinding
    private val adapter = RecyclerViewTickets()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(UpdateDB(this).readDB(null,null,null,"id",1).size>0){
            binding.NavMenu.menu.findItem(R.id.profile).isEnabled=true
            binding.NavMenu.menu.findItem(R.id.profileExit).isEnabled=true
            binding.NavMenu.menu.findItem(R.id.aut_page).isEnabled=false
            binding.NavMenu.menu.findItem(R.id.reg_page).isEnabled=false

            if(UpdateDB(this).readDB(arrayOf("Admin"),null,null,"id",1)[0][0]=="1"){
                binding.NavMenu.menu.findItem(R.id.addFlight).isEnabled=true
                binding.NavMenu.menu.findItem(R.id.addFlight).isVisible=true
            }
        }
        else {
            binding.NavMenu.menu.findItem(R.id.profile).isEnabled=false
            binding.NavMenu.menu.findItem(R.id.profileExit).isEnabled=false
            binding.NavMenu.menu.findItem(R.id.aut_page).isEnabled=true
            binding.NavMenu.menu.findItem(R.id.reg_page).isEnabled=true
        }

        binding.NavMenu.menu.findItem(R.id.main_page).isEnabled=false

        binding.NavMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.aut_page -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    startActivity(Intent(this, AutPageActivity::class.java))
                }
                R.id.reg_page -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    startActivity(Intent(this, RegPageActivity::class.java))
                }
                R.id.close -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    this.finishAffinity()
                }
                R.id.profileExit -> {
                    UpdateDB(this).deleteDB(null,null)
                    this.recreate()
                }
                R.id.profile -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.addFlight ->{
                    startActivity(Intent(this, AdminPage::class.java))
                    binding.drawer.closeDrawer(GravityCompat.END)
                }
            }
            true
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_filter_alt_24);
        binding.toolbarLayout.title = "Главная страница"
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val button2:Button = findViewById(R.id.button2)
        button2.setOnClickListener(this)
//        val button: Button = findViewById(R.id.button1)
//        button.setOnClickListener(this)
//        val button3: Button = findViewById(R.id.button3)
//        button3.setOnClickListener(this)
//        val button6: Button = findViewById(R.id.button6)
//        button6.setOnClickListener(this)
        init()
        filter2()
    }
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
    fun BuyTicket(idFlight: String, humans: String, flightClass: String, id_user: String?, appContext: Context, context: Context){
//        println(idFlight)
//        println(id_user)
        if(id_user==null)Toast.makeText(context,"Чтобы купить билет - зарегистрирутесь", Toast.LENGTH_LONG).show()//startActivity(Intent(this, AutPageActivity::class.java))
        else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("Подтверждение")
            builder.setMessage("Вы точно хотите купить билет?")
            builder.setPositiveButton("Подтвердить",
                DialogInterface.OnClickListener { dialog, which ->
                    val queue = Volley.newRequestQueue(appContext)
                    val url = "https://thisistesttoo.000webhostapp.com/buy.php"
                    val stringRequest = object : StringRequest(Request.Method.POST, url,
                        { response ->
//                    println(response)
//                    println(ProfileActivity::class.java)

                            if(response=="true")Toast.makeText(context,"Билет куплен", Toast.LENGTH_LONG).show()//startActivity(Intent(context, ProfileActivity::class.java))
                            else Toast.makeText(context,"Произошла ошибка", Toast.LENGTH_LONG).show()
                        },
                        {
                            Toast.makeText(context, "Подключение к серверу отсутсвует", Toast.LENGTH_LONG)
                                .show()
                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["id_flight"] = idFlight
                            headers["humans"] = humans
                            headers["id_user"] = id_user
                            headers["class_flight"] = flightClass

                            return headers
                        }
                    }
                    queue.add(stringRequest)
                })
            builder.setNegativeButton("Отмена",
                DialogInterface.OnClickListener { dialog, which -> })

            val dialog: AlertDialog = builder.create()
            dialog.show()


        }
    }

    private fun init(){
        val rcView: RecyclerView = findViewById(R.id.rcView)
        binding.apply {
            rcView.layoutManager=LinearLayoutManager(this@ScrollingActivity)
            rcView.adapter=adapter
        }
    }

    override fun onClick(v: View) {
        when(v.id){
//            R.id.button1 -> Task().execute()
//            R.id.button3 -> postRequest()
//            R.id.button6 -> filter()
//            R.id.button6 -> {
//                println(UpdateDB(this).deleteDB(null,null))
//                println(UpdateDB(this).insertDB(
//                    "32".toInt(),
//                    "324",
//                    "234",
//                    "342",
//                    "52",
//                    "324",
//                    "342",
//                    "324",
//                    "0".toInt()
//                ))
//                println(UpdateDB(this).readDB(null,null,null,"id",1))
//            }
            R.id.button2 -> filter2()
//            R.id.button2 -> println("Вторая кнопка")
        }
    }

    private fun filter2() {
//        val da: TextView = findViewById(R.id.textda)
        val humans: EditText = findViewById(R.id.editTextTextPersonName7)
        val fromCountry: EditText = findViewById(R.id.editTextTextPersonName8)
        val fromCity: EditText = findViewById(R.id.editTextTextPersonName9)
        val fromAirport: EditText = findViewById(R.id.editTextTextPersonName10)
        val toCountry: EditText = findViewById(R.id.editTextTextPersonName11)
        val toCity: EditText = findViewById(R.id.editTextTextPersonName12)
        val toAirport: EditText = findViewById(R.id.editTextTextPersonName13)
        val moneyFrom: EditText = findViewById(R.id.editTextNumber2)
        val moneyTo: EditText = findViewById(R.id.editTextNumber3)
        val date: EditText = findViewById(R.id.editTextDate)
        val radioGroup: RadioGroup = findViewById(R.id.radiogrup1)
        val radioButton: RadioButton =  (findViewById<RadioButton>(radioGroup.checkedRadioButtonId))

        val id_user:String? = if((UpdateDB(this).readDB(
                arrayOf("id"),
                null,
                null,
                "id",
                1
            )).isNotEmpty()) UpdateDB(this).readDB(
            arrayOf("id"),
            null,
            null,
            "id",
            1
        )[0][0] else null

        lateinit var flightClass: String
        if(radioButton.text.toString()=="Эконом-класс")flightClass="eco"
        else if(radioButton.text.toString()=="Бизнес-класс")flightClass="bus"

        binding.drawer.closeDrawer(GravityCompat.START)

        val queue = Volley.newRequestQueue(applicationContext)
//        println(this)
        val url = "https://thisistesttoo.000webhostapp.com/filter.php"
        val stringRequest = object: StringRequest(Request.Method.POST, url,
            { response ->
                /*
                //{"da":"net"}
                val resultArray: JSONObject = JSONObject(response)
                val resultArray1: JSONObject = resultArray.getJSONObject("da")
                print(resultArray1) net
                */
                println(response)
                adapter.removeTickets()
                if(response!="null") {
                    val resultArray: JSONArray = JSONArray(response)
                    var mass: MutableList<String> = mutableListOf()
                    val mass2: MutableList<MutableList<String>> = mutableListOf()
                    for (res in 0 until resultArray.length()) {

                        mass.add("${resultArray[res]}")
                        if (mass.size % 19 == 0) {
                            mass2.add(mass)
                            mass = mutableListOf()
                        }

                    }

                    for (i in 0 until mass2.size) {
                        adapter.addTicket(
                            Ticket(
                                this,
                                applicationContext,
                                id_user,
                                flightClass,
                                mass2[i][2],
                                "${mass2[i][13]}, ${mass2[i][15]}",
                                "${mass2[i][8]}, ${mass2[i][12]}",
                                mass2[i][10],
                                mass2[i][6],
                                "${mass2[i][14]}, ${mass2[i][16]}",
                                "${mass2[i][7]}, ${mass2[i][11]}",
                                mass2[i][9],
                                mass2[i][17],
                                if (flightClass == "eco") mass2[i][1]
                                else mass2[i][3],
                                mass2[i][0],
                                if (humans.text.toString() != "") humans.text.toString()
                                else "1",
                            )
                        )
                    }
                }
                else Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_LONG).show()
            },
            { Toast.makeText(this, "Подключение к серверу отсутсвует", Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["humans"] = humans.text.toString()
                headers["from_country"] = fromCountry.text.toString()
                headers["from_city"] = fromCity.text.toString()
                headers["from_airport"] = fromAirport.text.toString()
                headers["to_country"] = toCountry.text.toString()
                headers["to_city"] = toCity.text.toString()
                headers["to_airport"] = toAirport.text.toString()
                headers["money_from"] = moneyFrom.text.toString()
                headers["money_to"] = moneyTo.text.toString()
                headers["date"] = date.text.toString()
                headers["class"] = flightClass
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun filter() {
        val queue = Volley.newRequestQueue(applicationContext)
        println(this)
        val url = "https://thisistesttoo.000webhostapp.com/filter.php"
        val stringRequest = object: StringRequest(Request.Method.POST, url,
            { response ->
                /*
                //{"da":"net"}
                val resultArray: JSONObject = JSONObject(response)
                val resultArray1: JSONObject = resultArray.getJSONObject("da")
                print(resultArray1) net
                */
                val resultArray: JSONArray = JSONArray(response)
                val mass: MutableList<String> = mutableListOf()
                for (res in 0 until resultArray.length()) {
                    println(resultArray[res])

                    mass.add("${resultArray[res]}")
                }
                println(mass)
            },
            { println("Хах, ничего не произошло") })
        {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["humans"] = ""
                headers["from_country"] = ""
                headers["from_city"] = ""
                headers["from_airport"] = ""
                headers["to_country"] = ""
                headers["to_city"] = ""
                headers["to_airport"] = ""
                headers["money_from"] = ""
                headers["money_to"] = ""
                headers["date"] = ""
                headers["class"] = "eco"
                return headers
            }
        }
// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun postRequest() {
        val queue = Volley.newRequestQueue(applicationContext)
        println(this)
        val url = "https://thisistesttoo.000webhostapp.com/index.php"
        val stringRequest = object: StringRequest(Request.Method.POST, url,
            { response ->
                println("Response: %s".format(response.toString()))
            },
            { println("Хах, ничего не произошла") })
        {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["variableget"] = "da"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    class Task : AsyncTask<Void, Void, Void>() {
    var records=""
        override fun doInBackground(vararg p0: Void?): Void? {
            val url: String = "jdbc:mysql://db4free.net:3306/id17701508_test";
            val user: String = "id17701508_mysql";
            val password: String = "?L0Tdq7Sa8r_}6W3";
            val query = "select * from rent"
            val stmt: Statement
            val rs: ResultSet
            println("Начало")
            try {

                val con: Connection = DriverManager.getConnection(url, user, password)
                if (con.metaData.getTables(null, null, "rent", null).next()) {
                stmt = con.createStatement()
                rs = stmt.executeQuery(query)
                }
                else {
                val sql = ("CREATE TABLE rent "
                        + "(id INTEGER not NULL AUTO_INCREMENT, "
                        + " Year INTEGER, "
                        + " Month INTEGER, "
                        + " Size INTEGER, "
                        + " Money INTEGER, "
                        + " PRIMARY KEY ( id ))")
                    stmt = con.createStatement()
                    stmt.executeUpdate(sql)
                    rs = stmt.executeQuery(query)
                }
                while (rs.next()) {
                    records += rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + "\n"

                }
                println(con)
                println(stmt)
                println(rs)
                con.close()
                stmt.close()
                rs.close()
            } catch (e: SQLException) {
                println("Метод catch")
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            println(records)
            super.onPostExecute(result)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

         when (item.itemId) {
            android.R.id.home -> binding.drawer.openDrawer(GravityCompat.START)
            R.id.nav -> binding.drawer.openDrawer(GravityCompat.END)
//            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}
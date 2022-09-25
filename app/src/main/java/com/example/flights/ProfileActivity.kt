package com.example.flights

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.core.view.GravityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.flights.databinding.ActivityProfileBinding
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val adapter = RecyclerViewBoughtTickets()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.NavMenu.menu.findItem(R.id.aut_page).isEnabled=false
        binding.NavMenu.menu.findItem(R.id.reg_page).isEnabled=false
        binding.NavMenu.menu.findItem(R.id.profileExit).isEnabled=true

        if(UpdateDB(this).readDB(arrayOf("Admin"),null,null,"id",1)[0][0]=="1"){
            binding.NavMenu.menu.findItem(R.id.addFlight).isEnabled=true
            binding.NavMenu.menu.findItem(R.id.addFlight).isVisible=true
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Личный кабинет"
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
                R.id.addFlight ->{
                    startActivity(Intent(this, AdminPage::class.java))
                    binding.drawer.closeDrawer(GravityCompat.END)
                }
            }
            true
        }
        GetTickets()
        init()
    }
    override fun onBackPressed() {
//        moveTaskToBack(true)
        startActivity(Intent(this, ScrollingActivity::class.java))
    }
    private fun init() {
        val rcView2: RecyclerView = findViewById(R.id.rcView2)
        binding.apply {
            rcView2.layoutManager = LinearLayoutManager(this@ProfileActivity)
            rcView2.adapter = adapter
        }

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

    private fun GetTickets() {
        val array: MutableList<String> = UpdateDB(this).readDB(arrayOf("id","Login","Password"),null,null,"id",1)[0]
        val id:String=array[0]
        val Login:String=array[1]
        val Password:String=array[2]
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "https://thisistesttoo.000webhostapp.com/remoteprofile.php"
        val stringRequest = object: StringRequest(
            Request.Method.POST, url,
            { response ->
//                println(response)
                if(response.toString()=="null"){
                    binding.textView.isVisible=true
                }
                else {
                    binding.menulay.isVisible=true
                    adapter.removeTickets()
                    val resultArray: JSONArray = JSONArray(response)
                    var mass: MutableList<String> = mutableListOf()
//                    val mass2: MutableList<MutableList<String>> = mutableListOf()
                    for (res in 0 until resultArray.length()) {

                        mass.add("${resultArray[res]}")
                        if (mass.size % 5 == 0) {
//                            mass2.add(mass)
                            adapter.addTicket(Ticket1(mass[0],mass[1],mass[2],mass[3],mass[4]+" р"))
                            mass = mutableListOf()
                        }

                    }
                }
            },
            { Toast.makeText(this, "Подключение в серверу отсутсвует", Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["id_user"] = id
                headers["login_user"] = Login
                headers["password_user"] = Password
                return headers
            }
        }
        queue.add(stringRequest)
    }
}
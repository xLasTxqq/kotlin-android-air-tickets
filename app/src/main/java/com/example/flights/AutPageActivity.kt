package com.example.flights

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.flights.databinding.AutPageBinding
import org.json.JSONObject
import android.content.res.TypedArray




class AutPageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: AutPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AutPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.aut_page)
        val button4: Button = findViewById(R.id.button4)
        button4.setOnClickListener(this)

        binding.NavMenu.menu.findItem(R.id.aut_page).isEnabled=false

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Авторизация"

        val heightToolBar: Int = this.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0f).toInt()
        binding.lay.minimumHeight= windowManager.defaultDisplay.height/2+heightToolBar

        binding.NavMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.main_page -> {
                    startActivity(Intent(this, ScrollingActivity::class.java))
                    binding.drawer.closeDrawer(GravityCompat.END)
                }
                R.id.reg_page -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    startActivity(Intent(this, RegPageActivity::class.java))
                }
                R.id.close -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    this.finishAffinity()
                }
            }
            true
        }

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.button4 -> login()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> finish()
            R.id.nav -> {
                if(binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END)
                else binding.drawer.openDrawer(GravityCompat.END)
            }
        }
        return true
    }
    private fun login() {
        val login: EditText = findViewById(R.id.editTextTextPersonName6);
        val password: EditText = findViewById(R.id.editTextTextPassword3);
        val queue = Volley.newRequestQueue(applicationContext)
        val error: TextView = findViewById(R.id.textView15)
        println(this)
        val url = "https://thisistesttoo.000webhostapp.com/authorization.php"
        val stringRequest = object: StringRequest(
            Request.Method.POST, url,
            { response ->
//                print(response)
                if(response.toString()=="false"){
                    error.text="Не правильный логин или пароль"
                    login.setText("")
                    password.setText("")
                    login.requestFocus()
                }
                else{
//                    println(UpdateDB(this).readDB(null,null,null,"id",1))
                val resultArray: JSONObject = JSONObject(response)
                UpdateDB(this).insertDB(
                    resultArray["id"].toString().toInt(),
                    resultArray["Surname"].toString(),
                    resultArray["Name"].toString(),
                    resultArray["Patronymic"].toString(),
                    resultArray["Login"].toString(),
                    resultArray["Gender"].toString(),
                    resultArray["Number"].toString(),
                    resultArray["Password"].toString(),
                    resultArray["Admin"].toString().toInt()
                    )
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
            },
            { Toast.makeText(this, "Подключение к серверу отсутсвует", Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["login"] = login.text.toString()
                headers["password"] = password.text.toString()
                return headers

            }
        }
        queue.add(stringRequest)
    }
}
package com.example.flights

import android.annotation.SuppressLint
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
import com.example.flights.databinding.RegPageBinding
import org.json.JSONObject

class RegPageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: RegPageBinding
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_page)

        binding = RegPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Регистрация"

        binding.NavMenu.menu.findItem(R.id.reg_page).isEnabled=false

        binding.NavMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.main_page -> {
                    startActivity(Intent(this, ScrollingActivity::class.java))
                    binding.drawer.closeDrawer(GravityCompat.END)
                }
                R.id.aut_page -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    startActivity(Intent(this, AutPageActivity::class.java))
                }
                R.id.close -> {
                    binding.drawer.closeDrawer(GravityCompat.END)
                    this.finishAffinity()
                }
            }
            true
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
////        return super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.menu_auth , menu)
//        return true
//    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.button -> validate()
        }
    }
    private fun validate(){
        val Surname: EditText = findViewById(R.id.editTextTextPersonName)
        val Name: EditText = findViewById(R.id.editTextTextPersonName2)
        val Patronymic: EditText = findViewById(R.id.editTextTextPersonName3)
        val Login: EditText = findViewById(R.id.editTextTextPersonName4)
        val Number: EditText = findViewById(R.id.editTextNumber)
        var valid:Boolean=false

        if(Surname.length()==0) {
            Surname.setError("Поле не может быть пустым")
            Surname.requestFocus()
        }
        else if(Name.length()==0) {
            Name.setError("Поле не может быть пустым")
            Name.requestFocus()
        }
        else if(Patronymic.length()==0) {
            Patronymic.setError("Поле не может быть пустым")
            Patronymic.requestFocus()
        }
        else if(Login.length()==0) {
            Login.setError("Поле не может быть пустым")
            Login.requestFocus()
        }
        else if(Number.length()==0) {
            Number.setError("Поле не может быть пустым")
            Number.requestFocus()
        }
        else valid=true

        val password: EditText = findViewById(R.id.editTextTextPassword);
        val repeatPassword: EditText = findViewById(R.id.editTextTextPassword2);
        if(password.text.toString()!=repeatPassword.text.toString()){
            repeatPassword.setError("Пароли не совпадают")
//            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show()
            repeatPassword.requestFocus()
            repeatPassword.setText("")
        }
        else if(valid) reg()

    }
    private fun reg() {
        val surname: EditText = findViewById(R.id.editTextTextPersonName);
        val name: EditText = findViewById(R.id.editTextTextPersonName2);
        val patronymic: EditText = findViewById(R.id.editTextTextPersonName3);
        val login: EditText = findViewById(R.id.editTextTextPersonName4);
        val radioGroup: RadioGroup = findViewById(R.id.gender);
        val gender: RadioButton =  (findViewById<RadioButton>(radioGroup.checkedRadioButtonId))
        val number: EditText = findViewById(R.id.editTextNumber);
        val password: EditText = findViewById(R.id.editTextTextPassword);
        val repeat_password: EditText = findViewById(R.id.editTextTextPassword2);

        val queue = Volley.newRequestQueue(applicationContext)
        println(this)
        val url = "https://thisistesttoo.000webhostapp.com/registration.php"
        val stringRequest = object: StringRequest(
            Request.Method.POST, url,
            { response ->
//                println(response)
                if(response.toString()=="false") {
                    login.isFocusable
                    login.setText("")
                    login.requestFocus()
                    login.setError("Логин уже используется")
                    Toast.makeText(this, "Такой логин уже используется", Toast.LENGTH_LONG).show()
                }
                else
                {
//                    editTextTextPersonName5

                    println(UpdateDB(this).readDB(null,null,null,"id",1))
                    Toast.makeText(this, "Прошла успешно", Toast.LENGTH_LONG).show()
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
                    println(UpdateDB(this).readDB(null,null,null,"id",1))
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
            },
            { Toast.makeText(this, "Подключение к серверу отсутсвует", Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("surname",surname.text.toString())
                headers.put("name",name.text.toString())
                headers.put("patronymic",patronymic.text.toString())
                headers.put("login",login.text.toString())
                headers.put("gender",gender.text.toString())
                headers.put("number",number.text.toString())
                headers.put("password",password.text.toString())
                headers.put("repeat_password",repeat_password.text.toString())
                return headers
            }
        }
        queue.add(stringRequest)
    }
}
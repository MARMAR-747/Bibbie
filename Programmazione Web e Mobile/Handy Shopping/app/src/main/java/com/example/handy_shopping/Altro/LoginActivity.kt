package com.example.handy_shopping.Altro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.handy_shopping.R
import com.example.handy_shopping.databinding.LoginActivityBinding
import com.example.handy_shopping.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", 0)
        val userName = sharedPreferences.getString("userName", null)
        val userEmail = sharedPreferences.getString("userEmail", null)

        //se l'utente è già loggato lo mando direttamente alla home
        if (userId != 0 && userName != null && userEmail != null) {
            val intent = Intent(this, NavigationActivity::class.java).apply {
                putExtra("userId", userId)
                putExtra("userName", userName)
                putExtra("userEmail", userEmail)
            }
            startActivity(intent)
            finish()
        } else {
            binding = LoginActivityBinding.inflate(layoutInflater)
            val view = binding.root
            enableEdgeToEdge()
            setContentView(view)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            //setonclicklistener
            binding.btnAccedi.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.Password.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    login(email, password)
                } else {
                    Toast.makeText(this, "Inserisci entrambe le credenziali", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnRegistrati.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            binding.visible.setOnClickListener {
                binding.visible.visibility = View.GONE
                binding.hidden.visibility = View.VISIBLE
                binding.Password.inputType = 128
            }

            binding.hidden.setOnClickListener {
                binding.hidden.visibility = View.GONE
                binding.visible.visibility = View.VISIBLE
                binding.Password.inputType = 129
            }
        }
    }

    //retrofit
    private fun login(email: String, password: String) {
        val loginRequest = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }
        Client.retrofit.login(loginRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()
                    val error = loginResponse?.get("error")?.asString
                    if (error == null) {
                        val userId = loginResponse?.get("idutente")?.asInt
                        val userName = loginResponse?.get("nome")?.asString
                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("userId", userId ?: 0)
                        editor.putString("userName", userName)
                        editor.putString("userEmail", email)
                        editor.apply()
                        fetchUserProfile(userId ?: 0)
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Response error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Request failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserProfile(userId: Int) {
        Client.retrofit.getProfile(userId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val profileResponse = response.body()
                    val error = profileResponse?.get("error")?.asString
                    if (error == null) {
                        val userName = profileResponse?.get("nome")?.asString
                        val userEmail = profileResponse?.get("email")?.asString
                        val cardNumber = profileResponse?.get("numero_carta")?.asString
                        val expirationDate = profileResponse?.get("data_scadenza")?.asString
                        val cvv = profileResponse?.get("cvv")?.asString
                        val citta = profileResponse?.get("citta")?.asString
                        val indirizzo = profileResponse?.get("indirizzo")?.asString
                        val numeroCivico = profileResponse?.get("numero_civico")?.asString
                        val isPremium = profileResponse?.get("premium")?.asBoolean ?: false
                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("cardNumber", cardNumber ?: "")
                        editor.putString("expirationDate", expirationDate ?: "")
                        editor.putString("cvv", cvv ?: "")
                        editor.putString("citta", citta ?: "")
                        editor.putString("indirizzo", indirizzo ?: "")
                        editor.putString("numeroCivico", numeroCivico ?: "")
                        editor.putBoolean("isPremium", isPremium)
                        editor.apply()
                        val intent = Intent(this@LoginActivity, NavigationActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userName", userName)
                            putExtra("userEmail", userEmail)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Errore: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Errore nella richiesta: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

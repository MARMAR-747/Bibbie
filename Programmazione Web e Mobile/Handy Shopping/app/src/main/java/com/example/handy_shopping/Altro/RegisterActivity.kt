package com.example.handy_shopping.Altro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.handy_shopping.Home.ProdottoItem
import com.example.handy_shopping.R
import com.example.handy_shopping.databinding.RegistratiActivityBinding
import com.example.handy_shopping.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegistratiActivityBinding
    private lateinit var popupWindow: PopupWindow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistratiActivityBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //setOnClickListeners
        binding.infoPassword.setOnClickListener {
            showPopupWindow(it)
        }

        binding.btnAccedi.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRgstr.setOnClickListener {
            binding.tvEmailError.visibility = View.GONE
            val nome = binding.etNome.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.Password.text.toString()
            val password2 = binding.Password2.text.toString()
            if (nome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {
                if (validPass(password)) {
                    if (validEmail(email)) {
                        if (password == password2) {
                            registerUser(nome, email, password)
                        } else {
                            Toast.makeText(this, "Le due password non corrispono", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "La mail non rispetta la sintassi corretta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showPopupWindow(binding.infoPassword)
                }
            } else {
                Toast.makeText(this, "Compila correttamente tutti i campi", Toast.LENGTH_SHORT).show()
            }
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

        binding.visible2.setOnClickListener {
            binding.visible2.visibility = View.GONE
            binding.hidden2.visibility = View.VISIBLE
            binding.Password2.inputType = 128
        }

        binding.hidden2.setOnClickListener {
            binding.hidden2.visibility = View.GONE
            binding.visible2.visibility = View.VISIBLE
            binding.Password2.inputType = 129
        }
    }

    //funzioni di regex
    private fun validPass(password: String): Boolean {
        var upper = false
        var lower = false
        var number = false
        var length = false
        for (char in password) {
            when {
                char.isUpperCase() -> upper = true
                char.isLowerCase() -> lower = true
                char.isDigit() -> number = true
            }
        }
        if (password.length >= 8) length = true
        return upper && lower && number && length
    }

    private fun validEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        return email.matches(emailRegex.toRegex())
    }

    //retrofit
    private fun registerUser(nome: String, email: String, password: String) {
        val registerRequest = JsonObject().apply {
            addProperty("nome", nome)
            addProperty("email", email)
            addProperty("password", password)
        }
        Client.retrofit.register(registerRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val registerResponse = response.body()
                    val error = registerResponse?.get("error")?.asString
                    if (error == null) {
                        val userId = registerResponse?.get("idutente")?.asInt
                        val userName = registerResponse?.get("nome")?.asString
                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("userId", userId ?: 0)
                        editor.putString("userName", userName)
                        editor.putString("userEmail", email)
                        editor.putString("cardNumber", "")
                        editor.putString("expirationDate", "")
                        editor.putString("cvv", "")
                        editor.putString("citta", "")
                        editor.putString("indirizzo", "")
                        editor.putString("numeroCivico", "")
                        editor.apply()
                        val intent = Intent(this@RegisterActivity, NavigationActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userName", userName)
                            putExtra("userEmail", email)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registrazione fallita: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Response error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Richiesta fallita: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showPopupWindow(anchorView: View) {
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_info, null)
        popupWindow = PopupWindow(popupView, 870, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val background = ContextCompat.getDrawable(this, R.drawable.transparent_background)
        popupWindow.setBackgroundDrawable(background)
        popupWindow.showAsDropDown(anchorView, -840, -anchorView.height - 220)
        popupView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                popupWindow.dismiss()
                true
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                false
            } else {
                false
            }
        }
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(null)
    }
}

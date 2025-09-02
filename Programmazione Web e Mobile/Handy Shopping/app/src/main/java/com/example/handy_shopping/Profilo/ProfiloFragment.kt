package com.example.handy_shopping.Profilo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.handy_shopping.Faq.ContattiActivity
import com.example.handy_shopping.Altro.LoginActivity
import com.example.handy_shopping.R
import com.example.handy_shopping.databinding.FragmentProfiloBinding
import com.example.handy_shopping.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProfiloFragment : Fragment() {

    private lateinit var binding: FragmentProfiloBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var userId: Int = 0
    private var isEmailInUse = false
    lateinit var dialog : Dialog
    lateinit var vecchiaPassword : EditText
    lateinit var nuovaPassword : EditText
    lateinit var confermaNuovaPassword : EditText
    lateinit var pulsanteAnnulla : Button
    lateinit var pulsanteConferma : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfiloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("userId") ?: 0
        val userName = arguments?.getString("userName")
        val userEmail = arguments?.getString("userEmail")

        if (userName != null && userEmail != null) {
            getProfile(userId)
            profileViewModel.updateName(userName)
            binding.tvNomeUtente.text = "Ciao, $userName"
            profileViewModel.updateEmail(userEmail)


        } else {
            getProfile(userId)
        }

        //observe ViewModel
        profileViewModel.name.observe(viewLifecycleOwner) { name ->
            if (binding.etName.text.toString() != name) {
                binding.etName.setText(name)
            }
            binding.tvName.text = name
        }

        profileViewModel.email.observe(viewLifecycleOwner) { email ->
            if (binding.etEmail.text.toString() != email) {
                binding.etEmail.setText(email)
            }
            binding.tvEmail.text = email
        }

        profileViewModel.city.observe(viewLifecycleOwner) { city ->
            if (binding.etCitta.text.toString() != city) {
                binding.etCitta.setText(city)
            }
            binding.tvCitta.text = city
        }

        profileViewModel.address.observe(viewLifecycleOwner) { address ->
            if (binding.etIndirizzo.text.toString() != address) {
                binding.etIndirizzo.setText(address)
            }
            binding.tvIndirizzo.text = address
        }

        profileViewModel.houseNumber.observe(viewLifecycleOwner) { houseNumber ->
            val houseNumberStr = houseNumber ?: ""
            if (binding.etNumeroCivico.text.toString() != houseNumberStr) {
                binding.etNumeroCivico.setText(houseNumberStr)
            }
            binding.tvNumeroCivico.text = houseNumberStr
        }

        profileViewModel.userType.observe(viewLifecycleOwner) { isPremium ->
            val userTypeText = if (isPremium) "Utente premium" else "Utente standard"
            binding.tvPremium.text = userTypeText
        }

        profileViewModel.cardNumber.observe(viewLifecycleOwner) { cardNumber ->
            val formattedCardNumber = cardNumber.chunked(4).joinToString("-")
            if (binding.etCardNumber.text.toString().replace("-", "") != cardNumber) {
                binding.etCardNumber.setText(formattedCardNumber)
            }
            binding.tvCardNumber.text = formattedCardNumber
        }

        profileViewModel.expirationDate.observe(viewLifecycleOwner) { expirationDate ->
            if (binding.etExpirationDate.text.toString() != expirationDate) {
                binding.etExpirationDate.setText(expirationDate)
            }
            binding.tvExpirationDate.text = expirationDate
        }

        profileViewModel.cvv.observe(viewLifecycleOwner) { cvv ->
            if (binding.etCvv.text.toString() != cvv) {
                binding.etCvv.setText(cvv)
            }
            binding.tvCvv.text = cvv
        }

        profileViewModel.premiumExpirationDate.observe(viewLifecycleOwner) { expirationDate ->
            if (!expirationDate.isNullOrEmpty()) {
                binding.tvPremiumDate.text = "Il tuo abbonamento scade il $expirationDate"
            } else {
                binding.tvPremiumDate.text = ""
            }
        }


        //binding dei bottoni textChangedListeners
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                profileViewModel.updateName(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                profileViewModel.updateEmail(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etCardNumber.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val nonDigits = Regex("[^\\d]")

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(nonDigits, "")
                    if (userInput.length <= 16) {
                        current = userInput.chunked(4).joinToString("-")
                        profileViewModel.updateCardNumber(userInput)
                        binding.etCardNumber.setText(current)
                        binding.etCardNumber.setSelection(current.length)
                    } else {
                        binding.etCardNumber.setText(current.substring(0, 19))
                        binding.etCardNumber.setSelection(19)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etExpirationDate.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val nonDigits = Regex("[^\\d]")

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(nonDigits, "")
                    if (userInput.length <= 4) {
                        current = when {
                            userInput.length >= 3 -> "${userInput.substring(0, 2)}/${userInput.substring(2)}"
                            userInput.length >= 2 -> {
                                val month = userInput.substring(0, 2).toIntOrNull()
                                if (month != null && month in 1..12) {
                                    "${userInput.substring(0, 2)}/"
                                } else {
                                    userInput
                                }
                            }
                            else -> userInput
                        }
                        profileViewModel.updateExpirationDate(current)
                        binding.etExpirationDate.setText(current)
                        binding.etExpirationDate.setSelection(current.length)
                    } else {
                        binding.etExpirationDate.setText(current.substring(0, 5))
                        binding.etExpirationDate.setSelection(5)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etCvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > 3) {
                    binding.etCvv.setText(s.toString().substring(0, 3))
                    binding.etCvv.setSelection(3)
                } else {
                    profileViewModel.updateCvv(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        //binding dei bottoni setOnClickListener
        binding.btnLogout.setOnClickListener {
            activity?.let {
                val sharedPreferences = it.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(it, LoginActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }

        binding.btnFaq.setOnClickListener {
            activity?.let {
                val intent = Intent(it, ContattiActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnPremium.setOnClickListener {
            val cardNumber = binding.tvCardNumber.text.toString()
            val expirationDate = binding.tvExpirationDate.text.toString()
            val cvv = binding.tvCvv.text.toString()

            if (cardNumber.isNotEmpty() && expirationDate.isNotEmpty() && cvv.isNotEmpty()) {
                showPremiumDialog(userId)
            } else {
                Toast.makeText(context, "Per favore, compila tutti i campi della carta prima di diventare premium", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnChangePwd.setOnClickListener {
            val activity = requireActivity()
            dialog = Dialog(activity)
            dialog.setContentView(R.layout.change_password_dialog)
            dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            pulsanteAnnulla = dialog.findViewById(R.id.btnAnnulla)
            pulsanteConferma = dialog.findViewById(R.id.btnConferma)
            vecchiaPassword = dialog.findViewById(R.id.vecchiaPassword)
            nuovaPassword = dialog.findViewById(R.id.nuovaPassword)
            confermaNuovaPassword = dialog.findViewById(R.id.nuovaPassword2)

            val visible1 = dialog.findViewById<View>(R.id.visible1)
            val hidden1 = dialog.findViewById<View>(R.id.hidden1)
            val visible2 = dialog.findViewById<View>(R.id.visible2)
            val hidden2 = dialog.findViewById<View>(R.id.hidden2)
            val visible3 = dialog.findViewById<View>(R.id.visible3)
            val hidden3 = dialog.findViewById<View>(R.id.hidden3)

            visible1.setOnClickListener {
                visible1.visibility = View.GONE
                hidden1.visibility = View.VISIBLE
                vecchiaPassword.inputType = 128
            }
            hidden1.setOnClickListener {
                hidden1.visibility = View.GONE
                visible1.visibility = View.VISIBLE
                vecchiaPassword.inputType = 129
            }

            visible2.setOnClickListener {
                visible2.visibility = View.GONE
                hidden2.visibility = View.VISIBLE
                nuovaPassword.inputType = 128
            }
            hidden2.setOnClickListener {
                hidden2.visibility = View.GONE
                visible2.visibility = View.VISIBLE
                nuovaPassword.inputType = 129
            }

            visible3.setOnClickListener {
                visible3.visibility = View.GONE
                hidden3.visibility = View.VISIBLE
                confermaNuovaPassword.inputType = 128
            }
            hidden3.setOnClickListener {
                hidden3.visibility = View.GONE
                visible3.visibility = View.VISIBLE
                confermaNuovaPassword.inputType = 129
            }

            pulsanteConferma.setOnClickListener {
                val currentPassword = vecchiaPassword.text.toString()
                val newPassword = nuovaPassword.text.toString()
                val confirmPassword = confermaNuovaPassword.text.toString()

                if (currentPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                    if(validPass(newPassword)) {
                        if (newPassword == confirmPassword) {
                            if (currentPassword != newPassword){
                                updatePassword(userId, currentPassword, newPassword)
                            } else{
                                Toast.makeText(requireContext(),"La nuova password non può essere uguale a quella attuale",Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(),"Le password non coincidono",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(), "La password non rispetta la sintassi corretta", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                }
            }

            pulsanteAnnulla.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.btnEdit.setOnClickListener {
            binding.tvName.visibility = View.GONE
            binding.tvEmail.visibility = View.GONE
            binding.etName.visibility = View.VISIBLE
            binding.etEmail.visibility = View.VISIBLE
            binding.btnConfirm.visibility = View.VISIBLE
            binding.btnChangePwd.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.GONE
        }

        binding.btnConfirm.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                if (validEmail(email)) {
                    if (email == profileViewModel.email.value) {
                        binding.tvEmailError.visibility = View.GONE
                        updateUserProfile(userId, name, email)
                        binding.tvNomeUtente.text = "Ciao, $name"
                    } else {
                        binding.tvEmailError.visibility = View.GONE
                        checkEmailAndUpdateProfile(userId, name, email)
                    }
                } else {
                    binding.tvEmailError.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(context, "Per favore, compila tutti i campi correttamente", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEditCard.setOnClickListener {
            binding.tvCardNumber.visibility = View.GONE
            binding.tvExpirationDate.visibility = View.GONE
            binding.tvCvv.visibility = View.GONE
            binding.etCardNumber.visibility = View.VISIBLE
            binding.etExpirationDate.visibility = View.VISIBLE
            binding.etCvv.visibility = View.VISIBLE
            binding.btnConfirmCard.visibility = View.VISIBLE
            binding.btnEditCard.visibility = View.GONE
        }

        binding.btnConfirmCard.setOnClickListener {
            val cardNumber = binding.etCardNumber.text.toString().replace("-", "")
            val expirationDate = binding.etExpirationDate.text.toString()
            val cvv = binding.etCvv.text.toString()

            if (cardNumber.length == 16 && expirationDate.length == 5 && cvv.length == 3) {
                val month = expirationDate.substring(0, 2).toIntOrNull()
                if (month != null && month in 1..12) {
                    updateCardProfile(userId, cardNumber, expirationDate, cvv)
                } else {
                    Toast.makeText(context, "Per favore, inserisci un mese valido (01-12)", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Per favore, compila tutti i campi correttamente", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEditFatturazione.setOnClickListener {
            binding.tvCitta.visibility = View.GONE
            binding.tvIndirizzo.visibility = View.GONE
            binding.tvNumeroCivico.visibility = View.GONE
            binding.etCitta.visibility = View.VISIBLE
            binding.etIndirizzo.visibility = View.VISIBLE
            binding.etNumeroCivico.visibility = View.VISIBLE
            binding.btnConfirmFatturazione.visibility = View.VISIBLE
            binding.btnEditFatturazione.visibility = View.GONE
        }

        binding.btnConfirmFatturazione.setOnClickListener {
            val city = binding.etCitta.text.toString().trim()
            val address = binding.etIndirizzo.text.toString().trim()
            val houseNumber = binding.etNumeroCivico.text.toString().trim()

            if (city.isNotEmpty() && address.isNotEmpty() && houseNumber.isNotEmpty()) {
                updateAddressProfile(userId, city, address, houseNumber)
            } else {
                Toast.makeText(context, "Per favore, compila tutti i campi correttamente", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    //funzioni dialog
    private fun showDeleteAccountDialog() {
        val activity = requireActivity()
        dialog = Dialog(activity)
        dialog.setContentView(R.layout.delete_account)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val insertPassword: EditText = dialog.findViewById(R.id.insertPassword)
        val btnAnnulla: Button = dialog.findViewById(R.id.btnAnnulla)
        val btnConferma: Button = dialog.findViewById(R.id.btnConferma)
        val visible1: ImageView = dialog.findViewById(R.id.visible1)
        val hidden1: ImageView = dialog.findViewById(R.id.hidden1)

        visible1.setOnClickListener {
            visible1.visibility = View.GONE
            hidden1.visibility = View.VISIBLE
            insertPassword.inputType = 128

        }

        hidden1.setOnClickListener {
            hidden1.visibility = View.GONE
            visible1.visibility = View.VISIBLE
            insertPassword.inputType = 129
        }

        btnAnnulla.setOnClickListener {
            dialog.dismiss()
        }

        btnConferma.setOnClickListener {
            val password = insertPassword.text.toString()
            if (password.isNotEmpty()) {
                verifyPasswordAndDeleteAccount(userId, password, dialog)

            } else {
                Toast.makeText(requireContext(), "Inserisci la tua password", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showPremiumDialog(userId: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.premium_confirm)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val radioGroup: RadioGroup = dialog.findViewById(R.id.radioGroup)
        val btnAnnulla: Button = dialog.findViewById(R.id.btnAnnulla)
        val btnConferma: Button = dialog.findViewById(R.id.btnConferma)

        btnConferma.isEnabled = false
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            btnConferma.isEnabled = checkedId != -1
        }

        btnAnnulla.setOnClickListener {
            dialog.dismiss()
        }

        btnConferma.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val premiumType = when (selectedRadioButtonId) {
                    R.id.radioButton1 -> "Mensile"
                    R.id.radioButton2 -> "Semestrale"
                    R.id.radioButton3 -> "Annuale"
                    else -> "Mensile"
                }
                val currentPremiumExpiration = profileViewModel.premiumExpirationDate.value
                val newPremiumExpiration = calculatePremiumExpiration(premiumType, currentPremiumExpiration)
                updatePremiumStatus(userId, newPremiumExpiration)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Per favore, seleziona un'opzione premium.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun formatDate(dateStr: String): String {
        val formats = listOf(
            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        )

        for (format in formats) {
            try {
                val date = format.parse(dateStr)
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                return outputFormat.format(date)
            } catch (_: ParseException) {
            }
        }
        return ""
    }


    private fun calculatePremiumExpiration(premiumType: String, currentExpiration: String?): String {
        val calendar = Calendar.getInstance()
        val inputFormats = listOf(
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        )
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (!currentExpiration.isNullOrEmpty()) {
            var parsedDate: Date? = null
            for (format in inputFormats) {
                try {
                    parsedDate = format.parse(currentExpiration)
                    if (parsedDate != null) break
                } catch (_: ParseException) {
                }
            }
            if (parsedDate != null) {
                calendar.time = parsedDate
            } else {
                calendar.time = Date()
            }
        } else {
            calendar.time = Date()
        }

        when (premiumType) {
            "Mensile" -> calendar.add(Calendar.MONTH, 1)
            "Semestrale" -> calendar.add(Calendar.MONTH, 6)
            "Annuale" -> calendar.add(Calendar.YEAR, 1)
        }
        val newDate = calendar.time
        val formattedDate = outputFormat.format(newDate)
        return formattedDate
    }

    //funzioni di regex
    private fun validEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        return email.matches(emailRegex.toRegex())
    }

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

    //funzioni con retrofit
    private fun updateUserProfile(userId: Int, name: String, email: String) {
        val updateRequest = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("nome", name)
            addProperty("email", email)
        }

        Client.retrofit.updateUserProfile(updateRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val updateResponse = response.body()
                    val error = updateResponse?.get("error")?.asString
                    if (error == null) {
                        profileViewModel.updateName(name)
                        profileViewModel.updateEmail(email)
                        binding.tvName.text = name
                        binding.tvEmail.text = email
                        binding.tvName.visibility = View.VISIBLE
                        binding.tvEmail.visibility = View.VISIBLE
                        binding.etName.visibility = View.GONE
                        binding.etEmail.visibility = View.GONE
                        binding.btnConfirm.visibility = View.GONE
                        binding.btnEdit.visibility = View.VISIBLE
                        binding.btnChangePwd.visibility = View.GONE

                        Toast.makeText(context, "Dati del profilo aggiornati con successo", Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 409) {
                        binding.tvEmailError.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(context, "Errore: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore nella richiesta: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCardProfile(userId: Int, cardNumber: String, expirationDate: String, cvv: String) {
        val updateRequest = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("numero_carta", cardNumber)
            addProperty("data_scadenza", expirationDate)
            addProperty("cvv", cvv)
        }
        Client.retrofit.updateCardProfile(updateRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val updateResponse = response.body()
                    val error = updateResponse?.get("error")?.asString
                    if (error == null) {
                        profileViewModel.updateCardNumber(cardNumber)
                        profileViewModel.updateExpirationDate(expirationDate)
                        profileViewModel.updateCvv(cvv)
                        binding.tvCardNumber.visibility = View.VISIBLE
                        binding.tvExpirationDate.visibility = View.VISIBLE
                        binding.tvCvv.visibility = View.VISIBLE
                        binding.etCardNumber.visibility = View.GONE
                        binding.etExpirationDate.visibility = View.GONE
                        binding.etCvv.visibility = View.GONE
                        binding.btnConfirmCard.visibility = View.GONE
                        binding.btnEditCard.visibility = View.VISIBLE
                        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        sharedPreferences?.edit()?.apply {
                            putString("cardNumber", cardNumber)
                            putString("expirationDate", expirationDate)
                            putString("cvv", cvv)
                            apply()
                        }
                        Toast.makeText(context, "Dati della carta aggiornati con successo", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Errore: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore nella richiesta: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateAddressProfile(userId: Int, city: String, address: String, houseNumber: String) {
        val updateRequest = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("citta", city)
            addProperty("indirizzo", address)
            addProperty("numero_civico", houseNumber)
        }
        Client.retrofit.updateAddressProfile(updateRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val updateResponse = response.body()
                    val error = updateResponse?.get("error")?.asString
                    if (error == null) {
                        profileViewModel.updateCity(city)
                        profileViewModel.updateAddress(address)
                        profileViewModel.updateHouseNumber(houseNumber)

                        binding.tvCitta.text = city
                        binding.tvIndirizzo.text = address
                        binding.tvNumeroCivico.text = houseNumber

                        binding.tvCitta.visibility = View.VISIBLE
                        binding.tvIndirizzo.visibility = View.VISIBLE
                        binding.tvNumeroCivico.visibility = View.VISIBLE
                        binding.etCitta.visibility = View.GONE
                        binding.etIndirizzo.visibility = View.GONE
                        binding.etNumeroCivico.visibility = View.GONE
                        binding.btnConfirmFatturazione.visibility = View.GONE
                        binding.btnEditFatturazione.visibility = View.VISIBLE

                        val sharedPreferences = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()
                        editor?.putString("citta", city)
                        editor?.putString("indirizzo", address)
                        editor?.putString("numeroCivico", houseNumber)
                        editor?.apply()

                        Toast.makeText(context, "Indirizzo di fatturazione aggiornato con successo", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Errore: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore nella richiesta: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getProfile(userId: Int) {
        Client.retrofit.getProfile(userId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val profileResponse = response.body()
                    val error = profileResponse?.get("error")?.asString
                    if (error == null) {
                        val name = profileResponse?.get("nome")?.asString
                        val email = profileResponse?.get("email")?.asString
                        val city = profileResponse?.get("citta")?.asString
                        val address = profileResponse?.get("indirizzo")?.asString
                        val houseNumber = profileResponse?.get("numero_civico")?.asString
                        val cardNumber = profileResponse?.get("numero_carta")?.asString
                        val expirationDate = profileResponse?.get("data_scadenza")?.asString
                        val cvv = profileResponse?.get("cvv")?.asString
                        val isPremium = profileResponse?.get("premium")?.asBoolean ?: false
                        val premiumExpirationDateElement = profileResponse?.get("scadenza_premium")


                        profileViewModel.updateName(name ?: "")
                        profileViewModel.updateEmail(email ?: "")
                        profileViewModel.updateCity(city ?: "")
                        profileViewModel.updateAddress(address ?: "")
                        profileViewModel.updateHouseNumber(houseNumber ?: "")
                        profileViewModel.updateCardNumber(cardNumber ?: "")
                        profileViewModel.updateExpirationDate(expirationDate ?: "")
                        profileViewModel.updateCvv(cvv ?: "")
                        profileViewModel.updateUserType(isPremium)

                        if (premiumExpirationDateElement != null && !premiumExpirationDateElement.isJsonNull) {
                            val premiumExpirationDate = premiumExpirationDateElement.asString
                            val formattedDate = formatDate(premiumExpirationDate)
                            profileViewModel.updatePremiumExpirationDate(formattedDate)
                        } else {
                            profileViewModel.updatePremiumExpirationDate("")
                        }

                        binding.tvNomeUtente.text = "Ciao, ${name ?: ""}"
                        binding.tvPremium.text = if (isPremium) "Utente premium" else "Utente standard"
                    } else {
                        Toast.makeText(context, "Errore: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore nella richiesta: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkEmailAndUpdateProfile(userId: Int, name: String, email: String) {
        val checkEmailRequest = JsonObject().apply {
            addProperty("email", email)
        }
        Client.retrofit.checkEmailExists(checkEmailRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val emailExists = response.body()?.get("exists")?.asBoolean ?: false
                    if (emailExists) {
                        binding.tvEmailError.visibility = View.VISIBLE
                    } else {
                        isEmailInUse = false
                        updateUserProfile(userId, name, email)
                    }
                } else {
                    Toast.makeText(context, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore nella richiesta: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatePassword(userId: Int, currentPassword: String, newPassword: String) {
        val passwordRequest = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("current_password", currentPassword)
            addProperty("new_password", newPassword)
        }
        Client.retrofit.updatePassword(passwordRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.get("success")?.asBoolean == true) {
                        Toast.makeText(requireContext(), "Password modificata con successo", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Password corrente non corretta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Password corrente non corretta", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(requireContext(), "Errore di rete", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verifyPasswordAndDeleteAccount(userId: Int, password: String, dialog: Dialog) {
        val deleteRequest = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("password", password)
        }

        Client.retrofit.verifyPasswordAndDeleteAccount(deleteRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val deleteResponse = response.body()
                    val success = deleteResponse?.get("success")?.asBoolean ?: false
                    if (success) {
                        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        sharedPreferences?.edit()?.clear()?.apply()
                        dialog.dismiss()
                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        val errorMessage = deleteResponse?.get("error")?.asString ?: "Password errata, riprova."
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatePremiumStatus(userId: Int, scadenzaPremium: String) {
        val updateRequest = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("scadenza_premium", scadenzaPremium)
        }
        Client.retrofit.updatePremium(updateRequest).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val updateResponse = response.body()
                    val error = updateResponse?.get("error")?.asString
                    if (error == null) {
                        profileViewModel.updateUserType(true)
                        val formattedDate = formatDate(scadenzaPremium)
                        profileViewModel.updatePremiumExpirationDate(formattedDate)
                        binding.tvPremium.text = "Utente premium"
                        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()
                        editor?.putBoolean("isPremium", true)
                        editor?.apply()
                        Toast.makeText(context, "Upgrade a utente premium avvenuto con successo", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Errore: $error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Errore nella risposta: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore nella richiesta: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
package com.example.handy_shopping.Altro

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.handy_shopping.Acquisti.AcquistiFragment
import com.example.handy_shopping.Carrello.CarrelloFragment
import com.example.handy_shopping.Home.HomeFragment
import com.example.handy_shopping.Home.RicercaFragment
import com.example.handy_shopping.Profilo.ProfileViewModel
import com.example.handy_shopping.Profilo.ProfiloFragment
import com.example.handy_shopping.R
import com.example.handy_shopping.databinding.NavigationActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class NavigationActivity : AppCompatActivity(), HomeFragment.OnFragmentInteractionListener {
    private lateinit var binding: NavigationActivityBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = NavigationActivityBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", 0)
        val userName = sharedPreferences.getString("userName", "")
        val userEmail = sharedPreferences.getString("userEmail", "")
        val cardNumber = sharedPreferences.getString("cardNumber", "")
        val expirationDate = sharedPreferences.getString("expirationDate", "")
        val cvv = sharedPreferences.getString("cvv", "")
        val isPremium = sharedPreferences.getBoolean("isPremium", false)

        profileViewModel.updateName(userName ?: "")
        profileViewModel.updateEmail(userEmail ?: "")
        profileViewModel.updateCardNumber(cardNumber ?: "")
        profileViewModel.updateExpirationDate(expirationDate ?: "")
        profileViewModel.updateCvv(cvv ?: "")
        profileViewModel.updateUserType(isPremium)

        if (intent.getBooleanExtra("navigate_to_cart", false)) {
            loadFragment(CarrelloFragment())
        } else {
            val homeFragment = HomeFragment().apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, homeFragment)
                .commit()
        }

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            var fragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    fragment = HomeFragment()
                }
                R.id.nav_acquisti -> {
                    fragment = AcquistiFragment()
                }
                R.id.nav_profilo -> {
                    fragment = ProfiloFragment().apply {
                        arguments = Bundle().apply {
                            putInt("userId", userId)
                            putString("userName", userName)
                            putString("userEmail", userEmail)
                        }
                    }
                }
                R.id.nav_carrello -> {
                    fragment = CarrelloFragment()
                }
            }
            if (fragment != null) {
                loadFragment(fragment)
            }
            true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    scheduleDailyNotification()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            scheduleDailyNotification()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                scheduleDailyNotification()
            }
        }

    override fun onSwitchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, RicercaFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun scheduleDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("notification_message", "Hey, accedi all'app per scoprire le offerte di oggi!")
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}

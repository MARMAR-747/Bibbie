// ProfileViewModel.kt

package com.example.handy_shopping.Profilo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _city = MutableLiveData<String>()
    val city: LiveData<String> get() = _city

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    private val _houseNumber = MutableLiveData<String>()
    val houseNumber: LiveData<String> get() = _houseNumber

    private val _cardNumber = MutableLiveData<String>()
    val cardNumber: LiveData<String> get() = _cardNumber

    private val _expirationDate = MutableLiveData<String>()
    val expirationDate: LiveData<String> get() = _expirationDate

    private val _cvv = MutableLiveData<String>()
    val cvv: LiveData<String> get() = _cvv

    private val _userType = MutableLiveData<Boolean>()
    val userType: LiveData<Boolean> get() = _userType

    private val _premiumExpirationDate = MutableLiveData<String>()
    val premiumExpirationDate: LiveData<String> get() = _premiumExpirationDate

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updateCity(city: String) {
        _city.value = city
    }

    fun updateAddress(address: String) {
        _address.value = address
    }

    fun updateHouseNumber(houseNumber: String) {
        _houseNumber.value = houseNumber
    }

    fun updateCardNumber(cardNumber: String) {
        _cardNumber.value = cardNumber
    }

    fun updateExpirationDate(expirationDate: String) {
        _expirationDate.value = expirationDate
    }

    fun updateCvv(cvv: String) {
        _cvv.value = cvv
    }

    fun updateUserType(isPremium: Boolean) {
        _userType.value = isPremium
    }

    fun updatePremiumExpirationDate(date: String) {
        _premiumExpirationDate.value = date
    }

}

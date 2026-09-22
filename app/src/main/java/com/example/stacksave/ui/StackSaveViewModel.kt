package com.example.stacksave.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stacksave.model.StoreDeal
import com.example.stacksave.model.UserProfile
import com.example.stacksave.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StackSaveViewModel(app: Application) : AndroidViewModel(app) {
    private val auth = AuthRepository(app)
    private val users = UserRepository(app)
    private val api = DealFeedService()
    private val offline = OfflineRepository(app)

    private val _deals = MutableStateFlow(offline.loadDeals().ifEmpty { api.demoDeals() })
    val deals = _deals.asStateFlow()

    private val _savedDeals = MutableStateFlow<List<StoreDeal>>(emptyList())
    val savedDeals = _savedDeals.asStateFlow()

    private val _selectedDealForDetail = MutableStateFlow<StoreDeal?>(null)
    val selectedDealForDetail = _selectedDealForDetail.asStateFlow()

    private val _selectedLocation = MutableStateFlow("Sandton, Johannesburg")
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _lastUpdated = MutableStateFlow(currentTimestamp())
    val lastUpdated = _lastUpdated.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _quickFilter = MutableStateFlow("All") // "All", "Top Rated", "Fastest", "Free Delivery", "Super Cashback"
    val quickFilter = _quickFilter.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _loggedIn = MutableStateFlow(auth.currentUid() != null)
    val loggedIn = _loggedIn.asStateFlow()

    private val _profile = MutableStateFlow(UserProfile())
    val profile = _profile.asStateFlow()

    init {
        refreshDeals()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setQuickFilter(filter: String) {
        _quickFilter.value = filter
    }

    fun setSelectedLocation(location: String) {
        _selectedLocation.value = location
    }

    fun openDealDetail(deal: StoreDeal?) {
        _selectedDealForDetail.value = deal
    }

    fun toggleSavedDeal(deal: StoreDeal) {
        val current = _savedDeals.value.toMutableList()
        if (current.any { it.id == deal.id }) {
            current.removeAll { it.id == deal.id }
        } else {
            current.add(deal)
        }
        _savedDeals.value = current
    }

    fun isDealSaved(dealId: String): Boolean {
        return _savedDeals.value.any { it.id == dealId }
    }

    fun refreshDeals() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val r = api.getDeals()
            val list = r.getOrElse { api.demoDeals() }
            _deals.value = list
            offline.saveDeals(list)
            _lastUpdated.value = currentTimestamp()
            _isRefreshing.value = false
        }
    }

    fun login(email: String, password: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            auth.login(email, password).onSuccess {
                _loggedIn.value = true
                _profile.value = UserProfile(it, email = email)
                _message.value = if (auth.isFirebaseConfigured()) "Logged in with Firebase" else "Logged in using local demo mode"
                onDone()
            }.onFailure {
                _message.value = it.message ?: "Login failed"
            }
        }
    }

    fun register(email: String, password: String, name: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            auth.register(email, password, name).onSuccess { uid ->
                _loggedIn.value = true
                _profile.value = UserProfile(uid, name, email)
                users.saveProfile(_profile.value)
                _message.value = if (auth.isFirebaseConfigured()) "Account created with Firebase" else "Account created in local demo mode"
                onDone()
            }.onFailure {
                _message.value = it.message ?: "Registration failed"
            }
        }
    }

    fun updateSettings(language: String, notifications: Boolean, offlineSync: Boolean) {
        _profile.value = _profile.value.copy(
            language = language,
            notificationsEnabled = notifications,
            offlineSyncEnabled = offlineSync
        )
        offline.setOfflineEnabled(offlineSync)
        viewModelScope.launch {
            users.saveProfile(_profile.value)
        }
    }

    fun appContext() = getApplication<Application>()

    fun signOut() {
        auth.signOut()
        _loggedIn.value = false
    }

    private fun currentTimestamp(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }
}

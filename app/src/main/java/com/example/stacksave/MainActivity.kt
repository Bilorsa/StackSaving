package com.example.stacksave

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stacksave.data.LocalNotification
import com.example.stacksave.model.StoreDeal
import com.example.stacksave.ui.*

class MainActivity : ComponentActivity() {
    private val vm: StackSaveViewModel by viewModels()
    private val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        if (Build.VERSION.SDK_INT >= 33) {
            permission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            StackSaveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StackSaveApp(vm)
                }
            }
        }
    }
}

@Composable
fun StackSaveApp(vm: StackSaveViewModel) {
    val logged by vm.loggedIn.collectAsStateWithLifecycle()
    if (!logged) {
        LoginScreen(vm)
    } else {
        MainShell(vm)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: StackSaveViewModel) {
    var isRegister by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val msg by vm.message.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = "StackSave Logo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "StackSave Eats & Deals",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Smart Deal Aggregator & Cashback",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!isRegister) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .clickable { isRegister = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Log In",
                            fontWeight = if (!isRegister) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isRegister) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isRegister) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .clickable { isRegister = true }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Register",
                            fontWeight = if (isRegister) FontWeight.Bold else FontWeight.Normal,
                            color = if (isRegister) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isRegister) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                if (isRegister) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = confirm,
                        onValueChange = { confirm = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Confirm Password") },
                        leadingIcon = { Icon(Icons.Outlined.LockReset, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if ((!isRegister || pass == confirm) && validPassword(pass)) {
                            if (isRegister) {
                                vm.register(email.trim(), pass, name.trim()) {}
                            } else {
                                vm.login(email.trim(), pass) {}
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isRegister) "Create Account" else "Sign In",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        vm.login("sso.demo@stacksave.co.za", "DemoPass123!") {}
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Demo SSO Login")
                }

                AnimatedVisibility(visible = msg.isNotBlank()) {
                    Column {
                        Spacer(modifier = Modifier.height(14.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Text(
                                text = msg,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Password: 8+ chars, upper, lower, number & symbol.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun validPassword(p: String) =
    p.length >= 8 && p.any(Char::isUpperCase) && p.any(Char::isLowerCase) && p.any(Char::isDigit) && p.any { !it.isLetterOrDigit() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainShell(vm: StackSaveViewModel) {
    var tab by remember { mutableStateOf(0) }
    val savedDeals by vm.savedDeals.collectAsStateWithLifecycle()
    val selectedDealForDetail by vm.selectedDealForDetail.collectAsStateWithLifecycle()
    var showSavedBasketSheet by remember { mutableStateOf(false) }

    val totalSavedAmount = remember(savedDeals) {
        savedDeals.sumOf { it.saving }
    }

    Scaffold(
        bottomBar = {
            Column {
                // Floating Saved Basket Bar
                AnimatedVisibility(visible = savedDeals.isNotEmpty()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { showSavedBasketSheet = true },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        tonalElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${savedDeals.size}",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Saved Basket",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                    Text(
                                        text = "Total Savings: R %.2f".format(totalSavedAmount),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.9f)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "View Basket",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = tab == 0,
                        onClick = { tab = 0 },
                        icon = {
                            Icon(
                                imageVector = if (tab == 0) Icons.Default.RestaurantMenu else Icons.Outlined.RestaurantMenu,
                                contentDescription = "Eats & Deals"
                            )
                        },
                        label = { Text("Eats & Deals") }
                    )

                    NavigationBarItem(
                        selected = tab == 1,
                        onClick = { tab = 1 },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (savedDeals.isNotEmpty()) {
                                        Badge { Text("${savedDeals.size}") }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (tab == 1) Icons.Default.ShoppingBag else Icons.Outlined.ShoppingBag,
                                    contentDescription = "Saved Basket"
                                )
                            }
                        },
                        label = { Text("Basket") }
                    )

                    NavigationBarItem(
                        selected = tab == 2,
                        onClick = { tab = 2 },
                        icon = {
                            Icon(
                                imageVector = if (tab == 2) Icons.Default.Analytics else Icons.Outlined.Analytics,
                                contentDescription = "Analytics"
                            )
                        },
                        label = { Text("Analytics") }
                    )

                    NavigationBarItem(
                        selected = tab == 3,
                        onClick = { tab = 3 },
                        icon = {
                            Icon(
                                imageVector = if (tab == 3) Icons.Default.AccountCircle else Icons.Outlined.AccountCircle,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AnimatedContent(
                targetState = tab,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut()
                        )
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut()
                        )
                    }.using(SizeTransform(clip = false))
                },
                label = "ScreenTransition"
            ) { targetTab ->
                when (targetTab) {
                    0 -> HomeScreen(vm)
                    1 -> SavedBasketScreen(vm)
                    2 -> AnalyticsScreen(vm)
                    else -> ProfileScreen(vm)
                }
            }
        }
    }

    if (selectedDealForDetail != null) {
        DealDetailModal(
            deal = selectedDealForDetail!!,
            onDismiss = { vm.openDealDetail(null) },
            vm = vm
        )
    }

    if (showSavedBasketSheet) {
        SavedBasketBottomSheet(
            vm = vm,
            onDismiss = { showSavedBasketSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: StackSaveViewModel) {
    val deals by vm.deals.collectAsStateWithLifecycle()
    val isRefreshing by vm.isRefreshing.collectAsStateWithLifecycle()
    val lastUpdated by vm.lastUpdated.collectAsStateWithLifecycle()
    val searchQuery by vm.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by vm.selectedCategory.collectAsStateWithLifecycle()
    val quickFilter by vm.quickFilter.collectAsStateWithLifecycle()
    val selectedLocation by vm.selectedLocation.collectAsStateWithLifecycle()

    var showLocationPicker by remember { mutableStateOf(false) }

    val categories = remember(deals) {
        listOf("All") + deals.map { it.category }.distinct().sorted()
    }

    val quickFilters = listOf("All", "Top Rated", "Fastest", "Free Delivery", "Super Cashback")

    val filteredDeals = remember(deals, searchQuery, selectedCategory, quickFilter) {
        deals.filter { deal ->
            val matchesCategory = selectedCategory == "All" || deal.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    deal.productName.contains(searchQuery, ignoreCase = true) ||
                    deal.storeName.contains(searchQuery, ignoreCase = true) ||
                    deal.category.contains(searchQuery, ignoreCase = true)
            val matchesQuickFilter = when (quickFilter) {
                "Top Rated" -> deal.rating >= 4.7
                "Fastest" -> deal.estimatedTime.contains("15") || deal.estimatedTime.contains("20")
                "Free Delivery" -> deal.deliveryFee.contains("Free", ignoreCase = true)
                "Super Cashback" -> deal.cashbackRate >= 4.0
                else -> true
            }
            matchesCategory && matchesSearch && matchesQuickFilter
        }
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        animationSpec = if (isRefreshing) {
            infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        } else {
            tween(durationMillis = 300)
        },
        label = "refreshRotation"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .clickable { showLocationPicker = true }
                        .padding(vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Deliver / Shop near",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = selectedLocation,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondary)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { vm.refreshDeals() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Feed",
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { vm.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search stores, groceries, electronics...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { vm.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }

        item {
            Column {
                Text(
                    text = "Featured Deals & Promotions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        PromoBannerCard(
                            title = "Checkers Sixty60",
                            subtitle = "Up to 35% Off Fresh Grocery Baskets",
                            badgeText = "0% DELIVERY FEE",
                            gradient = listOf(Color(0xFF0D9488), Color(0xFF14B8A6)),
                            icon = Icons.Default.LocalGroceryStore,
                            onClick = { vm.setSelectedCategory("Groceries") }
                        )
                    }
                    item {
                        PromoBannerCard(
                            title = "Woolworths Dash",
                            subtitle = "5.0% Instant Cashback Specials",
                            badgeText = "SUPER CASHBACK",
                            gradient = listOf(Color(0xFF4F46E5), Color(0xFF6366F1)),
                            icon = Icons.Default.WorkspacePremium,
                            onClick = { vm.setSelectedCategory("Groceries") }
                        )
                    }
                    item {
                        PromoBannerCard(
                            title = "Game Tech Express",
                            subtitle = "Flash Sale on Audio & Headphones",
                            badgeText = "LIMITED TIME",
                            gradient = listOf(Color(0xFFD97706), Color(0xFFF59E0B)),
                            icon = Icons.Default.Headphones,
                            onClick = { vm.setSelectedCategory("Electronics") }
                        )
                    }
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = cat.equals(selectedCategory, ignoreCase = true)
                        CategoryPill(
                            category = cat,
                            isSelected = isSelected,
                            onClick = { vm.setSelectedCategory(cat) }
                        )
                    }
                }
            }
        }

        // Quick Filter Chips (Top Rated, Fastest, Free Delivery, Super Cashback)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = quickFilters) { filter ->
                    val isSelected = filter == quickFilter
                    FilterChip(
                        selected = isSelected,
                        onClick = { vm.setQuickFilter(filter) },
                        label = { Text(filter) },
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Deal Feed Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Top Stores Near You (${filteredDeals.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Updated: $lastUpdated",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Deal Feed Cards
        if (filteredDeals.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No deals match your filters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Try switching category or resetting search.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredDeals, key = { it.id }) { deal ->
                DealCard(
                    deal = deal,
                    isSaved = vm.isDealSaved(deal.id),
                    onSaveToggle = { vm.toggleSavedDeal(deal) },
                    onCardClick = { vm.openDealDetail(deal) }
                )
            }
        }
    }

    // Location Picker Dialog
    if (showLocationPicker) {
        AlertDialog(
            onDismissRequest = { showLocationPicker = false },
            title = { Text("Select Delivery / Shop Area") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Sandton, Johannesburg", "Rosebank, Johannesburg", "Centurion, Pretoria", "Umhlanga, Durban", "Waterfront, Cape Town").forEach { loc ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.setSelectedLocation(loc)
                                    showLocationPicker = false
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = if (loc == selectedLocation) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = loc,
                                modifier = Modifier.padding(14.dp),
                                fontWeight = if (loc == selectedLocation) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationPicker = false }) { Text("Close") }
            }
        )
    }
}

@Composable
fun PromoBannerCard(
    title: String,
    subtitle: String,
    badgeText: String,
    gradient: List<Color>,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradient))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.25f),
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun CategoryPill(category: String, isSelected: Boolean, onClick: () -> Unit) {
    val icon = when {
        category.contains("Grocery", ignoreCase = true) -> Icons.Default.ShoppingCart
        category.contains("Tech", ignoreCase = true) || category.contains("Electr", ignoreCase = true) -> Icons.Default.Devices
        category.contains("Fit", ignoreCase = true) -> Icons.Default.FitnessCenter
        category.contains("Beauty", ignoreCase = true) -> Icons.Default.Face
        else -> Icons.Default.Storefront
    }

    val animatedBgColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "categoryPillBg"
    )

    val animatedContentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "categoryPillContent"
    )

    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = animatedBgColor,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = animatedContentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = animatedContentColor
            )
        }
    }
}

@Composable
fun DealCard(
    deal: StoreDeal,
    isSaved: Boolean,
    onSaveToggle: () -> Unit,
    onCardClick: () -> Unit
) {
    val saveScale by animateFloatAsState(
        targetValue = if (isSaved) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "bookmarkScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Graphical Top Thumbnail Banner Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
            ) {
                // Background Store Graphic Text / Icon
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f),
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.Center)
                )

                // Top Left: Featured Tag
                if (deal.isFeatured) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "FEATURED DEAL",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // Top Right: Save Bookmark Icon
                IconButton(
                    onClick = onSaveToggle,
                    modifier = Modifier
                        .padding(8.dp)
                        .scale(saveScale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save Deal",
                        tint = if (isSaved) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }

                // Bottom Left: Delivery Time & Rating Overlay
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.75f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "%.1f (%d)".format(deal.rating, deal.ratingCount),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.75f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = deal.estimatedTime,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Body Content
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = deal.storeName,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = deal.deliveryFee,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = deal.productName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Price & Savings Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "R %.2f".format(deal.salePrice),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "R %.2f".format(deal.originalPrice),
                            style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Save R %.2f".format(deal.saving),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "%.1f%% Cashback".format(deal.cashbackRate),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SavedBasketScreen(vm: StackSaveViewModel) {
    val savedDeals by vm.savedDeals.collectAsStateWithLifecycle()

    val totalSavings = remember(savedDeals) { savedDeals.sumOf { it.saving } }
    val totalCost = remember(savedDeals) { savedDeals.sumOf { it.salePrice } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Saved Deals Basket",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Review saved deals and claim cashback at store checkout",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (savedDeals.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Your Saved Basket is empty",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap the bookmark icon on any deal to save it for quick comparison.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Saved Basket Value",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "R %.2f".format(totalCost),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Total Instant Savings",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "R %.2f".format(totalSavings),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            items(savedDeals, key = { it.id }) { deal ->
                DealCard(
                    deal = deal,
                    isSaved = true,
                    onSaveToggle = { vm.toggleSavedDeal(deal) },
                    onCardClick = { vm.openDealDetail(deal) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealDetailModal(
    deal: StoreDeal,
    onDismiss: () -> Unit,
    vm: StackSaveViewModel
) {
    val context = vm.appContext()
    val isSaved = vm.isDealSaved(deal.id)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    vm.toggleSavedDeal(deal)
                    Toast.makeText(
                        context,
                        if (!isSaved) "Deal added to Saved Basket!" else "Removed from Saved Basket",
                        Toast.LENGTH_SHORT
                    ).show()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (!isSaved) "Add to Saved Basket (Save R %.2f)".format(deal.saving) else "Remove from Saved Basket",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = deal.storeName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = deal.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = deal.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Rating & Reviews:")
                    Text("%.1f ★ (%d reviews)".format(deal.rating, deal.ratingCount), fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Estimated Delivery:")
                    Text(deal.estimatedTime, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Delivery Fee:")
                    Text(deal.deliveryFee, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Cashback Rate:")
                    Text("%.1f%% Instant Cashback".format(deal.cashbackRate), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedBasketBottomSheet(
    vm: StackSaveViewModel,
    onDismiss: () -> Unit
) {
    val savedDeals by vm.savedDeals.collectAsStateWithLifecycle()
    val totalSavings = remember(savedDeals) { savedDeals.sumOf { it.saving } }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Saved Deals Basket (${savedDeals.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Total Combined Savings: R %.2f".format(totalSavings),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = savedDeals) { d ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(d.productName, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text("${d.storeName} • Save R %.2f".format(d.saving), style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { vm.toggleSavedDeal(d) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }
                }
            }

            Button(
                onClick = {
                    Toast.makeText(vm.appContext(), "Cashback claim submitted for saved deals!", Toast.LENGTH_LONG).show()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Claim All Cashback", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AnalyticsScreen(vm: StackSaveViewModel) {
    val deals by vm.deals.collectAsStateWithLifecycle()

    val totalDeals = deals.size
    val avgPrice = if (deals.isEmpty()) 0.0 else deals.map { it.salePrice }.average()
    val storeDistribution = remember(deals) {
        deals.groupBy { it.storeName }.mapValues { it.value.size }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Deal Analytics & Insights",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Real-time store comparison and savings breakdown",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnalyticsStatCard(
                    title = "Active Deals",
                    value = "$totalDeals",
                    icon = Icons.Default.LocalOffer,
                    modifier = Modifier.weight(1f)
                )
                AnalyticsStatCard(
                    title = "Avg Deal Price",
                    value = "R %.2f".format(avgPrice),
                    icon = Icons.Default.Payments,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Store Deal Distribution",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    storeDistribution.forEach { (store, count) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = store, fontWeight = FontWeight.Medium)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 12.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$count deals",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsStatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileScreen(vm: StackSaveViewModel) {
    val profile by vm.profile.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (profile.displayName.ifBlank { profile.email.ifBlank { "User" } }).take(1).uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = profile.displayName.ifBlank { "StackSave User" },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = profile.email.ifBlank { "demo.account@stacksave.co.za" },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Status", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = if (profile.uid == "local-demo-user") "Demo Mode" else "Firebase",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Language", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = if (profile.language == "zu") "isiZulu" else "English",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { vm.signOut() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out", fontWeight = FontWeight.Bold)
        }
    }
}

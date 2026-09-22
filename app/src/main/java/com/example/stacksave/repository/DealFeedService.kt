package com.example.stacksave.repository

import com.example.stacksave.model.StoreDeal
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DealApi {
    @GET("products?limit=24")
    suspend fun getProducts(): ProductResponse
}

data class ProductResponse(val products: List<ApiProduct> = emptyList())
data class ApiProduct(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val discountPercentage: Double = 15.0,
    val rating: Double = 4.5,
    val category: String = "General",
    val thumbnail: String = ""
)

class DealFeedService {
    private val api = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DealApi::class.java)

    suspend fun getDeals(): Result<List<StoreDeal>> = runCatching {
        val stores = listOf("Checkers Sixty60", "Woolworths Dash", "Game Express", "Takealot NOW", "Clicks Express", "Pick n Pay ASAP")
        val times = listOf("15-25 min", "20-30 min", "25-35 min", "30-45 min")
        val fees = listOf("Free Delivery", "Free Delivery", "R15.00 Delivery", "R20.00 Delivery")

        api.getProducts().products.mapIndexed { index, p ->
            val store = stores[index % stores.size]
            val original = p.price * (1.15 + (index % 3) * 0.1)
            val sale = p.price
            val cashback = when {
                store.contains("Checkers") -> 4.5
                store.contains("Woolworths") -> 5.0
                store.contains("Takealot") -> 3.0
                store.contains("Game") -> 6.0
                store.contains("Clicks") -> 3.5
                else -> 2.5
            }
            val formattedCategory = p.category
                .replace("-", " ")
                .split(" ")
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

            StoreDeal(
                id = p.id.toString(),
                storeName = store,
                productName = p.title,
                originalPrice = original,
                salePrice = sale,
                cashbackRate = cashback,
                category = formattedCategory,
                imageUrl = p.thumbnail,
                rating = if (p.rating > 0) p.rating else 4.7,
                ratingCount = 350 + (index * 85) % 1200,
                estimatedTime = times[index % times.size],
                deliveryFee = fees[index % fees.size],
                isFeatured = index % 3 == 0,
                description = p.description.ifBlank { "Exclusive instant deals with price match guarantee & fast doorstep delivery." }
            )
        }
    }

    fun demoDeals() = listOf(
        StoreDeal("1", "Checkers Sixty60", "Weekly Fresh Grocery Basket", 420.0, 329.99, 4.5, "Groceries", rating = 4.9, ratingCount = 1420, estimatedTime = "15-25 min", deliveryFee = "Free Delivery", isFeatured = true, description = "Farm fresh essentials, pantry staples and organic produce bundled for maximum savings."),
        StoreDeal("2", "Woolworths Dash", "Everyday Artisanal Essentials", 280.0, 199.99, 5.0, "Groceries", rating = 4.8, ratingCount = 980, estimatedTime = "20-30 min", deliveryFee = "Free Delivery", isFeatured = true, description = "Premium quality dairy, bakery, and prepared meals delivered fresh to your door."),
        StoreDeal("3", "Game Express", "Bluetooth Noise-Cancelling Speaker", 899.0, 599.00, 6.0, "Electronics", rating = 4.7, ratingCount = 650, estimatedTime = "25-35 min", deliveryFee = "R15.00 Delivery", isFeatured = false, description = "High performance wireless audio with deep bass and 20-hour battery life."),
        StoreDeal("4", "Takealot NOW", "Fitness Tracker & Smart Band", 750.0, 499.00, 3.0, "Fitness", rating = 4.6, ratingCount = 1120, estimatedTime = "30-40 min", deliveryFee = "Free Delivery", isFeatured = true, description = "Track heart rate, steps, sleep, and sports modes with AMOLED display."),
        StoreDeal("5", "Clicks Express", "Skincare & Health Glow Kit", 350.0, 249.50, 3.5, "Beauty", rating = 4.9, ratingCount = 830, estimatedTime = "20-30 min", deliveryFee = "Free Delivery", isFeatured = false, description = "Dermatologist recommended skincare routine set for daily protection."),
        StoreDeal("6", "Pick n Pay ASAP", "Pantry Saver & Drinks Pack", 190.0, 125.00, 2.5, "Groceries", rating = 4.5, ratingCount = 490, estimatedTime = "15-25 min", deliveryFee = "R10.00 Delivery", isFeatured = false, description = "Stock up on essential beverages, snacks, and baking ingredients.")
    )
}

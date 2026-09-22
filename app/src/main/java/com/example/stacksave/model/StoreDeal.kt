package com.example.stacksave.model

data class StoreDeal(
    val id: String = "",
    val storeName: String = "",
    val productName: String = "",
    val originalPrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val cashbackRate: Double = 0.0,
    val category: String = "General",
    val imageUrl: String = "",
    val rating: Double = 4.8,
    val ratingCount: Int = 850,
    val estimatedTime: String = "20-35 min",
    val deliveryFee: String = "Free Delivery",
    val isFeatured: Boolean = false,
    val description: String = "Special aggregated deal with instant cashback and price guarantee."
) {
    val saving: Double get() = (originalPrice - salePrice).coerceAtLeast(0.0)
    val discountPercent: Int get() = if (originalPrice > 0) (((originalPrice - salePrice) / originalPrice) * 100).toInt() else 0
}

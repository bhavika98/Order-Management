package com.app.incroyable.order_management.model

data class OrderStats(
    val trashed: Int,
    val delivered: Int,
    val totalSales: Double,
    val totalWaste: Double,
    val totalRevenue: Double
)

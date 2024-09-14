package com.app.incroyable.order_management.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatCentsToCurrency(cents: Int): String {
    val dollars = cents / 100.0
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(dollars)
}

fun convertTimestampToDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a  •  dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun capitalizeFirstLetter(text: String?): String {
    if (text.isNullOrEmpty()) {
        return text ?: ""
    }
    val lowerCaseText = text.lowercase()
    return lowerCaseText.replaceFirstChar { it.uppercase() }
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities =
        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
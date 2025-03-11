package com.task.feature_gifs.domain.model.connection

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

@SuppressLint("MissingPermission")
class ConnectionManager(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    fun isInternetAvailable(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    fun registerCallback(callback: ConnectivityManager.NetworkCallback) {
        networkCallback = callback
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    fun unregisterCallbacks() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }
}
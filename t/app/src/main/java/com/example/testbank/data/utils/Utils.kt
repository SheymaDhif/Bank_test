package com.example.testbank.data.utils

import android.content.Context
import android.net.ConnectivityManager
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object Utils {

    private val REACHABILITY_SERVER = "https://www.google.com"

    private fun hasNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        Timber.d("hasNetworkAvailable: ${(network != null)}")
        return network?.isConnected ?: false
    }

    fun hasInternetConnected(context: Context): Boolean {
        if (hasNetworkAvailable(context)) {
            try {
                val connection = URL(REACHABILITY_SERVER).openConnection() as HttpURLConnection
                connection.setRequestProperty("User-Agent", "Test")
                connection.setRequestProperty("Connection", "close")
                connection.connectTimeout = 1500
                connection.connect()
                Timber.d("hasInternetConnected: ${(connection.responseCode == 200)}")
                return connection.responseCode == 200
            } catch (e: IOException) {
                Timber.d("Error checking internet connection", e)
            }
        } else {
            Timber.d("No network available!")
        }
        Timber.d("hasInternetConnected: false")
        return false
    }


}
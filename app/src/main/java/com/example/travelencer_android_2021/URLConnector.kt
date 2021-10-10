package com.example.travelencer_android_2021

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

internal class URLConnector(private val URL: String) : Thread() {
    var result: String? = null
        private set

    override fun run() {
        val output = request(URL)
        result = output
    }

    private fun request(urlStr: String): String {
        val output = StringBuilder()
        try {
            val url = java.net.URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            if (conn != null) {
                conn.connectTimeout = 10000
                conn.requestMethod = "GET"
                conn.doInput = true
                conn.doOutput = true
                val resCode = conn.responseCode
                if (resCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    var line: String? = null
                    while (true) {
                        line = reader.readLine()
                        if (line == null) {
                            break
                        }
                        output.append(line + "\n")
                    }
                    reader.close()
                    conn.disconnect()
                }
            }
        } catch (ex: Exception) {
            Log.e("SampleHTTP", "Exception in processing response.", ex)
            ex.printStackTrace()
        }
        return output.toString()
    }
}
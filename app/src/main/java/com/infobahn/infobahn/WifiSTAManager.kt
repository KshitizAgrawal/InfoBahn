package com.infobahn.infobahn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.util.Log



class WifiSTAManager(context: Context) {

    private var mwifiManager: WifiManager
    private var context: Context

    companion object {
        private var networkId: Int = -1
    }

    init {
        this.context = context
        mwifiManager = this.context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun enableSTAPoints() {
        mwifiManager.setWifiEnabled(true)
    }

    fun disableSTAPoint() {
        mwifiManager.setWifiEnabled(false)
    }

    fun isSTAEnable(): Boolean {
        return mwifiManager.isWifiEnabled
    }

    fun checkWifiOnAndConnected(): Boolean {
        if (mwifiManager.isWifiEnabled) {

            val wifiInfo = mwifiManager.connectionInfo

            Log.i("networkId connected", wifiInfo.networkId.toString())

            return wifiInfo.networkId != -1
        } else {
            return false
        }
    }

    fun removeNetwork() {
        Log.i("networkId remove", WifiSTAManager.networkId.toString())
        mwifiManager.removeNetwork(WifiSTAManager.networkId)
    }

    /*
        search for available access points from DB.
     */
    fun connectAvailableAccessPoint() {
        Log.i("info", "getting scan results")
        val scanResultList:List<ScanResult>  = this.mwifiManager.scanResults
        Log.i("scanResult", scanResultList.toString())
        for (scanResult in scanResultList) {

            Log.i("scanResult.SSID", scanResult.SSID)

            //TODO("search ssid in database of credentials")
            if (scanResult.SSID=="OnePlus 7T") {
                Log.i("found AP", "OnePlus 7T")
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {

                    Log.i("info", "connecting to AP")

                    var wifiConfig = WifiConfiguration()
                    wifiConfig.SSID = "\"OnePlus 7T\""
                    wifiConfig.preSharedKey = "\"Whatever\""

                    WifiSTAManager.networkId = this.mwifiManager.addNetwork(wifiConfig)

                    this.mwifiManager.disconnect()
                    this.mwifiManager.enableNetwork(WifiSTAManager.networkId, true)
                    this.mwifiManager.reconnect()

                    Log.i("info", "connected to AP")
                }
                else {

                    val suggestion1 = WifiNetworkSuggestion.Builder()
                        .setSsid("Oneplus 7T")
                        .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                        .build()

                    val suggestion2 = WifiNetworkSuggestion.Builder()
                        .setSsid("Oneplus 7T")
                        .setWpa2Passphrase("Whatever")
                        .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                        .build()

                    val suggestion3 = WifiNetworkSuggestion.Builder()
                        .setSsid("Oneplus 7T")
                        .setWpa3Passphrase("Whatever")
                        .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                        .build()

                    val suggestionsList = listOf(suggestion1, suggestion2, suggestion3)

                    val status = this.mwifiManager.addNetworkSuggestions(suggestionsList);
                    if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                        // do error handling here
                    }

                    // Optional (Wait for post connection broadcast to one of your suggestions)
                    val intentFilter = IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);

                    val broadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            if (!intent.action.equals(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                                return;
                            }
                            // do post connect processing here
                        }
                    };
                    context.registerReceiver(broadcastReceiver, intentFilter);
                }
                break
            }
        }
    }

}
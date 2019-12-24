package com.infobahn.infobahn

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grantPermissions()

//        onRequestPermissionsResult()

        val mWifiApManager: WifiApManager = WifiApManager(this)

        if (mWifiApManager.isWifiApEnabled) {
            textViewAPStatus("ENABLED")
        }
        else {
            textViewAPStatus("DISABLED")
        }
    }

    fun onClickGiveButton(view: View) {
        val intent = Intent(this, GiveActivity::class.java)
        startActivity(intent)
    }

    fun onClickTakeButton(view: View) {

        Log.i("info", "changing avtivity to activity_take")
        val intent = Intent(this, TakeActivity::class.java)
        startActivity(intent)
    }

    fun textViewAPStatus(status: String) {
        val textViewStatus = findViewById<TextView>(R.id.status).apply {
            text = status
        }
    }
    
    fun grantPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
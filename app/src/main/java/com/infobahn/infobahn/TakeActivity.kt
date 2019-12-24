package com.infobahn.infobahn

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_take.*

class TakeActivity : AppCompatActivity() {

    lateinit var mwifiManager: WifiSTAManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take)

        mwifiManager = WifiSTAManager(this)

        if(!mwifiManager.checkWifiOnAndConnected()) {

            if (!mwifiManager.isSTAEnable()) mwifiManager.enableSTAPoints()

            mwifiManager.connectAvailableAccessPoint()
        }
    }

    override fun onBackPressed() {
        this.mwifiManager.checkWifiOnAndConnected()
        this.mwifiManager.removeNetwork()

        val intent = Intent(this, MainActivity::class.java).apply{}
        startActivity(intent)
    }

    fun onCLickDisconnectButton(view: View) {
        this.mwifiManager.checkWifiOnAndConnected()
        this.mwifiManager.removeNetwork()

        val intent = Intent(this, MainActivity::class.java).apply{}
        startActivity(intent)
    }

}

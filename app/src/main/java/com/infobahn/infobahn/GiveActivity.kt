package com.infobahn.infobahn

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.app.ActivityManager.RunningAppProcessInfo
import java.nio.file.Files.size
import android.os.Debug.getMemoryInfo
import android.app.ActivityManager
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T





class GiveActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_give)

        val context = this.getApplicationContext()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val writePermission = Settings.System.canWrite(context)

            /*
             * From android O, modify wifiAPConfig and setWIfiAP have been
             * moved to system only apps module
             * User will have to manually set ssid/presahredkey and switch on AP.
            */
            if (writePermission == true) {
                try {
                    val mWifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

                    val method = mWifiManager.javaClass.getMethod("getWifiApConfiguration")
                    val wifiConfig = method.invoke(mWifiManager) as WifiConfiguration
                } catch(e: Exception) {
                    Log.i("Exception", "unable to write AP configuration")
                }

            }
            else {
                Log.i("NO permission", "write")
//                modifySsytemSetting()
            }
        }

        textViewShowCreds()
        listRunningApps()
    }

    override fun onBackPressed() {
        minimizeApp()
    }

    fun onClickDummy(view: View) {
        toMainActivity()
    }

    fun textViewShowCreds() {

        var mCredentials: String = "Onboarded to our network.\n Thanks for joining our community."

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.System.canWrite(this.getApplicationContext())) {
                mCredentials = "Not allowed to change access point.\n Manually change credentials:\n SSID: kagrawal\n Password: 12345678"
            }
        }
        else {
            mCredentials = "Android version not supported"
        }

        val textView = findViewById<TextView>(R.id.showCredential).apply {
            text = mCredentials
        }
    }

    fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply{}
        startActivity(intent)
    }

    fun minimizeApp() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    fun modifySsytemSetting() {
//                        ActivityCompat.requestPermissions(this,
//                    arrayOf(
//                        Manifest.permission.WRITE_SETTINGS), 1)

        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        startActivity(intent)
        toMainActivity()
    }

    fun listRunningApps() {

        val tag = "Processes"

        val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(mInfo)
        val listOfRunningProcess = activityManager.runningAppProcesses
        Log.d(tag, "XXSize: " + listOfRunningProcess.size)

        for (runningAppProcessInfo in listOfRunningProcess) {

            if (runningAppProcessInfo.uid > 1026 || runningAppProcessInfo.uid == -5 || runningAppProcessInfo.processName.toString().contains("tethering") ) {
                Log.d(
                    tag, "ANS " + runningAppProcessInfo.processName +
                            " Id :" + runningAppProcessInfo.pid +
                            " UID: " + runningAppProcessInfo.uid
                )
            }
        }
    }

    fun checkMObileConnection() {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isMobileConn: Boolean = false
        connMgr.allNetworks.forEach { network ->
            connMgr.getNetworkInfo(network).apply {
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }
    }
}
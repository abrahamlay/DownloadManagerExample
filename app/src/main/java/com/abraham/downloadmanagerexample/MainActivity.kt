package com.abraham.downloadmanagerexample

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var downloadId:Long = 0

    val onDownloadComplete = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
            if(downloadId == id){
                Toast.makeText(this@MainActivity,"Download Complete",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(onDownloadComplete, object : IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE){})
        btn_download.setOnClickListener {
            download()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }

    private lateinit var req: DownloadManager.Request

    private fun download(){
        req = DownloadManager.Request(Uri.parse("https://via.placeholder.com/468x60?text=Visit+Blogging.com+Now"))
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setAllowedOverRoaming(false)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setTitle("Downloading")
            .setDescription("Some image from placeholder")
            .setVisibleInDownloadsUi(true)
            .setDestinationInExternalFilesDir(this,
                Environment.getDataDirectory().path,
                "blogging.png")

        val downloadManager :DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(req)
    }
}

package miq0717.downloadwithprogress

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_main.*
import miq0717.downloadwithprogress.DownloadService

class MainActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.progress)
    var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        registerReceiver()

        btn_download.setOnClickListener {
            startDownload()
        }
    }

//    @OnClick(R.id.btn_download)
//    fun downloadFile() {
//
////        if(checkPermission()){
//        startDownload()
//        //        } else {
////            requestPermission();
////        }
//    }

    private fun startDownload() {
        Log.e(TAG, "startDownload")
        val intent = Intent(this, DownloadService::class.java)
        startService(intent)
    }

    private fun registerReceiver() {
        Log.e(TAG, "registerReceiver")
        val bManager = LocalBroadcastManager.getInstance(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(MESSAGE_PROGRESS)
        bManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e(TAG, "onReceive")
            if (intent.action == MESSAGE_PROGRESS) {
                Log.e(TAG, "onReceive MESSAGE_PROGRESS")
                val downloadProgress: DownloadProgress? = intent.getParcelableExtra("download")
                //                mProgressBar.setProgress(download.getProgress());
                if (downloadProgress != null) Log.e(TAG, "onReceive" + downloadProgress.progress)
                if (downloadProgress != null && downloadProgress.progress == 100.0) {
                    progress_text?.text = "File Download Complete"
                    Toast.makeText(context,"Download Complete!",Toast.LENGTH_SHORT).show()
                } else {
                    progress_text?.text = String.format("Downloaded (%.2f/%.2f) MB", downloadProgress?.currentFileSize, downloadProgress?.totalFileSize)
                }
            }
        }
    }

    //    private boolean checkPermission(){

    //        int result = ContextCompat.checkSelfPermission(this,
    //                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    //        if (result == PackageManager.PERMISSION_GRANTED){
    //
    //            return true;
    //
    //        } else {
    //
    //            return false;
    //        }
    //    }
    //    private void requestPermission(){
    //
    //        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    //
    //    }
    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    //        switch (requestCode) {
    //            case PERMISSION_REQUEST_CODE:
    //                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //
    //                    startDownload();
    //                } else {
    //
    //                    Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();
    //
    //                }
    //                break;
    //        }
    //    }
    companion object {
        const val MESSAGE_PROGRESS = "message_progress"
        private const val PERMISSION_REQUEST_CODE = 1
        private const val TAG = "MainActivity"
    }
}
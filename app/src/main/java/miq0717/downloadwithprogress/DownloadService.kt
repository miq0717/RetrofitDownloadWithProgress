package miq0717.downloadwithprogress

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.*
import kotlin.math.pow

/*Created by MiQ0717 on 15-Jun-2020.*/
class DownloadService : IntentService("Download Service") {
    private var totalFileSize = 0.0
    override fun onHandleIntent(intent: Intent?) {
        initDownload()
    }

    private fun initDownload() {
        Log.e(TAG, "initDownload")
        val retrofit = Retrofit.Builder()
                .baseUrl("https://download.learn2crack.com/")
                .build()
        val retrofitInterface = retrofit.create(RetrofitInterface::class.java)
        val request = retrofitInterface.downloadFile("files/Node-Android-Chat.zip")
        try {
            downloadFile(request?.execute()?.body())
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun downloadFile(body: ResponseBody?) {
        Log.e(TAG, "downloadFile")
        var count: Int
        val data = ByteArray(1024 * 4)
        val fileSize = body!!.contentLength()
        val bis: InputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        val savePath = File(getExternalFilesDir(Context.STORAGE_SERVICE).toString() + File.separator + "DOWNLOADS")
        if (!savePath.exists()) {
            try {
                savePath.mkdirs()
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
        val outputFile = File(savePath, "file.zip")
        val output: OutputStream = FileOutputStream(outputFile)
        var total: Long = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        while (bis.read(data).also { count = it } != -1) {
            total += count.toLong()
            totalFileSize = (fileSize / 1024.0.pow(2.0))
            val current = total / 1024.0.pow(2.0)
            val progress = total * 100 / fileSize.toDouble()
            val currentTime = System.currentTimeMillis() - startTime
            val download = DownloadProgress()
            download.totalFileSize = totalFileSize
            if (currentTime > 1000 * timeCount) {
                download.currentFileSize = current
                download.progress = progress
                sendNotification(download)
                timeCount++
            }
            output.write(data, 0, count)
        }
        onDownloadComplete()
        output.flush()
        output.close()
        bis.close()
    }

    private fun sendNotification(downloadProgress: DownloadProgress) {
        Log.e(TAG, "sendNotification")
        sendIntent(downloadProgress)
    }

    private fun sendIntent(downloadProgress: DownloadProgress) {
        Log.e(TAG, "sendIntent")
        val intent = Intent(MainActivity.MESSAGE_PROGRESS)
        intent.putExtra("download", downloadProgress)
        LocalBroadcastManager.getInstance(this@DownloadService).sendBroadcast(intent)
    }

    private fun onDownloadComplete() {
        Log.e(TAG, "onDownloadComplete")
        val download = DownloadProgress()
        download.progress = 100.0
        sendIntent(download)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
//        notificationManager.cancel(0);
    }

    companion object {
        private const val TAG = "DownloadService"
    }
}
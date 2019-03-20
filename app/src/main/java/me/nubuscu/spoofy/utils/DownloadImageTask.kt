package me.nubuscu.spoofy.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream
import java.net.URL

class DownloadImageTask(val target: ImageView) : AsyncTask<String, Void, Bitmap?>() {
    override fun doInBackground(vararg urls: String?): Bitmap? {
        val url: String? = urls[0]
        var bmp: Bitmap? = null
        try {
            val inp: InputStream = URL(url).openStream()
            bmp = BitmapFactory.decodeStream(inp)
        } catch (e: Exception) {
            Log.e("image", "failed to get image", e)
        }
        return bmp
    }

    override fun onPostExecute(result: Bitmap?) {
        target.setImageBitmap(result)
    }

}
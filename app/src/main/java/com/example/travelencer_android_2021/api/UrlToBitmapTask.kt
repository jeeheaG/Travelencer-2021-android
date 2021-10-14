package com.example.travelencer_android_2021.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.URL


class URLtoBitmapTask() : AsyncTask<Void, Void, Bitmap>() {
    //액티비티에서 설정해줌
    lateinit var taskUrl: URL
    override fun doInBackground(vararg params: Void?): Bitmap {
        val bitmap = BitmapFactory.decodeStream(taskUrl.openStream())
        return bitmap
    }
    override fun onPreExecute() {
        super.onPreExecute()

    }
    override fun onPostExecute(result: Bitmap) {
        super.onPostExecute(result)
    }
}
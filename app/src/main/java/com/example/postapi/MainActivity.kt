package com.example.postapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        GlobalScope.launch(Dispatchers.IO){
            val url = "https://medic.madskill.ru/api/sendCode"
            val email = "lichka119@gmail.com"

            val requestBody = "".toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("accept", "application/json")
                .addHeader("email", email)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // обработка ошибки
                    Log.d("response",call.toString())

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string() // получаем тело ответа
                    val output = "\\\\u([0-9a-fA-F]{4})".toRegex()
                        .replace(responseBody.toString()) { matchResult ->
                            Integer.parseInt(matchResult.groupValues[1], 16)
                                .toChar()
                                .toString()
                        }

        

                    Log.d("response",output)
                }
            })

        }
    }
}
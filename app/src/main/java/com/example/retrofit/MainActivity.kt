package com.example.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            IHomeService.instance.getBanner().execute(object: NetCallback<List<BannerData>> {
                override fun onFailure(e: Exception?) {

                }

                override fun onSuccess(data: List<BannerData>?) {

                }
            })
        }
    }
}
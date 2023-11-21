package com.example.retrofit

import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * create by libo
 * create on 2021/6/28
 * description 单例的Retrofit和Okhttp管理类
 */
object ApiManager {
    /** 链接超时时间 */
    private val TIMEOUT = 10

    private val mOkhttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)  //连接超时设置
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)   //读取缓存超时10s
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)  //写入缓存超时10s
            .retryOnConnectionFailure(true)  //失败重连
            builder.build()
    }

//    val retrofitBuilder by lazy {
//        Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(mOkhttpClient)
//    }

    fun <T> create(cls: Class<T>, baseUrl: String): T {
        return Retrofit(mOkhttpClient, GsonConverterFactory(Gson())).createService(cls)
//        return retrofitBuilder.baseUrl(baseUrl).build().create(cls)
    }

}
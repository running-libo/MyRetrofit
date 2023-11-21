package com.example.retrofit

import android.telecom.Call
import com.example.retrofit.http.GET

/**
 * create by libo
 * create on 2021/6/28
 * description
 */
interface IHomeService {

    companion object {
        val instance = ApiManager.create(IHomeService::class.java, Api.SERVER_ADDRESS_RELEASE)

        fun invoke(): IHomeService {
            return instance
        }
    }

    /**
     * banner
     */
    @GET(Api.BANNER)
    suspend fun getBanner(): Call
}
package com.example.retrofit

import com.example.retrofit.call.NetCall
import com.example.retrofit.http.GET

/**
 * create by libo
 * create on 2021/6/28
 * description
 */
interface IHomeService {

    companion object {
        val instance = ApiManager.create(IHomeService::class.java)

        fun invoke(): IHomeService {
            return instance
        }
    }

    /**
     * banner
     */
    @GET(Api.BANNER)
    suspend fun getBanner(): NetCall<List<BannerData>>
}
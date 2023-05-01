package com.sonny.basetest.base.network

import retrofit2.Response
import retrofit2.http.*

interface BaseRemoteApi {
	
	@POST("test")
	fun getData(@Path("user_id") userId: String): Response<BaseResponse>
}

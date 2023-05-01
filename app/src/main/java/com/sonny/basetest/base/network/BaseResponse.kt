package com.sonny.basetest.base.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


open class BaseResponse : Throwable {
    @SerializedName("error")
    var apiError: ApiError? = null

    @SerializedName("success")
    var isSuccess: Boolean = false
    var statusCode: Int? = null

    constructor() {}
    constructor(message: String) {
        apiError = ApiError(message)
    }

    constructor(success: Boolean) {
        isSuccess = success
    }

    override fun toString(): String {
        return ("BaseResponse{" +
                "success=" + isSuccess +
                ", apiError=" + apiError +
                ", statusCode=" + statusCode +
                '}')
    }

    val isUnauthorizedError: Boolean
        get() {
            return ((apiError != null) && (apiError!!.isUnauthorizedError))
        }

    companion object {
        @JvmOverloads
        fun makeNetworkFailedError(httpStatus: Int? = null): BaseResponse {
            val baseResponse: BaseResponse = BaseResponse()
            baseResponse.apiError = ApiError()
            baseResponse.statusCode = httpStatus
            return baseResponse
        }

        fun makeUnknownError(httpStatus: Int?): BaseResponse {
            return makeNetworkFailedError(httpStatus)
        }

        fun makeNetworkFailedErrorJson(httpStatus: Int?): String {
            return Gson().toJson(makeNetworkFailedError(httpStatus))
        }
    }
}
package com.sonny.basetest.base.network

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiError {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("message")
    @Expose
    var message: String

    @SerializedName("violations")
    @Expose // Using JsonObject means it can't be null, something to bear in mind
    var violations: JsonObject? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    constructor() {
        code = "NETWORK_FAILED"
        message = "Unable to communicate with server. Please try again."
    }

    constructor(message: String) {
        this.message = message
    }

    override fun toString(): String {
        return ("ApiError{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", violations=" + violations +
                ", url=" + url +
                '}')
    }

    val isUnauthorizedError: Boolean
        get() {
            return (code != null) && ((code == UNAUTHORIZED_STATUS_CODE))
        }

    companion object {
        private val UNAUTHORIZED_STATUS_CODE: String = "UNAUTHORIZED"
    }
}
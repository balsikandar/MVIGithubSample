package com.balsikandar.github.data.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.io.Serializable


class Error(@field:SerializedName("code") val code: Int, @field:SerializedName("error") val error: String?) :
    RuntimeException(), Serializable {

    override val message: String
        get() = if (error == null || error.isEmpty()) String.format(
            "HTTP request failed (code=%d)",
            code
        ) else error

    companion object {
        fun parse(httpResponse: Response<*>): Error {
            if (httpResponse.isSuccessful) return Error(0, "")
            return try {
                //TODO create GsonUtils perhaps
                Gson().fromJson(
                    httpResponse.errorBody()!!.charStream(),
                    Error::class.java
                )
            } catch (e: Exception) {
                Error(httpResponse.code(), null)
            }
        }
    }

}
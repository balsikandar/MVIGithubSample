package com.balsikandar.common.utils

import com.google.gson.Gson

// only for use inside ui
object GsonUtils {
    private val gson: Gson by lazy { Gson() }
    fun gson() = gson
}

// unique name for any object in form of `ClassName(object hex code)`
val Any.name: String
    get() = "${javaClass.simpleName}(${Integer.toHexString(hashCode())})"

// class name for any object
val Any.type: String
    get() = javaClass.simpleName

// json for any object
fun Any.json(): String = GsonUtils.gson().toJson(this)
package com.jackson.imagefinder.utils

object StringUtils {

    fun defaultStr(str: String?, default: String = ""): String {
        return if (str.isNullOrEmpty()) default else str
    }

}
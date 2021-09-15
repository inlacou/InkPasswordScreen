package com.inlacou.inkpasswordscreenlibraryproject

import java.security.MessageDigest

fun String.toMD5(): String = MessageDigest.getInstance("MD5").digest(this.toByteArray()).toHex()
fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
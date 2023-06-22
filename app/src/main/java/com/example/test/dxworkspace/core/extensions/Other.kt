package com.example.test.dxworkspace.core.extensions

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import kotlin.coroutines.CoroutineContext

 val PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6J3DDsYRVeZ4g8HI5ni4Izb4xkepWp/ArqrI162hF7P/9sMegdeSKS9VAcqJzTFwTsrl2VB7c7jmISCLHLOcwlxeCGPaGqdTgKVKWne8bugb9XbOn2eEcBmj7hlzKC+0rP3pwszvBEjougI2vWi9Hd10LutwaCxyjEhtTyWYd1op5FjwBidA/UViAtZ0QMXp9Y44pk5IlFlolodFBBB0Hv/JvZy2M+7LNjM6c01q6UeOkl1BZz2mqM2gDp4p7EPu+CqODjjySV76C8k0QxqejUKoXPr7KMF7SsgxI9KeCvriltDxmhlhRiUnGniPWE+EVVbzdSQ/HBh0h7F2t8d4QQIDAQAB\n-----END PUBLIC KEY-----"

inline fun justTry(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        Log.d("TAG", "justTry: ${e.message} ")
    }
}

fun CoroutineScope.safeLaunch(context: CoroutineContext, block: suspend CoroutineScope.() -> Unit): Job {
    return this.launch(context) {
        try {
            block()
        } catch (ce: CancellationException) {

        } catch (e: Exception) {
            Log.e("TAG", "Coroutine error", e)
        }
    }
}


fun encryptMessage(message: String): String {

    val publicBytes: ByteArray = Base64.getDecoder().decode(PUBLIC_KEY
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\n", ""))
//        Base64.decode(PUBLIC_KEY.getBytes("UTF-8"), Base64.DEFAULT)

    // Tạo khóa công khai từ byte array
    val keySpec = X509EncodedKeySpec(publicBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    val publicKeyObj: PublicKey = keyFactory.generatePublic(keySpec)

    // Mã hóa thông điệp bằng khóa công khai
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKeyObj)
    val encryptedBytes: ByteArray = cipher.doFinal(message.toByteArray())

    // Trả về kết quả mã hóa dạng Base64
    return Base64.getEncoder().encodeToString(encryptedBytes)
}
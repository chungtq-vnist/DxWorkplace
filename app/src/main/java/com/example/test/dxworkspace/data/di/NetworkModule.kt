package com.example.test.dxworkspace.data.di

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.test.dxworkspace.BuildConfig
import com.example.test.dxworkspace.DxApplication
import com.example.test.dxworkspace.core.extensions.GetInfoDevice
import com.example.test.dxworkspace.core.extensions.encryptMessage
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Dns
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.get
import com.example.test.dxworkspace.data.remote.api.DxApi
import com.example.test.dxworkspace.data.remote.api.LoginApi
import okhttp3.logging.HttpLoggingInterceptor

@Module
class NetworkModule {

    companion object {
        const val BASE_URL = "https://dxclan.com:5000"
//         const val BASE_URL_DEV = "http://10.0.2.2:8000"
//        const val BASE_URL_DEV = "http://192.168.1.3:8000"
        const val BASE_URL_DEV = "http://119.17.214.105:8000"
        const val BASE_URL_MANUFACTURING_SERVICE = "119.17.214.105:8000/msf"
        const val IS_LIVE = true
        private const val CONNECT_TIMEOUT = 120L
        private const val READ_TIMEOUT = 120L
        private const val WRITE_TIMEOUT = 120L
        private const val ACCEPT_HEADER = "Content-Type"
        private const val X_FNB_TOKEN = "X-Fnb-Token"
        private const val X_APP_VERSION = "X-Version-App"
        private const val JSON_TYPE = "application/json"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(if(BuildConfig.DEBUG) BASE_URL_DEV else BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun createOkClient(sharedPreferences: SharedPreferences): OkHttpClient {
        val okHttpClientBuilder = unSafeOkHttpClient()
        okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.dns(object : Dns {
            override fun lookup(hostname: String) = InetAddress.getAllByName(hostname).toList()
        })

        if(BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor)
            okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
        }

        okHttpClientBuilder.addInterceptor { chain ->
            val request = chain.request()
            var builder = chain.request().newBuilder()
            val listPathManufacturing = listOf<String>("/manufacturing-dashboard","/manufacturing-works",
                "/manufacturing-mill","/manufacturing-command",
                "/manufacturing-plan","/work-schedule")
            // change url for manufacturing service
//            if(IS_LIVE && listPathManufacturing.firstOrNull { request.url.encodedPath.contains(it) } != null){
//                val path = request.url.encodedPath
//                val r = request.url.pathSegments.drop(1)
//                val urlRetry = request.url.newBuilder()
//                    .scheme("http") // hoặc "https" nếu bạn đang sử dụng kết nối bảo mật
//                    .host("119.17.214.105")
//                    .port(8000)
//                    .addPathSegment("msf") // Thêm path "/msf" vào URL
//                    .build()
//                builder = chain.request().newBuilder().url(urlRetry)
//            }

            if(!IS_LIVE){
                val urlRetry = request.url.newBuilder()
                    .removePathSegment(0)
                    .host("192.168.1.3")
                    .port(8000)
                    .build()
                builder = chain.request().newBuilder().url(urlRetry)
            }


            builder.addHeader(ACCEPT_HEADER, JSON_TYPE)
            builder.addHeader(X_APP_VERSION, BuildConfig.VERSION_NAME)
//            builder.addHeader("X-Type-App", "AndroidStaff")
//            SPSocketManage.INSTANCE?.mState.let {
//                builder.addHeader("X-Socket-App", if (it == SPSocketManage.StateSocket.SUCCESS) "1" else "0")
//            }
            DxApplication.getInstance().let {
                builder.addHeader("X-Device-Id", GetInfoDevice.getDeviceId(it))
            }

            val token: String? = sharedPreferences[Constants.APLogin.TOKEN]
            val currentRole: String? = sharedPreferences[Constants.APLogin.CURRENT_ROLE_ID]
            val currentPage: String? = sharedPreferences[Constants.APLogin.CURRENT_PAGE]
            if (!token.isNullOrEmpty()) {
                builder.removeHeader("utk")
                builder.addHeader("utk", token)
            }
            if (!currentPage.isNullOrEmpty()) {
                builder.removeHeader("crtp")
                builder.addHeader("crtp", encryptMessage(currentPage))
            }
            if (!currentRole.isNullOrEmpty()) {
                builder.removeHeader("crtr")
                builder.addHeader("crtr", encryptMessage(currentRole))
            }

            chain.proceed(builder.build())
        }

        return okHttpClientBuilder.build()
    }


    fun unSafeOkHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(
                    sslSocketFactory,
                    trustAllCerts.first() as X509TrustManager
                )
                okHttpClient.hostnameVerifier { _, _ -> true }
            }

            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }


    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit) : DxApi = retrofit.create(DxApi::class.java)


}
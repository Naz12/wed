package com.teknosols.a3orrsy.datamodel.source.Remote

import android.content.Context
import com.teknosols.a3orrsy.other.util.SessionManager
import com.teknosols.a3orrsy.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class RetrofitClientInstance (ctx: Context){

    private var retrofit: Retrofit? = null
    private val httpClient = OkHttpClient.Builder()
    var context: Context

    val BASE_URL = "http://earsna.com/admin/public/api/"

    init {
        context = ctx
        if (retrofit == null) {
            initRetrofit()
        }
    }

    fun initRetrofit(){
        var retrofitBuilder = retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        val authToken = SessionManager(context).getAuthenticationToken()
        if (authToken.isNotEmpty()) {
            val interceptor =
                AuthenticationInterceptor(
                    authToken,
                    context
                )
            httpClient.addInterceptor(interceptor)

            if(BuildConfig.DEBUG) {
                val loggingIntercepter = getLoggingInterceptor()
                loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(loggingIntercepter)
            }

            retrofitBuilder.client(httpClient.build())
        }else{
            val interceptor =
                AuthenticationInterceptor(
                    "",
                    context
                )
            httpClient.addInterceptor(interceptor)

            if(BuildConfig.DEBUG) {
                val loggingIntercepter = getLoggingInterceptor()
                loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(loggingIntercepter)
            }

            retrofitBuilder.client(httpClient.build())
        }

        retrofit = retrofitBuilder.build()
    }

    fun getService(): ApiService {
        return retrofit!!.create<ApiService>(ApiService::class.java!!)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor{
        val loggingIntercepter = HttpLoggingInterceptor()
        return loggingIntercepter
    }

    fun getRetrofit(): Retrofit? {
        return retrofit
    }


    companion object {
        var singleInstance: RetrofitClientInstance? = null

        fun getInstance(context: Context): RetrofitClientInstance? {
            if (singleInstance == null)
                singleInstance =
                    RetrofitClientInstance(context)

            return singleInstance
        }
    }

    class AuthenticationInterceptor internal constructor(private val authToken: String,private val context: Context) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()

            val builder = original.newBuilder()
                .header("x-access-token", authToken)

            val request = builder.build()

            //return chain.proceed(request)

            val response = chain.proceed(request)
            if (response.code() == 401) {
                SessionManager(context).logout()
                SessionManager(context).redirectToLogin(context,response.message())
            }

            return response
        }
    }

}
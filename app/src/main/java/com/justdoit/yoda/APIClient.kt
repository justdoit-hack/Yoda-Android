package com.justdoit.yoda

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.justdoit.yoda.api.UserService
import com.justdoit.yoda.api.FakeApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object APIClient {
    private const val API_URL = "https://yoda.wintu.dev/api/"
    private const val TEST_API_URL = "https://jsonplaceholder.typicode.com"

    val fakeApiService: FakeApiService = create(FakeApiService::class.java)
    val userService: UserService = create(UserService::class.java)
    //TODO 各Serviceの追加

    private lateinit var retrofit: Retrofit

    fun <S> create(serviceClass: Class<S>): S {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        // create retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)  //FIXME テスト用API以外のもの実装したら置き換え
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(serviceClass)
    }

    @SuppressLint("HardwareIds")
    fun getPhoneNumber(activity: Activity): String {
        val tm: TelephonyManager by lazy {
            activity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        }
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                0
            )
        }
        return tm.line1Number
    }

    fun toE164(originalPhoneNumber: String): String {
        val number = originalPhoneNumber.substring(1)
        return "+81$number"
    }

    fun getE164PhoneNumber(activity: Activity): String {
        return toE164(getPhoneNumber(activity))
    }
}

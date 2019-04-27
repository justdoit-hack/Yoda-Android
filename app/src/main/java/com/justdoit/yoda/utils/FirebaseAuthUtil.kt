package com.justdoit.yoda.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.justdoit.yoda.Yoda
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthUtil {
    private val tag = "FirebaseAuthUtil"
    private val tm: TelephonyManager by lazy {
        Yoda.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @SuppressLint("HardwareIds")
    fun getPhoneNumber(): String? {
        return when (ActivityCompat.checkSelfPermission(Yoda.instance, android.Manifest.permission.READ_PHONE_STATE)) {
            PackageManager.PERMISSION_GRANTED -> this.tm.line1Number.toE164()
            else -> null
        }
    }

    private fun String.toE164(): String {
        val number = this.substring(1)
        return "+81$number"
    }

    suspend fun getSMSCode(activity: Activity, phoneNumber: String): SMSEntity? {
        return GlobalScope.async(Dispatchers.Default) {
            getSMSCodeInternal(activity, phoneNumber)
        }.await()
    }

    private suspend fun getSMSCodeInternal(activity: Activity, phoneNumber: String): SMSEntity? {
        return suspendCoroutine { continuation ->
            var verificationID = ""
            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                // SMSコード受信！
                override fun onVerificationCompleted(credentitial: PhoneAuthCredential?) {
                    credentitial?.smsCode?.let { continuation.resume(SMSEntity(verificationID, it)) } ?: run {
                        continuation.resume(null)
                    }
                }

                // SMS送れない！
                override fun onVerificationFailed(e: FirebaseException?) {
                    Log.e(this@FirebaseAuthUtil.tag, e?.message, e)
                    continuation.resume(null)
                }

                override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                    verificationID = p0.toString()
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String?) {
                    verificationID = p0.toString()
                }
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,// E164形式
                10,
                TimeUnit.SECONDS,
                activity,
                callback
            )
        }
    }

    data class SMSEntity(
        val verificationID: String,
        val smsCode: String
    )
}
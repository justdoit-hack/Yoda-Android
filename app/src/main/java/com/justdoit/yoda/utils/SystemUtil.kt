package com.justdoit.yoda.utils

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SystemUtil {
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
                    continuation.resume(SMSEntity(verificationID, credentitial?.smsCode!!))
                }

                // SMS送れない！
                override fun onVerificationFailed(credentitial: FirebaseException?) {
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
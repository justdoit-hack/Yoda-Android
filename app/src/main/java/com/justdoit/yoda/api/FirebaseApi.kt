package com.justdoit.yoda.api

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseApi {
    suspend fun getIdToken(verificationID: String, smsCode: String): String {
        val auth = FirebaseAuth.getInstance()
        val credential = PhoneAuthProvider.getCredential(verificationID, smsCode)
        return GlobalScope.async(Dispatchers.Default) {
            getIdTokenInternal(auth, credential)
        }.await()
    }

    private suspend fun getIdTokenInternal(auth: FirebaseAuth, credential: PhoneAuthCredential): String {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<GetTokenResult> { tokenResult ->
                if (tokenResult.isSuccessful) {
                    val idToken = tokenResult.result?.token
                    idToken?.let { continuation.resume(idToken) }
                }
                continuation.resume("")
            }

            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.getIdToken(true)?.addOnCompleteListener(callback)
                } else {
                    Log.w("Phone", "signInWithPhoneAuthCredential:failure", task.exception)
                }
            }
            return@suspendCoroutine // resumeが呼ばれるまで待つ
        }
    }
}
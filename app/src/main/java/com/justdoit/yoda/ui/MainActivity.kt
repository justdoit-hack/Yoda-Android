package com.justdoit.yoda.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.justdoit.yoda.R
import com.justdoit.yoda.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gildor.coroutines.retrofit.awaitResponse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authToken = "854c3553137942199cc8cee96d101d70"

        val userRepository = UserRepository.getInstance()
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                try {
                    userRepository.fetchUser(authToken).awaitResponse().body()?.let {
                        Log.d("Login", "Login Success!")
                        Log.d("Login", it.user.toString())
                        //TODO SharedPreferenceで保存して
                    }
                } catch (e: Throwable) {
                    Log.e("Error", e.message)
                }
            }
        }
    }

}

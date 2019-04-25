package com.justdoit.yoda.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justdoit.yoda.R
import com.justdoit.yoda.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userRepository = UserRepository.getInstance()

        val phoneNumber = "08067788700"
        val password = "test1234"

        runBlocking {
            val result = userRepository.login(phoneNumber, password).awaitResult()
            when(result) {

            }
        }
    }

}

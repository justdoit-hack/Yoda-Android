package com.justdoit.yoda.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.justdoit.yoda.R
import com.justdoit.yoda.repository.UserRepository
import com.justdoit.yoda.utils.exec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userRepository = UserRepository.getInstance()

        val phoneNumber = "08067788700"
        val password = "test1234"
        
    }

}

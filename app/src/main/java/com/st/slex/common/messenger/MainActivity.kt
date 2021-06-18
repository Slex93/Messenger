package com.st.slex.common.messenger

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.st.slex.common.messenger.auth.model.AuthDatabase
import com.st.slex.common.messenger.auth.model.AuthRepository
import com.st.slex.common.messenger.auth.viewmodel.AuthViewModel
import com.st.slex.common.messenger.auth.viewmodel.AuthViewModelFactory
import com.st.slex.common.messenger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val database = AuthDatabase()
    private val repository by lazy { AuthRepository(database) }

    private val authScreenViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.sonny.basetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {
    private val sharedViewModel: MainActivityViewModel by viewModel(
        parameters = {
            parametersOf("Test")
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            collectUpdateData()
        }
    }


    private suspend fun collectUpdateData() {
        sharedViewModel.pageData.collect {
            it?.apply {
              // todo
            }
        }
    }

}
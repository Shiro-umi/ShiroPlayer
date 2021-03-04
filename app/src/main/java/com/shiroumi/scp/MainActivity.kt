package com.shiroumi.scp

import android.os.Bundle
import android.os.Handler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPage()
        }

        Handler(mainLooper).postDelayed(
            { viewModel.apply { autoInitialize { getDocument() } } }, 2000L
        )
    }

    @Preview
    @Composable
    fun MainPage() {
        val title = viewModel.title.observeAsState()
        title.value?.let { Text(it) }
    }
}
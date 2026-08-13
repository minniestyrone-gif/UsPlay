package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.UsPlayViewModel
import com.example.ui.screens.MainScreen
import com.example.ui.theme.UsPlayTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      UsPlayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          val usPlayViewModel: UsPlayViewModel = viewModel()
          MainScreen(viewModel = usPlayViewModel)
        }
      }
    }
  }
}


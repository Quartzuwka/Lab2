package com.example.lab2

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lab2.ui.theme.Lab2Theme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel


class CurrencyViewModelFactory(val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CbrViewModel(application) as T
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2Theme {
                val owner = LocalViewModelStoreOwner.current

                owner?.let {
                    val mainViewModel: CbrViewModel = viewModel(
                        it,
                        "CbrViewModel",
                        CurrencyViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    App(viewModel = mainViewModel)
                }
            }
        }
    }

    @Composable
    fun App(viewModel: CbrViewModel = viewModel()) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center, // Aligns children vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
            ) {
                ToggleableButtonsRow()
                Text("123")
            }
            Text(
                text = "VS",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp), // Отступы слева и справа
                textAlign = TextAlign.Center
            )
            Column(
                verticalArrangement = Arrangement.Center, // Aligns children vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
            ) {
                ToggleableButtonsRow()
                Text("123")
            }
        }
    }

    @Composable
    fun ToggleableButtonsRow() {
        // Хранит индекс активной кнопки
        var selectedButtonIndex = remember { mutableStateOf(-1) }

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val buttons = listOf("RUB", "USD", "EUR")

            buttons.forEachIndexed { index, text ->
                Box(
                    modifier = Modifier
                        .padding(0.dp)
                        .size(40.dp, 40.dp)
                        .background(
                            color = if (selectedButtonIndex.value == index) Color.Green else Color.Gray,
                            shape = RectangleShape
                        )
                        .clickable {
                            selectedButtonIndex.value =
                                if (selectedButtonIndex.value == index) -1 else index
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

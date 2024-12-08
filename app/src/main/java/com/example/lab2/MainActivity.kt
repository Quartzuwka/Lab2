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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



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
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "main_screen"
        ) {
            composable("main_screen") {
                MainScreen(viewModel, navController)
            }
            composable("selection_screen") {
                SelectionScreen(viewModel, navController)
            }
        }
    }

    @Composable
    fun MainScreen(viewModel: CbrViewModel, navController: NavController) {
        // Хранит индекс активной кнопки
        var selectedButtonIndex = remember { mutableStateOf(-1) }
        // Хранит текст для кнопки "Custom"
        var firstButtonSelection = remember { mutableStateOf("Custom1") }
        var secondButtonSelection = remember { mutableStateOf("Custom2") }


        // Слушаем изменения savedStateHandle
        val lifecycleOwner = LocalLifecycleOwner.current
        navController.currentBackStackEntry?.savedStateHandle
            ?.getLiveData<String>("selectedItem")
            ?.observe(lifecycleOwner) { selectedItem ->
                firstButtonSelection.value = selectedItem
                selectedButtonIndex.value = 3
            }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val buttons = listOf(firstButtonSelection.value, secondButtonSelection.value)
                OutlinedButton(

                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("selection_screen") }
                ) {
                    Text(text = buttons[0])
                }
                OutlinedButton(

                    modifier = Modifier.weight(1f),
                    onClick = { }
                ) {
                    Text(text = buttons[1])
                }

                }
            }
        }
    }


    @Composable
    fun SelectionScreen(vm: CbrViewModel, navController: NavController) {
        val itemList = vm.parsedList


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(itemList.value) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {

                            // Возвращаем выбранное значение на первый экран
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selectedItem", item.name)
                            navController.popBackStack() // Возврат на первый экран
                        },
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .background(Color.LightGray)
                            .padding(16.dp),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }



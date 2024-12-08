package com.example.lab2


import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.lab2.network.CbrXmlParser
import com.example.lab2.network.CurrencyItem
import com.example.lab2.network.KtorClientWebService
import com.example.lab2.network.WebService
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.FileInputStream

class CbrViewModel(application: Application) : ViewModel() {

//    private val _uiState = MutableStateFlow(uiState())
//    val uiState: StateFlow<uiState> = _uiState.asStateFlow()

    private val _uiState = MutableStateFlow(uiState())
    val uiState: StateFlow<uiState> get() = _uiState

    var parsedList = mutableStateOf<List<CurrencyItem>>(emptyList())
        private set // Закрытый сеттер, чтобы избежать изменения извне
    val webService: WebService = KtorClientWebService()
    var CurrentItem: CurrencyItem? = null
    val newItem = CurrencyItem(1F, 1,  "Российский рубль", "RUB")

    init {
            viewModelScope.launch() {
                val xmlString = webService.getXMlString("https://www.cbr.ru//scripts/XML_daily.asp")
                parsedList.value = CbrXmlParser().parsing(xmlString)
                val parsedData = CbrXmlParser().parsing(xmlString)
                parsedList.value = listOf(newItem) + parsedData
            }
        }


    fun putFirstItem(item: CurrencyItem) {
        _uiState.update { currentState ->
            currentState.copy(
                firstButtonText = item.name
            )
        }
        CurrentItem = item
    }
    fun putSecondItem(item: CurrencyItem) {
        _uiState.update { currentState ->
            currentState.copy(
                secondButtonText = item.name
            )
        }
        CurrentItem = item
    }

    fun setCurrentButton(cur: String){
        _uiState.update { currentState ->
            currentState.copy(
                currentButton = cur
            )
        }
    }


}

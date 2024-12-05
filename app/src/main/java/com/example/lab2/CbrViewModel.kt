package com.example.lab2


import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.lab2.network.CbrXmlParser
import com.example.lab2.network.KtorClientWebService
import com.example.lab2.network.WebService
import java.io.FileInputStream

class CbrViewModel(application: Application) : ViewModel() {



    init {
        val webService: WebService = KtorClientWebService()
            viewModelScope.launch() {
                val xmlString = webService.getXMlString("https://www.cbr.ru//scripts/XML_daily.asp")
                val parsed = CbrXmlParser().parsing(xmlString)
                println(parsed)
            }
        }
}

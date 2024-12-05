package com.example.lab2.network

interface WebService {
    suspend fun getXMlString(url: String): String
}
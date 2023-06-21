package com.example.myapplication.domain.util

fun simplifyErrorMessage(genericError: String?): String {

    if (genericError?.contains("Unable to resolve host") == true) {
        return "No Internet Connection"
    }
    return "Some Error Occurred. Please try again"
}
package com.example.beinconnectmoviesapp.Util

sealed class Resource<T> (val data: T? = null, val message: String? = null, var title: String? = null) {
    class Success<T>(_data: T) : Resource<T>(data = _data)
    class Error<T>(_message: String) : Resource<T>(message = _message)
    object Loading : Resource<Any>()
    object Empty : Resource<Any>()
}

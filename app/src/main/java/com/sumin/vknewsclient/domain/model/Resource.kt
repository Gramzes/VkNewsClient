package com.sumin.vknewsclient.domain.model

sealed interface Resource<T>{
    class Loading<T>(): Resource<T>
    class Data<T>(val data: T): Resource<T>
    class EndOfData<T>(val data: T): Resource<T>
}
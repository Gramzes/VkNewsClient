package com.sumin.vknewsclient.domain.model

sealed class Resource<T>(val data: T){

    class Loading<T>(data: T): Resource<T>(data)
    class Data<T>(data: T): Resource<T>(data)
    class EndOfData<T>(data: T): Resource<T>(data)

    fun copy(data: T): Resource<T>{
        return when(this){
            is Data -> Data(data)
            is EndOfData -> EndOfData(data)
            is Loading -> Loading(data)
        }
    }
}
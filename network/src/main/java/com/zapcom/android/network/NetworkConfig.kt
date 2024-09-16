package com.zapcom.android.networking

import javax.inject.Inject


data class NetworkConfig @Inject constructor(val baseURL:String){

    companion object{
        internal const val BASE_URL = "jsonkeeper.com"
        const val URL_PATH = "b/5BEJ"
    }
}
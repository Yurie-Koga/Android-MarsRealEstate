/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
/** When just use JSON string **/
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

// 1. Base URL
private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

// 5. Create Moshi instance to use JSON string by converting to Kotlin objects
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// 2. Create a Retrofit object (client library)
private val retrofit = Retrofit.Builder()
    // Retrofit has a ScalarsConverter that supports strings and other primitive types
//    .addConverterFactory(ScalarsConverterFactory.create()) /** When just use JSON string **/
    .addConverterFactory(MoshiConverterFactory.create(moshi))   /** When use JSON string by converting to Kotlin objects **/
    .baseUrl(BASE_URL)
    .build()

// 3. Define interfaces with annotations
interface MarsApiService {
    // Generated Path: BASE_URL + EndPoint: "realestate"
    // 'getProperties()' gets the JSON response string from the web service
    // Annotation tells Retrofit what this method should do
    @GET("realestate")            // import retrofit2.http.GET
//    fun getProperties(): Call<String>   // import retrofit2.Call: start the request   /** For JSON string **/
    suspend fun getProperties(): List<MarsProperty> /** For Kotlin objects **/
}

// 4. Initialize the Retrofit service: Must be public to share within this entire app
object MarsApi {
    // Must lazily initialize since this call is computationally expensive
    val retrofitService: MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }
}
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

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch
import java.lang.Exception

/** Status of the web request **/
enum class MarsApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MarsApiStatus>
        get() = _status

    // The internal MutableLiveData for deserialized Kotlin object from internet
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    // To navigate the detail page when the image in grid view tapped
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSeletedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     * Call the Retrofit service and handle the returned JSON string.
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
       viewModelScope.launch {
           _status.value = MarsApiStatus.LOADING
           try {
               // getProperties()('GET' method) returns data from internet as Kotlin objects 'MarsProperty'
               _properties.value = MarsApi.retrofitService.getProperties(filter.value)
               _status.value = MarsApiStatus.DONE

           } catch (e: Exception) {
               _status.value = MarsApiStatus.ERROR
               // Clear RecyclerView
               _properties.value = ArrayList()
           }
       }
    }

    /** Called when filter value is changed in the option menu **/
    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    /** Set the selected property **/
    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    /** Clear navigation state **/
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }
}

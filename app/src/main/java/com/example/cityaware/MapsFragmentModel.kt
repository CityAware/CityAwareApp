package com.example.cityaware

import android.os.Bundle
import androidx.lifecycle.ViewModel

class MapsFragmentModel : ViewModel() {
    private var savedInstanceStateData: Bundle? = null

    fun getSavedInstanceStateData(): Bundle? {
        return savedInstanceStateData
    }

    fun setSavedInstanceStateData(savedInstanceStateData: Bundle?) {
        this.savedInstanceStateData = savedInstanceStateData
    }
}


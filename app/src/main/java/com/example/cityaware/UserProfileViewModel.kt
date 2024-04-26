package com.example.cityaware

import androidx.lifecycle.ViewModel


class UserProfileViewModel : ViewModel() {
    private var activeState = false

    fun getActiveState(): Boolean {
        return activeState
    }

    fun setActiveState(active: Boolean) {
        activeState = active
    }
}
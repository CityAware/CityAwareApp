package com.example.cityaware


import androidx.lifecycle.ViewModel
import com.example.cityaware.model.Post



class PostsListFragmentViewModel : ViewModel() {
    var data: List<Post> = mutableListOf()
}
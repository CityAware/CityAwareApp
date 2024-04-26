package com.example.cityaware


import androidx.lifecycle.ViewModel
import com.example.cityaware.model.Post
import java.util.LinkedList


class PostsListFragmentViewModel : ViewModel() {
    private var data: List<Post> = LinkedList()

    fun getData(): List<Post> {
        return data
    }

    fun setData(list: List<Post>) {
        data = list
    }
}
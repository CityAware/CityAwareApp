package com.example.cityaware


import androidx.lifecycle.ViewModel
import com.example.cityaware.model.Post
import java.util.LinkedList


class PostsListFragmentViewModel : ViewModel() {
    companion object {
        var data: List<Post> = LinkedList()
    }
}
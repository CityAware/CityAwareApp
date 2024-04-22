package com.example.cityaware.model;

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Model private constructor() {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel = FirebaseModel()
    var localDb: AppLocalDbRepository = AppLocalDb.getAppDb
    fun signOut() {
        firebaseModel.signOut()
    }

    interface Listener<T> {
        fun onComplete(data: T)
    }

    val auth: FirebaseAuth
        get() = firebaseModel.auth

    fun getUserPosts(label: String?, callback: Listener<List<Post?>?>?) {
        firebaseModel.getUserPosts(label, callback)
    }

    fun getAllPosts(callback: Listener<List<Post?>?>?) {
        firebaseModel.getAllPosts(callback)
    }

    fun addPost(st: Post?, listener: Listener<Void?>?) {
        firebaseModel.addPost(st!!, listener!!)
    }

    fun uploadImage(name: String?, bitmap: Bitmap?, listener: Listener<String?>?) {
        firebaseModel.uploadImage(name!!, bitmap!!, listener!!)
    }

    fun signUp(
        email: String?,
        label: String?,
        password: String?,
        listener: Listener<android.util.Pair<Boolean?, String?>?>
    ) {
        firebaseModel.signUp(email, label, password, listener!!)
    }

    fun login(email: String?, password: String?, listener: Listener<android.util.Pair<Boolean?, String?>?>) {
        firebaseModel.login(email, password, listener!!)
    }

    fun getPostById(id: String?, listener: Listener<Post?>?) {
        firebaseModel.getPostById(id, listener!!)
    }

    val db: FirebaseFirestore
        get() = firebaseModel.getDb()

    fun updatePostById(id: String?, updates: Map<String?, Any?>?) {
        firebaseModel.updatePostByid(id, updates!!)
    }

    companion object {
        private val _instance = Model()
        fun instance(): Model {
            return _instance
        }
    }
}


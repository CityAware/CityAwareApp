package com.example.cityaware.model;

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.cityaware.model.AppLocalDb.appDb
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class Model private constructor() {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel: FirebaseModel = FirebaseModel()
    var localDb = appDb

    interface Listener<T> {
        fun onComplete(data: T)
    }

    fun getAllPosts(callback: Listener<List<Post?>?>?) {
        (callback as Listener<List<Post>?>?)?.let { firebaseModel.getAllPosts(it) }
        //        executor.execute(()->{
//            List<Post> data = localDb.studentDao().getAll();
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainHandler.post(()->{
//                callback.onComplete(data);
//            });
//        });
    }

    fun addPost(st: Post?, listener: Listener<Void?>?) {
        if (listener != null) {
            if (st != null) {
                firebaseModel.addPost(st, listener)
            }
        }
        //        executor.execute(()->{
//            localDb.PostDao().insertAll(st);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainHandler.post(()->{
//                listener.onComplete();
//            });
//        });
    }

    fun uploadImage(name: String?, bitmap: Bitmap?, listener: Listener<String?>?) {
        listener?.let { bitmap?.let { it1 -> name?.let { it2 -> firebaseModel.uploadImage(it2, it1, it) } } }
    }
    fun signUp(email: String?, password: String?, listener: (Any) -> Unit) {
        firebaseModel.signUp(email, password, listener)
    }
    fun login(email: String?, password: String?, listener: Listener<String?>?) {
        firebaseModel.login(email, password, listener!!)
    }

    companion object {
        private val _instance = Model()
        fun instance(): Model {
            return _instance
        }
    }
}
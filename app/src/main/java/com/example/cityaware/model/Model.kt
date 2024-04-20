package com.example.cityaware.model;

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.cityaware.model.AppLocalDb.appDb
import java.util.concurrent.Executor


class Model private constructor() {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel: FirebaseModel = FirebaseModel()
    var localDb = appDb

    interface Listener<T> {
        fun onComplete(data: T)
    }

    fun getAllPosts(callback: Listener<List<Post?>?>?) {
        firebaseModel.getAllPosts(callback)
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
        firebaseModel.addPost(st, listener)
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
        firebaseModel.uploadImage(name, bitmap, listener)
    }

    companion object {
        private val _instance = Model()
        fun instance(): Model {
            return _instance
        }
    }
}
package com.example.cityaware.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import androidx.core.os.HandlerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();
//    AppLocalDbRepository localDb = AppLocalDb.INSTANCE.getAppDb();

    public static Model instance() {
        return _instance;
    }

    private Model() {

    }

    public void signOut() {
        firebaseModel.signOut();
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    public FirebaseAuth getAuth() {
        return firebaseModel.auth;
    }

    public void getUserPosts(String label, Listener<List<Post>> callback) {
        firebaseModel.getUserPosts(label, callback);
    }

    public void getAllPosts(Listener<List<Post>> callback) {
        firebaseModel.getAllPosts(callback);
    }

    public void addPost(Post st, Listener<Void> listener) {
        firebaseModel.addPost(st, listener);
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name, bitmap, listener);
    }

    public void signUp(String email, String label, String password, Listener<Pair<Boolean, String>> listener) {
        firebaseModel.signUp(email, label, password, listener);
    }

    public void login(String email, String password, Listener<Pair<Boolean, String>> listener) {
        firebaseModel.login(email, password, listener);
    }

    public void getPostById(String id, Listener<Post> listener) {
        firebaseModel.getPostById(id, listener);
    }

    public FirebaseFirestore getDb() {
        return firebaseModel.getDb();
    }

    public void updatePostById(String id, Map<String, Object> updates) {
        firebaseModel.updatePostByid(id, updates);
    }
    public void updateUserLabelByLabel(Context context, String label, Map<String, Object> updates) {
        firebaseModel.updateUserLabelByLabel(context,label, updates);
    }
}


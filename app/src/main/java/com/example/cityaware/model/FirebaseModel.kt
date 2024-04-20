package com.example.cityaware.model;


import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.LinkedList


class FirebaseModel internal constructor() {
    var db: FirebaseFirestore
    var storage: FirebaseStorage
    var auth: FirebaseAuth

    init {
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.setFirestoreSettings(settings)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun getAllPosts(callback: Model.Listener<List<Post>?>) {
        db.collection(Post.COLLECTION).get().addOnCompleteListener { task ->
            val list: MutableList<Post> = LinkedList()
            if (task.isSuccessful) {
                val jsonsList = task.result
                for (json in jsonsList) {
                    val post = Post.fromJson(json.data)
                    list.add(post)
                }
            }
            callback.onComplete(list)
        }
    }

    fun addPost(post: Post, listener: Model.Listener<Void?>) {
        db.collection(Post.COLLECTION).document(post.id).set(post.toJson())
            .addOnCompleteListener { listener.onComplete(null) }
    }

    fun uploadImage(name: String, bitmap: Bitmap, listener: Model.Listener<String?>) {
        val storageRef = storage.getReference()
        val imagesRef = storageRef.child("images/$name.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener { listener.onComplete(null) }.addOnSuccessListener {
            imagesRef.getDownloadUrl()
                .addOnSuccessListener { uri -> listener.onComplete(uri.toString()) }
        }
    }
}
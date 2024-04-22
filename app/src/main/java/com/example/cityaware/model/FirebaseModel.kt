package com.example.cityaware.model;

import android.graphics.Bitmap
import android.util.Pair
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.LinkedList


class FirebaseModel internal constructor() {
    var db: FirebaseFirestore
    var storage: FirebaseStorage
    var auth: FirebaseAuth
    var CurrUser: FirebaseUser? = null

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

    /*public void signUp(String email,String label, String password, Model.Listener<Boolean> listener) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    listener.onComplete(true);
                }
                else{listener.onComplete(false);}
            }
        });
    }*/
    fun signUp(
        email: String?,
        label: String?,
        password: String?,
        listener: Model.Listener<Pair<Boolean, String>?>
    ) {
        db.collection(User.COLLECTION).whereEqualTo(User.ACCOUNT_LABEL, label).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        auth.createUserWithEmailAndPassword(email!!, password!!)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Add the new user to the database
                                    val user = User(email, label)
                                    db.collection(User.COLLECTION)
                                        .add(user.toJson()).addOnCompleteListener {
                                            listener.onComplete(
                                                Pair(
                                                    true,
                                                    "Sign up success"
                                                )
                                            )
                                        }
                                } else {
                                    val exception = task.exception
                                    if (exception is FirebaseAuthUserCollisionException) {
                                        // Email already exists
                                        listener.onComplete(
                                            Pair(
                                                false,
                                                "Email already exists"
                                            )
                                        )
                                    } else {
                                        // Other error like connection issue or password under 6 characters / invalid characters
                                        listener.onComplete(
                                            Pair(
                                                false,
                                                "Sign up failed"
                                            )
                                        )
                                    }
                                }
                            }
                    } else {
                        listener.onComplete(
                            Pair(
                                false,
                                "Label is taken"
                            )
                        )
                    }
                } else {
                    listener.onComplete(
                        Pair(
                            false,
                            "Error checking for unique label"
                        )
                    )
                }
            }
    }

    fun login(email: String?, password: String?, listener: Model.Listener<Boolean?>) {
        auth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                CurrUser = auth.currentUser
                listener.onComplete(true)
            } else {
                listener.onComplete(false)
            }
        }
    }
}
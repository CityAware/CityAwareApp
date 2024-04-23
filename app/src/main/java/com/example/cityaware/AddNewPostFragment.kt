package com.example.cityaware.model

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation.findNavController
import com.example.cityaware.R
import com.example.cityaware.databinding.FragmentAddPostBinding
import com.google.firebase.Timestamp
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException



public class AddNewPostFragment : Fragment() {
    companion object {
        var binding: FragmentAddPostBinding? = null
        var cameraLauncher: ActivityResultLauncher<Void?>? = null
        var galleryLauncher: ActivityResultLauncher<String?>? = null
        var isAvatarSelected = false
        var sp: SharedPreferences? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parentActivity = activity
        parentActivity!!.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.removeItem(R.id.addNewPostFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, this, Lifecycle.State.RESUMED)
        cameraLauncher=registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                binding!!.avatarImg.setImageBitmap(bitmap)
                isAvatarSelected = true
            }
        }
        galleryLauncher = registerForActivityResult<String, Uri>(
            ActivityResultContracts.GetContent(),
            object : ActivityResultCallback<Uri?> {
                override fun onActivityResult(result: Uri?) {
                    if (result != null) {
                        binding!!.avatarImg.setImageURI(result)
                        isAvatarSelected = true
                    }
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater!!, container, false)
        val view: View = binding!!.getRoot()
        binding!!.addLocationBtn.setOnClickListener { view ->
            findNavController(view).navigate(
                R.id.mapsFragment,
                savedInstanceState
            )
        }
        binding!!.saveBtn.setOnClickListener { view1 ->
            val title = binding!!.postTitle.getText().toString()
            val details = binding!!.postDes.getText().toString()
            val location = binding!!.address.getText().toString()
            val label: String = sp!!.getString("label", "")!!
            var id = title
            try {
                val digest =
                    MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(
                    (title +
                            Timestamp.now().seconds +
                            Timestamp.now().nanoseconds + "")
                        .toByteArray(StandardCharsets.UTF_8)
                )
                val stringBuilder = StringBuilder()
                for (b in hash) {
                    stringBuilder.append(String.format("%02x", b.toInt() and 0xff))
                }
                id = stringBuilder.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            val post = Post(
                id!!,
                title,
                "",
                details,
                location,
                label,
                Timestamp.now().seconds
            )
            if (details == "" || title == "") {
                Toast.makeText(context, "missing title or details ", Toast.LENGTH_LONG).show()
            } else {
               if(isAvatarSelected){
                   binding!!.avatarImg.setDrawingCacheEnabled(true)
                   binding!!.avatarImg.buildDrawingCache()
                   val bitmap = (binding!!.avatarImg.getDrawable() as BitmapDrawable).bitmap
                   Model.instance().uploadImage(id, bitmap, object : Model.Listener<String?> {
                       override fun onComplete(data: String?) {
                           if (data != null) {
                               post.imgUrl = data
                           }
                            Model.instance().addPost(post, object : Model.Listener<Void?> {
                                 override fun onComplete(data: Void?) {
                                      findNavController(view1).popBackStack()
                                 }
                            })
                       }
                   })}else{
                     Model.instance().addPost(post, object : Model.Listener<Void?> {
                          override fun onComplete(data: Void?) {
                            findNavController(view1).navigate(R.id.postsListFragment)
                          }
                     })
                   }


               }
            }
        binding!!.cancelBtn.setOnClickListener { view1 ->
            findNavController(view1).popBackStack(
                R.id.postsListFragment,
                false
            )
        }
        binding!!.cameraButton.setOnClickListener { view1 -> cameraLauncher!!.launch(null) }
        binding!!.galleryButton.setOnClickListener { view1 -> galleryLauncher!!.launch("media/*") }
        return view
    }



    }

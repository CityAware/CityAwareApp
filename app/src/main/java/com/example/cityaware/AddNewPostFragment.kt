package com.example.cityaware

import android.R
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
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation.findNavController
import com.example.cityaware.model.Post
import com.example.cityaware.databinding.FragmentAddPostBinding
import com.example.cityaware.model.Model

class AddNewPostFragment : Fragment() {
    var binding: FragmentAddPostBinding? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var galleryLauncher: ActivityResultLauncher<String>? = null
    var isAvatarSelected = false
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
        cameraLauncher = registerForActivityResult<Void?, Bitmap>(
            TakePicturePreview(),
            object : ActivityResultCallback<Bitmap?> {
                override fun onActivityResult(result: Bitmap) {
                    if (result != null) {
                        binding.avatarImg.setImageBitmap(result)
                        isAvatarSelected = true
                    }
                }
            })
        galleryLauncher = registerForActivityResult<String, Uri>(
            GetContent(),
            object : ActivityResultCallback<Uri?> {
                override fun onActivityResult(result: Uri) {
                    if (result != null) {
                        binding.avatarImg.setImageURI(result)
                        isAvatarSelected = true
                    }
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view: View = binding.getRoot()
        binding.saveBtn.setOnClickListener { view1 ->
            val name: String = binding.nameEt.getText().toString()
            val stId: String = binding.idEt.getText().toString()
            val post = Post(stId, name, "", "")
            if (name == "" || stId == "") {
                Toast.makeText(context, "missing name or ID", Toast.LENGTH_LONG).show()
            } else {
                if (isAvatarSelected) {
                    binding.avatarImg.setDrawingCacheEnabled(true)
                    binding.avatarImg.buildDrawingCache()
                    val bitmap =
                        (binding.avatarImg.getDrawable() as BitmapDrawable).bitmap
                    Model.instance().uploadImage(stId, bitmap) { url ->
                        if (url != null) {
                            post.imgUrl = url
                        }
                        Model.instance()
                            .addPost(post) { unused -> findNavController(view1).popBackStack() }
                    }
                } else {
                    Model.instance()
                        .addPost(post) { unused -> findNavController(view1).popBackStack() }
                }
            }
        }
        binding.cancellBtn.setOnClickListener { view1 ->
            findNavController(view1).popBackStack(
                R.id.postsListFragment,
                false
            )
        }
        binding.cameraButton.setOnClickListener { view1 -> cameraLauncher!!.launch(null) }
        binding.galleryButton.setOnClickListener { view1 -> galleryLauncher!!.launch("media/*") }
        return view
    }
}
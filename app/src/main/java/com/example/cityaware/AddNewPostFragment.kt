package com.example.karenhub

import android.R
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.cityaware.MapsFragmentModel
import com.example.cityaware.model.Post
import com.example.karenhub.databinding.FragmentAddPostBinding
import com.example.karenhub.model.Model
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class AddNewPostFragment : Fragment() {
    var binding: FragmentAddPostBinding? = null
    var location: LatLng? = null
    var locationName: String? = null
    var x: Double? = null
    var y: Double? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var galleryLauncher: ActivityResultLauncher<String>? = null
    var sp: SharedPreferences? = null
    var isAvatarSelected = false
    private var bottomNavigationView: BottomNavigationView? = null
    var viewModelProvider: ViewModelProvider? = null
    var viewModel: MapsFragmentModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        sp = context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val bundle = arguments
        if (!bundle!!.isEmpty()) {
            location = bundle.getParcelable("location")
            locationName = bundle.getString("locationName")
        }
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
                        val viewModelProvider = ViewModelProvider(activity!!)
                        val viewModel = viewModelProvider.get(
                            MapsFragmentModel::class.java
                        )
                        binding.avatarImg.setImageBitmap(result)
                        var bundle: Bundle? = Bundle()
                        if (viewModel.getSavedInstanceStateData() != null) {
                            bundle = viewModel.getSavedInstanceStateData()
                        }
                        bundle!!.putParcelable("imgBitmap", result)
                        viewModel.setSavedInstanceStateData(bundle)
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
        bottomNavigationView =
            activity!!.findViewById<BottomNavigationView>(R.id.main_bottomNavigationView)
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view: View = binding.getRoot()
        viewModelProvider = ViewModelProvider(activity!!)
        viewModel = viewModelProvider!!.get(MapsFragmentModel::class.java)
        if (locationName != null) {
            binding.address.setText(locationName)
        }
        binding.addLocationBtn.setOnClickListener(View.OnClickListener { view ->
            findNavController(
                view
            ).navigate(R.id.mapsFragment, savedInstanceState)
        })
        binding.saveBtn.setOnClickListener { view1 ->
            val title: String = binding.postTitle.getText().toString()
            val details: String = binding.postDes.getText().toString()
            val location: String = binding.address.getText().toString()
            val label = sp!!.getString("label", "")
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
                id,
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
                if (isAvatarSelected) {
                    binding.avatarImg.setDrawingCacheEnabled(true)
                    binding.avatarImg.buildDrawingCache()
                    val bitmap =
                        (binding.avatarImg.getDrawable() as BitmapDrawable).bitmap
                    Model.instance().uploadImage(id, bitmap) { url ->
                        if (url != null) {
                            post.imgUrl = url
                        }
                        Model.instance()
                            .addPost(post) { unused -> findNavController(view1).popBackStack() }
                    }
                } else {
                    Model.instance()
                        .addPost(post) { unused -> findNavController(view1).navigate(R.id.postsListFragment) }
                }
            }
        }
        binding.cancelBtn.setOnClickListener { view1 ->
            findNavController(view1).popBackStack(
                R.id.postsListFragment,
                false
            )
        }
        binding.cameraButton.setOnClickListener { view1 -> cameraLauncher!!.launch(null) }
        binding.galleryButton.setOnClickListener { view1 -> galleryLauncher!!.launch("media/*") }
        return view
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setShowHideAnimationEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        val viewModelProvider = ViewModelProvider(activity!!)
        val viewModel = viewModelProvider.get(
            MapsFragmentModel::class.java
        )
        val savedInstanceStateData = viewModel.getSavedInstanceStateData()
        if (savedInstanceStateData != null) {
            location = viewModel.getSavedInstanceStateData()!!.getParcelable("location")
            locationName = viewModel.getSavedInstanceStateData()!!.getString("locationName")
            if (locationName != null) {
                binding.address.setText(locationName)
            }
            val bitmap = viewModel.getSavedInstanceStateData()!!.getParcelable<Bitmap>("imgBitmap")
            if (bitmap != null) {
                binding.avatarImg.setImageBitmap(bitmap)
            }
        } else {
            viewModel.setSavedInstanceStateData(Bundle())
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setShowHideAnimationEnabled(true)
        viewModel!!.setSavedInstanceStateData(Bundle())
    }

    companion object {
        fun newInstance(location: LatLng?, locationName: String?): AddNewPostFragment {
            val newPostFragment = AddNewPostFragment()
            val bundle = Bundle()
            bundle.putParcelable("location", location)
            bundle.putString("locationName", locationName)
            newPostFragment.setArguments(bundle)
            return newPostFragment
        }
    }
}
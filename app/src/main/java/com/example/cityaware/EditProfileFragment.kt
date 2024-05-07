package com.example.cityaware

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.cityaware.databinding.FragmentEditProfileBinding
import com.example.cityaware.model.Model
import com.example.cityaware.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class EditProfileFragment : Fragment() {
    private var binding: FragmentEditProfileBinding? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var sp: SharedPreferences? = null
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var isAvatarSelected = false
    private var viewModelProvider: ViewModelProvider? = null
    private var userViewModel: UserProfileViewModel? = null
    var updates: MutableMap<String, Any>? = null

    private var email: String? = null
    private var label: String? = null
    private var id: String? = null
    private var imgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.main_bottomNavigationView)
        sp = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        // ... rest of your onCreate code ...
        updates = HashMap()
        val parentActivity = activity
        parentActivity!!.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.removeItem(R.id.editUserProfileFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, this, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        viewModelProvider = ViewModelProvider(requireActivity())

        // Get the user's current profile information
        val user = User.fromJson(sp!!.all)
        email = user.email
        label = user.label
//        id = user.id


        // Display the user's current profile information
        binding!!.edituserEmail.setText(email)
        binding!!.edituserLabel.setText(label)
        Picasso.get().load(imgUrl).into(binding!!.avatarImgEditUser)

        // ... rest of your onCreateView code ...

        binding!!.saveEditUserProfile.setOnClickListener { view ->
            // Get the updated profile information
            val updatedEmail = binding!!.edituserEmail.text.toString()
            val updatedLabel = binding!!.edituserLabel.text.toString()

            if (updatedEmail != null && updatedEmail !== email) {
                updates!!["email"] = updatedEmail
            }
            if (updatedLabel != null && updatedLabel !== label) {
                updates!!["label"] = updatedLabel
            }
            Log.d("Updates", updates.toString())  // Add this line
            Model.instance().updateUserById(id, updates)

//            if (imgUrl != null && imgUrl != "") {
//                Picasso.get().load(imgUrl).into(binding!!.avatarImgEditUser)
//            }
//            if (isAvatarSelected) {
//                binding!!.avatarImgEditUser.isDrawingCacheEnabled = true
//                binding!!.avatarImgEditUser.buildDrawingCache()
//                val bitmap = (binding!!.avatarImgEditUser.drawable as BitmapDrawable).bitmap
//                Model.instance().uploadImage(id, bitmap) { url: String? ->
//                    if (url != null) {
//                        updates!!["imgUrl"] = url
//                        imgUrl = url
//                        Model.instance().updateUserById(id, updates)
//                    }
//                }
//            } else {
//            }
            viewModelProvider = ViewModelProvider(requireActivity())
            userViewModel = viewModelProvider!!.get(UserProfileViewModel::class.java)
            requireActivity().finish()
            val bundle = ActivityOptionsCompat.makeCustomAnimation(
                requireContext(), android.R.anim.fade_in, android.R.anim.fade_out
            )
                .toBundle()
            startActivity(requireActivity().intent, bundle)
            if (userViewModel!!.activeState) {
                Navigation.findNavController(view).navigate(R.id.userProfile)
            }



            // Navigate back to the previous screen
            Navigation.findNavController(view).popBackStack()
        }

        binding!!.cancelBtnEditUserProfile.setOnClickListener { view ->
            // Discard any changes and navigate back to the previous screen
            Navigation.findNavController(view).popBackStack()
        }

//        binding!!.imageBtnEditUserImg.setOnClickListener { view1: View? -> cameraLauncher!!.launch(null) }
//        binding!!.galleryBtnEditPost.setOnClickListener { view1: View? -> galleryLauncher!!.launch("media/*") }
        return view
    }
    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        bottomNavigationView!!.visibility = View.GONE
        (activity as AppCompatActivity?)!!.supportActionBar!!.setShowHideAnimationEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    @SuppressLint("RestrictedApi")
    override fun onStop() {
        super.onStop()
        bottomNavigationView!!.visibility = View.VISIBLE
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setShowHideAnimationEnabled(true)
    }
    override fun onResume() {
        super.onResume()
        val viewModelProvider = ViewModelProvider(requireActivity())
        val viewModel = viewModelProvider.get(
            MapsFragmentModel::class.java
        )
        val savedInstanceStateData = viewModel.savedInstanceStateData
        if (savedInstanceStateData != null) {

            val bitmap = viewModel.savedInstanceStateData!!.getParcelable<Bitmap>("imgBitmap")
            if (bitmap != null) {
                binding!!.avatarImgEditUser.setImageBitmap(bitmap)
            }
        } else {
            viewModel.savedInstanceStateData = Bundle()
        }
    }


}
package com.example.cityaware

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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.cityaware.AddNewPostFragment.Companion.viewModel
import com.example.cityaware.databinding.FragmentEditPostBinding
import com.example.cityaware.databinding.FragmentEditProfileBinding
import com.example.cityaware.databinding.FragmentUserProfileBinding
import com.example.cityaware.model.Model
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class EditProfileFragment : Fragment(){
    var email: String? = null
    var binding: FragmentEditProfileBinding? = null
    var imgUrl: String? = null
    var label: String? = null
    var id: String? = null
    var updates: MutableMap<String, Any>? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var galleryLauncher: ActivityResultLauncher<String>? = null
    var sp: SharedPreferences? = null
    var isAvatarSelected = false
    private var bottomNavigationView: BottomNavigationView? = null
    var viewModelProvider: ViewModelProvider? = null
    var userViewModel: UserProfileViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.main_bottomNavigationView)
        sp = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
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
        cameraLauncher = registerForActivityResult<Void?, Bitmap>(ActivityResultContracts.TakePicturePreview()) { result ->
            if (result != null) {
                viewModelProvider = ViewModelProvider(requireActivity())
                val viewModel = viewModelProvider!!.get(
                    MapsFragmentModel::class.java
                )
                binding!!.avatarImgEditUser.setImageBitmap(result)
                var bundle: Bundle? = Bundle()
                if (viewModel.savedInstanceStateData != null) {
                    bundle = viewModel.savedInstanceStateData
                }
                bundle!!.putParcelable("imgBitmap", result)
                viewModel.savedInstanceStateData = bundle
                isAvatarSelected = true
            }
        }
        galleryLauncher = registerForActivityResult<String, Uri>(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                binding!!.avatarImgEditUser.setImageURI(result)
                isAvatarSelected = true
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        id = requireArguments().getString("userId")
        imgUrl = requireArguments().getString("editImgUrl")
        label = requireArguments().getString("editLabel")
        email = requireArguments().getString("editEmail")
        val view: View = binding!!.root

        //show previous post details
        if (label != null) {
            binding!!.edituserLabel.setText(label)
        }
        if (imgUrl != null && imgUrl != "") {
            Picasso.get().load(imgUrl).into(binding!!.avatarImgEditUser)
        }
        if (email != null) {
            binding!!.edituserEmail.setText(email)
        }
        //save btn
        binding!!.saveEditUserProfile.setOnClickListener { view1: View? ->
            val editedLabel = binding!!.edituserLabel.text.toString()
            val editedEmail = binding!!.edituserEmail.text.toString()
            if (editedLabel != null && editedLabel !== label) {
                updates!!["label"] = editedLabel
                if (editedEmail != null && editedEmail !== email) {
                    updates!!["email"] = editedEmail
                }

                if (isAvatarSelected) {
                    binding!!.avatarImgEditUser.isDrawingCacheEnabled = true
                    binding!!.avatarImgEditUser.buildDrawingCache()
                    val bitmap = (binding!!.avatarImgEditUser.drawable as BitmapDrawable).bitmap
                    Model.instance().uploadImage(id, bitmap) { url: String? ->
                        if (url != null) {
                            updates!!["imgUrl"] = url
                            imgUrl = url
                            Model.instance().updateUserById(id, updates)
                        }
                    }
                } else {
                    Model.instance().updateUserById(id, updates)
                }
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
            }
        }
            binding!!.cancelBtnEditUserProfile.setOnClickListener { view1: View? ->
                Navigation.findNavController(
                    view1!!
                ).popBackStack(R.id.postFragment, false)
            }
            binding!!.imageBtnEditUserImg.setOnClickListener { view1: View? ->
                cameraLauncher!!.launch(
                    null
                )
            }
            binding!!.galleryBtnEditPost.setOnClickListener { view1: View? ->
                galleryLauncher!!.launch(
                    "media/*"
                )
            }
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
            viewModel!!.savedInstanceStateData = (Bundle())
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

        companion object {
            fun newInstance(): EditProfileFragment {
                val EditProfileFragment = EditProfileFragment()
                val bundle = Bundle()
                EditProfileFragment.arguments = bundle
                return EditProfileFragment
            }
        }

}
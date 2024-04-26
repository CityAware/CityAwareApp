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
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.cityaware.databinding.FragmentEditPostBinding
import com.example.cityaware.model.Model
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso


class EditPostFragment : Fragment() {
    var binding: FragmentEditPostBinding? = null
    var location: LatLng? = null
    var locationName: String? = null
    var title: String? = null
    var details: String? = null
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
    var viewModel: MapsFragmentModel? = null
    var userViewModel: UserProfileViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.main_bottomNavigationView)
        sp = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        updates = HashMap()
        val bundle = arguments
        if (!bundle!!.isEmpty()) {
            location = bundle.getParcelable("location")
            locationName = bundle.getString("locationName")
        }
        val parentActivity = activity
        parentActivity!!.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.removeItem(R.id.editPostFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, this, Lifecycle.State.RESUMED)
        cameraLauncher = registerForActivityResult<Void?, Bitmap>(
            ActivityResultContracts.TakePicturePreview(),
            object : ActivityResultCallback<Bitmap?> {
                override fun onActivityResult(result: Bitmap?) {
                    if (result != null) {
                        viewModelProvider = ViewModelProvider(activity!!)
                        val viewModel = viewModelProvider!![MapsFragmentModel::class.java]
                        binding!!.avatarImgEditPost.setImageBitmap(result)
                        var bundle = Bundle()
                        if (viewModel.getSavedInstanceStateData() != null) {
                            bundle = viewModel.getSavedInstanceStateData()!!
                        }
                        bundle.putParcelable("imgBitmap", result)
                        viewModel.setSavedInstanceStateData(bundle)
                        isAvatarSelected = true
                    }
                }
            })
        galleryLauncher = registerForActivityResult<String, Uri>(
            ActivityResultContracts.GetContent(),
            object : ActivityResultCallback<Uri?> {

                override fun onActivityResult(result: Uri?) {
                    if (result != null) {
                        binding!!.avatarImgEditPost.setImageURI(result)
                        isAvatarSelected = true
                    }
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPostBinding.inflate(inflater, container, false)
        id = requireArguments().getString("postId")
        imgUrl = requireArguments().getString("editImgUrl")
        location = requireArguments().getParcelable("location")
        locationName = requireArguments().getString("locationName")
        label = requireArguments().getString("editLabel")
        title = requireArguments().getString("EditTitle")
        details = requireArguments().getString("Editdetails")
        val view: View = binding!!.getRoot()
        viewModelProvider = ViewModelProvider(requireActivity())
        viewModel = viewModelProvider!![MapsFragmentModel::class.java]
        if (locationName != null) {
            binding!!.addresseditpost.setText(locationName)
        }
        //show previous post details
        if (title != null) {
            binding!!.editpostTitle.setText(title)
        }
        if (details != null) {
            binding!!.editpostDescription.setText(details)
        }
        if (imgUrl != null && imgUrl != "") {
            Picasso.get().load(imgUrl).into(binding!!.avatarImgEditPost)
        }
        if (locationName != null) {
            binding!!.addresseditpost.setText(locationName)
        }
        binding!!.addLoctionEditPost.setOnClickListener(View.OnClickListener { view ->
            findNavController(
                view
            ).navigate(R.id.mapsFragment, savedInstanceState)
        })

        //save btn
        binding!!.saveEditPost.setOnClickListener { view1 ->
            val editedTitle: String = binding!!.editpostTitle.getText().toString()
            val editedDetails: String = binding!!.editpostDescription.getText().toString()
            val editedLocation: String = binding!!.addresseditpost.getText().toString()
            if (editedTitle != null && editedTitle !== title) {
                updates!!["title"] = editedTitle
            }
            if (editedDetails != null && editedDetails !== details) {
                updates!!["details"] = editedDetails
            }
            if (!editedLocation.isEmpty() || locationName != null) {
                updates!!["location"] = editedLocation
            }
            if (isAvatarSelected) {
                binding!!.avatarImgEditPost.setDrawingCacheEnabled(true)
                binding!!.avatarImgEditPost.buildDrawingCache()
                val bitmap =
                    (binding!!.avatarImgEditPost.getDrawable() as BitmapDrawable).bitmap
                Model.instance().uploadImage(id, bitmap, object : Model.Listener<String?> {
                    override fun onComplete(data: String?) {
                        if (data != null) {
                            updates!!["image"] = data
                            imgUrl = data
                            Model.instance().updatePostById(id, updates)
                        } else {
                            Model.instance().updatePostById(id, updates)
                        }

                    }

                })

            }
            viewModelProvider = ViewModelProvider(requireActivity())
            userViewModel = viewModelProvider!!.get(UserProfileViewModel::class.java)
            requireActivity().finish()
            val bundle = ActivityOptionsCompat.makeCustomAnimation(
                requireContext(), android.R.anim.fade_in, android.R.anim.fade_out
            )
                .toBundle()
            startActivity(requireActivity().intent, bundle)
            if (userViewModel!!.getActiveState()) {
                findNavController(view).navigate(R.id.userProfile)
            }
        }
        binding!!.cancelBtnEditPost.setOnClickListener { view1 ->
            findNavController(view1).popBackStack(
                R.id.postFragment,
                false
            )
        }
        binding!!.imageBtnEditPost.setOnClickListener { view1 -> cameraLauncher!!.launch(null) }
        binding!!.galleryBtnEditPost.setOnClickListener { view1 -> galleryLauncher!!.launch("media/*") }
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
        viewModel!!.setSavedInstanceStateData(Bundle())
    }

    override fun onResume() {
        super.onResume()
        val viewModelProvider = ViewModelProvider(requireActivity())
        val viewModel = viewModelProvider[MapsFragmentModel::class.java]
        val savedInstanceStateData: Bundle = viewModel.getSavedInstanceStateData()!!
        if (savedInstanceStateData != null) {
            location = viewModel.getSavedInstanceStateData()!!.getParcelable("location")
            locationName = viewModel.getSavedInstanceStateData()!!.getString("locationName")
            if (locationName != null) {
                binding!!.addresseditpost.setText(locationName)
            }
            val bitmap: Bitmap = viewModel.getSavedInstanceStateData()!!.getParcelable("imgBitmap")!!
            if (bitmap != null) {
                binding!!.avatarImgEditPost.setImageBitmap(bitmap)
            }
        } else {
            viewModel.setSavedInstanceStateData(Bundle())
        }
    }

    companion object {
        fun newInstance(): EditPostFragment {
            val newEditPostFragment = EditPostFragment()
            val bundle = Bundle()
            newEditPostFragment.setArguments(bundle)
            return newEditPostFragment
        }
    }
}

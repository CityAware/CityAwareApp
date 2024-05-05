package com.example.cityaware

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.example.cityaware.model.FirebaseModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController


class PostFragment : Fragment() {


    var titleTv: TextView? = null
    var detailsTv: TextView? = null
    var locationTV: TextView? = null
    var labelTV: TextView? = null
    var title: String? = null
    var details: String? = null
    var location: String? = null
    var imgUrl: String? = null
    var label: String? = null
    var image: ImageView? = null
    var id: String? = null
    var sp: SharedPreferences? = null
    fun newInstance(
        title: String?,
        details: String?,
        location: String?,
        ImgUrl: String?,
        label: String?,
        id: String?
    ): PostFragment {
        val frag = PostFragment()
        val bundle = Bundle()
        bundle.putString("TITLE", title)
        bundle.putString("DETAILS", details)
        bundle.putString("LOCATION", location)
        bundle.putString("IMAGE", ImgUrl)
        bundle.putString("LABEL", label)
        bundle.putString("ID", id)
        frag.setArguments(bundle)
        return frag
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val bundle = arguments
        if (bundle != null) {
            title = bundle.getString("TITLE")
            details = bundle.getString("DETAILS")
            location = bundle.getString("LOCATION")
            imgUrl = bundle.getString("IMAGE")
            label = bundle.getString("LABEL")
            id = bundle.getString("ID")
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        val button = view.findViewById<View>(R.id.editBtn_postFrag)
        val deleteButton = view.findViewById<View>(R.id.deleteBtn_postFrag)
        button.visibility = View.INVISIBLE
        deleteButton.visibility = View.INVISIBLE
        //show post details
        title = PostFragmentArgs.fromBundle(requireArguments()).postTitle
        details = PostFragmentArgs.fromBundle(requireArguments()).postDetails
        location = PostFragmentArgs.fromBundle(requireArguments()).postLocInfo
        imgUrl = PostFragmentArgs.fromBundle(requireArguments()).PostImgUrl
        label = PostFragmentArgs.fromBundle(requireArguments()).postLabel
        id = PostFragmentArgs.fromBundle(requireArguments()).postId
        val titleTv = view.findViewById<TextView>(R.id.postfrag_title_tv)
        if (title != null) {
            titleTv.text = title
        }
        detailsTv = view.findViewById(R.id.postDetails_tv)
        if (details != null) {
            detailsTv!!.setText(details)
        }
        locationTV = view.findViewById(R.id.postLocation)
        if (location != null) {
            locationTV!!.setText(location)
        }
        image = view.findViewById(R.id.postUrl_blueFrag)
        if (imgUrl != null && imgUrl != "") {
            Picasso.get().load(imgUrl).into(image)
        }
        labelTV = view.findViewById(R.id.labelTv)
        if (label != null) {
            labelTV!!.setText(label)
        }

        //check if user has permissions  to edit
        val currUserLabel = sp!!.getString("label", "")
        if (currUserLabel == label) {
            deleteButton.visibility=View.VISIBLE
                deleteButton.setOnClickListener { view ->
                    Log.d("PostFragment", id!!)
                    FirebaseModel().deletePostById(id) {
                        // Navigate back to the home page
                    }
                    val action: NavDirections = PostFragmentDirections.actionPostFragmentToPostsListFragment()
                    findNavController(view).navigate(action)
                }

            button.visibility = View.VISIBLE
            button.setOnClickListener { view ->
                val action = PostFragmentDirections.actionPostFragmentToEditPostFragment(
                    LatLng(0.0, 0.0), location!!, id!!, title!!, details!!, label!!, imgUrl!!
                )
                findNavController(view).navigate(action)
            }
        }
        return view
    }



    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    companion object {
        fun newInstance(
            title: String?,
            details: String?,
            location: String?,
            ImgUrl: String?,
            label: String?,
            id: String?
        ): PostFragment {
            val frag = PostFragment()
            val bundle = Bundle()
            bundle.putString("TITLE", title)
            bundle.putString("DETAILS", details)
            bundle.putString("LOCATION", location)
            bundle.putString("IMAGE", ImgUrl)
            bundle.putString("LABEL", label)
            bundle.putString("ID", id)
            frag.setArguments(bundle)
            return frag
        }
    }
}
package com.example.cityaware


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.cityaware.databinding.FragmentUserProfileBinding
import com.example.cityaware.model.Model
import com.google.android.material.bottomnavigation.BottomNavigationView

class userProfile constructor() : Fragment() {
    private var mViewModel: UserProfileViewModel? = null
    var binding: FragmentUserProfileBinding? = null
    private var bottomNavigationView: BottomNavigationView? = null
    var sp: SharedPreferences? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider: ViewModelProvider = ViewModelProvider(requireActivity())
        mViewModel = viewModelProvider.get(UserProfileViewModel::class.java)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomNavigationView = requireActivity().findViewById(R.id.main_bottomNavigationView)
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view: View = binding!!.getRoot()
        sp = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        binding!!.profileLabel.setText(sp!!.getString("label", ""))
        binding!!.profileEmail.setText(sp!!.getString("email", ""))
        binding!!.logout.setOnClickListener((View.OnClickListener({ view1: View? ->
            Model.instance().signOut()
            val editor: SharedPreferences.Editor = sp!!.edit()
            editor.clear()
            editor.apply()
            val i: Intent = Intent(getContext(), LogInActivity::class.java)
            val bundle: Bundle? = ActivityOptionsCompat.makeCustomAnimation(
                requireContext(), android.R.anim.fade_in, android.R.anim.fade_out
            )
                .toBundle()
            startActivity(i, bundle)
            requireActivity().finish()
        })))
        return view
    }

    public override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profilePostListFragment: Fragment = PostsListFragment()
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.profile_posts_frame, profilePostListFragment)
        transaction.commit()
    }

    public override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    public override fun onStart() {
        super.onStart()
        mViewModel!!.activeState = true
        (getActivity() as AppCompatActivity?)!!.getSupportActionBar()!!
            .setShowHideAnimationEnabled(false)
    }

    public override fun onResume() {
        super.onResume()
        mViewModel!!.activeState = true
    }

    @SuppressLint("RestrictedApi")
    public override fun onStop() {
        super.onStop()
        mViewModel!!.activeState = false
        (getActivity() as AppCompatActivity?)!!.getSupportActionBar()!!
            .setShowHideAnimationEnabled(true)
    }

    companion object {
        fun newInstance(): userProfile {
            return userProfile()
        }
    }
}
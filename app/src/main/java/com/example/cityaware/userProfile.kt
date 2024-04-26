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
import com.example.cityaware.R
import com.example.cityaware.databinding.FragmentUserProfileBinding
import com.example.cityaware.model.Model
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserProfile : Fragment() {
    private lateinit var mViewModel: UserProfileViewModel
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sp: SharedPreferences

    companion object {
        fun newInstance() = UserProfile()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(requireActivity())
        mViewModel = viewModelProvider.get(UserProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bottomNavigationView = requireActivity().findViewById(R.id.main_bottomNavigationView)
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view: View = binding.root
        sp = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        binding.profileLabel.text = sp.getString("label", "")
        binding.profileEmail.text = sp.getString("email", "")
        binding.logout.setOnClickListener {
            Model.instance().signOut()
            val editor = sp.edit()
            editor.clear()
            editor.apply()
            val i = Intent(context, LogInActivity::class.java)
            val bundle = ActivityOptionsCompat.makeCustomAnimation(
                requireContext(), android.R.anim.fade_in, android.R.anim.fade_out
            ).toBundle()
            startActivity(i, bundle)
            requireActivity().finish()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profilePostListFragment = PostsListFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.profile_posts_frame, profilePostListFragment)
        transaction.commit()
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        mViewModel.setActiveState(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.setActiveState(true)
    }

    @SuppressLint("RestrictedApi")
    override fun onStop() {
        super.onStop()
        mViewModel.setActiveState(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(true)
    }
}
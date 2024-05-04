package com.example.cityaware


import android.app.FragmentManager

import android.content.Context

import android.content.SharedPreferences

import android.os.Bundle


import androidx.annotation.NonNull

import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider

import androidx.navigation.NavController

import androidx.navigation.NavDestination

import androidx.navigation.NavDirections

import androidx.navigation.Navigation

import androidx.recyclerview.widget.LinearLayoutManager


import android.util.Log

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup


import com.example.cityaware.databinding.FragmentPostsListBinding

import com.example.cityaware.model.Model

import com.example.cityaware.model.Post

import com.google.android.material.bottomnavigation.BottomNavigationView

import com.squareup.picasso.MemoryPolicy

import com.squareup.picasso.Picasso


class PostsListFragment : Fragment() {
    private lateinit var binding: FragmentPostsListBinding
    private lateinit var adapter: PostRecyclerAdapter
    private lateinit var postListViewModel: PostsListFragmentViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sp: SharedPreferences
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var userViewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomNavigationView = requireActivity().findViewById(R.id.main_bottomNavigationView)
        binding = FragmentPostsListBinding.inflate(inflater, container, false)
        val view: View = binding.root
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PostRecyclerAdapter(inflater, postListViewModel.data)
        binding.recyclerView.adapter = adapter
        viewModelProvider = ViewModelProvider(requireActivity())
        userViewModel = viewModelProvider.get(UserProfileViewModel::class.java)

        adapter.setOnItemClickListener(object : PostRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                val st: Post = postListViewModel.data.get(pos)!!
                Picasso.get().invalidate(st.imgUrl)
                val action = PostsListFragmentDirections.actionPostsListFragmentToPostFragment(
                    st.title!!, st.details!!, st.location!!, st.label!!, st.imgUrl!!, st.id
                )
                Navigation.findNavController(view).navigate(action as NavDirections)
            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        postListViewModel = ViewModelProvider(this).get(PostsListFragmentViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        reloadData(userViewModel.activeState)
    }

    override fun onResume() {
        super.onResume()
        reloadData(userViewModel.activeState)
    }

    override fun onStop() {
        super.onStop()
    }

    fun reloadData(activeState: Boolean) {
        binding.progressBar.visibility = View.VISIBLE
        if (!activeState) {
            Model.instance().getAllPosts(object : Model.Listener<List<Post?>?> {
                override fun onComplete(stList: List<Post?>?) {
                    val nonNullList = stList?.filterNotNull() ?: listOf()
                    postListViewModel.data=nonNullList // set the data in the view model
                    adapter.data = postListViewModel.data
                    binding.progressBar.visibility = View.GONE
                }
            })
        } else {
            sp = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            Model.instance().getUserPosts(sp.getString("label", ""), object : Model.Listener<List<Post?>?> {
                override fun onComplete(stList: List<Post?>?) {
                    val nonNullList = stList?.filterNotNull() ?: listOf()
                    postListViewModel.data=nonNullList // set the data in the view model
                    adapter.data = postListViewModel.data
                    binding.progressBar.visibility = View.GONE
                }
            })
        }
    }
}
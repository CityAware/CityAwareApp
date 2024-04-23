package com.example.cityaware


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.createNavigateOnClickListener
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cityaware.model.Post
import com.example.cityaware.databinding.FragmentPostsListBinding
import com.example.cityaware.model.Model


class PostsListFragment : Fragment() {
    var binding: FragmentPostsListBinding? = null
    var adapter: PostRecyclerAdapter? = null
    var viewModel: PostsListFragmentViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostsListBinding.inflate(inflater, container, false)
        val view: View = binding!!.getRoot()
        binding!!.recyclerView.setHasFixedSize(true)
        binding!!.recyclerView.setLayoutManager(LinearLayoutManager(context))
        adapter = PostRecyclerAdapter(getLayoutInflater(), viewModel!!.data)
        binding!!.recyclerView.setAdapter(adapter)
        adapter!!.setOnItemClickListener(object : PostRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                Log.d("TAG", "Row was clicked $pos")
                val st: Post = viewModel!!.data.get(pos)
                val action: PostsListFragmentDirections.ActionPostsListFragmentToBlueFragment =
                    PostsListFragmentDirections.actionPostsListFragmentToBlueFragment(st.title)
                findNavController(view).navigate((action as NavDirections))
            }
        })
        val addButton = view.findViewById<View>(R.id.btnAdd)
        val action: NavDirections = PostsListFragmentDirections.actionGlobalAddPostFragment()
        addButton.setOnClickListener(createNavigateOnClickListener(action))
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(
            PostsListFragmentViewModel::class.java
        )
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    fun reloadData() {
        binding.progressBar.setVisibility(View.VISIBLE)
        Model.instance().getAllPosts { stList ->
            viewModel.setData(stList)
            adapter!!.setData(viewModel.getData())
            binding.progressBar.setVisibility(View.GONE)
        }
    }
}
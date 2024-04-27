package com.example.cityaware


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cityaware.model.Post
import com.squareup.picasso.Picasso

import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy

class PostViewHolder(itemView: View, private val listener: PostRecyclerAdapter.OnItemClickListener?, private val data: List<Post>) :
    RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView = itemView.findViewById(R.id.postlistrow_name_tv)
    private val idTv: TextView = itemView.findViewById(R.id.postlistrow_label_tv)
    private val avatarImage: ImageView = itemView.findViewById(R.id.postlistrow_avatar_img)

    init {
        itemView.setOnClickListener {
            val pos = adapterPosition
            listener?.onItemClick(pos)
        }
    }

    fun bind(post: Post) {
        nameTv.text = post.title
        idTv.text = post.label
        if (post.imgUrl != "") {
            Picasso.get()
                .load(post.imgUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(avatarImage)
        } else {
            avatarImage.setImageResource(R.drawable.noimage)
        }
    }
}


class PostRecyclerAdapter(private var inflater: LayoutInflater, var data: List<Post>) :
    RecyclerView.Adapter<PostViewHolder>() {
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }

//    fun setData(data: List<Post>) {
//        this.data = data
//        notifyDataSetChanged()
//    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = inflater.inflate(R.layout.post_list_row, parent, false)
        return PostViewHolder(view, listener, data)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val st = data[position]
        holder.bind(st)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
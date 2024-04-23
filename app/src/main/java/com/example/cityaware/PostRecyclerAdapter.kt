package com.example.cityaware


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cityaware.model.Post
import com.squareup.picasso.Picasso
import com.example.cityaware.R


class PostViewHolder(itemView: View, listener: PostRecyclerAdapter.OnItemClickListener?, var data: List<Post>) :
    RecyclerView.ViewHolder(itemView) {
    var nameTv: TextView
    var idTv: TextView
    var avatarImage: ImageView


    init {
        nameTv = itemView.findViewById(R.id.postlistrow_name_tv)
        idTv = itemView.findViewById(R.id.postlistrow_label_tv)
        avatarImage = itemView.findViewById(R.id.postlistrow_avatar_img)
        itemView.setOnClickListener {
            val pos = getAdapterPosition()
            listener!!.onItemClick(pos)
        }
    }

    fun bind(post: Post, pos: Int) {
        nameTv.text = post.title
        idTv.text = post.id
        //cb.setChecked(post.cb);
        //cb.setTag(pos);
        if (post.imgUrl !== "") {
            Picasso.get().load(post.imgUrl).placeholder(R.drawable.avatar).into(avatarImage)
        } else {
            avatarImage.setImageResource(R.drawable.avatar)
        }
    }
}


class PostRecyclerAdapter(var inflater: LayoutInflater, var data: List<Post>) :
    RecyclerView.Adapter<PostViewHolder>() {
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }

    fun setData(data: List<Post>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = inflater.inflate(R.layout.post_list_row, parent, false)
        return PostViewHolder(view, listener, data)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val st = data[position]
        holder.bind(st, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
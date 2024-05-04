package com.example.cityaware


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cityaware.model.Post
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class PostViewHolder constructor(
    itemView: View,
    listener: PostRecyclerAdapter.OnItemClickListener?,
    var data: List<Post?>?
) : RecyclerView.ViewHolder(itemView) {
    var nameTv: TextView
    var idTv: TextView
    var avatarImage: ImageView

    init {
        nameTv = itemView.findViewById(R.id.postlistrow_name_tv)
        idTv = itemView.findViewById(R.id.postlistrow_label_tv)
        avatarImage = itemView.findViewById(R.id.postlistrow_avatar_img)
        itemView.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val pos: Int = getAdapterPosition()
                listener!!.onItemClick(pos)
            }
        })
    }

    fun bind(post: Post?, pos: Int) {
        nameTv.setText(post!!.title)
        idTv.setText(post.label)
        if (post.imgUrl !== "") {
            //Picasso.get().load(post.getImgUrl()).placeholder(R.drawable.avatar).into(avatarImage);
            Picasso.get()
                .load(post.imgUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(avatarImage)
            //.placeholder(R.drawable.avatar)
        } else {
            avatarImage.setImageResource(R.drawable.noimage)
        }
    }
}

class PostRecyclerAdapter constructor(
    private val inflater: LayoutInflater,
    data: List<Post?>? = null
) : RecyclerView.Adapter<PostViewHolder>() {

    var data: List<Post?>? = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = inflater.inflate(R.layout.post_list_row, parent, false)
        return PostViewHolder(view, listener, data)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = data?.get(position)
        holder.bind(post, position)
    }

    override fun getItemCount(): Int = data?.size ?: 0
}

package jx.lessons.firebasesmschatwithmvvm.presentation.listpostuser

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.addChip_home_post
import jx.lessons.firebasesmschatwithmvvm.databinding.ItemPostsBinding

class ListPostUserAdapter(var context: Context) :RecyclerView.Adapter<ListPostUserAdapter.MyViewHolder>() {
    var list = ArrayList<Post>()

    fun setData(list: ArrayList<Post>){
        this.list.clear()
        list.let { this.list.addAll(it) }
        notifyDataSetChanged()
    }

    inner class MyViewHolder(var binding: ItemPostsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Post){
            binding.commentText.text = data.comments.size.toString()
            binding.likeText.text = data.likeS.size.toString()
            binding.descriptionPost.text = data.postDescription
            binding.titlePost.text = data.postTitle
            data.tagsList.forEach {
                binding.tagChips.addChip_home_post(it)
            }
            Glide.with(context)
                .load(data.imageUri)
                .into(binding.imagePost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =  MyViewHolder(
        ItemPostsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int =list.size
}
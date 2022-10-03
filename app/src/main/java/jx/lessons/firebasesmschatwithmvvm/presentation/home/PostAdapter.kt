package jx.lessons.firebasesmschatwithmvvm.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.addChip_home_post
import jx.lessons.firebasesmschatwithmvvm.databinding.ItemPostsBinding
import java.util.logging.Handler

class PostAdapter(var context: Context, var listClickView: ListClickView) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    var list = ArrayList<Post>()

    fun setData(list: ArrayList<Post>){
        this.list.clear()
        list.let { this.list.addAll(it) }
        notifyDataSetChanged()
    }

    inner class MyViewHolder(var binding: ItemPostsBinding):RecyclerView.ViewHolder(binding.root)   {
        fun bind(data: Post){
            var i = 0
            binding.commentText.text = (data.comments.size-1).toString()
            binding.likeText.text = (data.likeS.size-1).toString()
            binding.descriptionPost.text = data.postDescription
            binding.titlePost.text = data.postTitle
            binding.imageLike.setOnClickListener {
                listClickView.clickedLike(data.randomKey)
            }
            binding.imageComment.setOnClickListener {
                listClickView.clickedComment(data.randomKey)
            }
            binding.imagePost.setOnClickListener {
                i++
                val handler = android.os.Handler()
                handler.postDelayed({
                    if (i==2){
                        listClickView.doubleClickImageView(data.randomKey)
                    }
                    i = 0
                },500)
            }
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
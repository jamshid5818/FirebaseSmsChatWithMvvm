package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.addChip_home_post
import jx.lessons.firebasesmschatwithmvvm.databinding.ItemPostsBinding
import kotlin.collections.ArrayList

class PostAdapter(var list: ArrayList<Post>,var context: Context, var listClickView: ListClickView) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemPostsBinding):RecyclerView.ViewHolder(binding.root)   {
        fun bind(data: Post){
            var i = 0
            binding.commentText.text = (data.comments.size-1).toString()
            binding.likeText.text = (data.likeS.size-2).toString()
            binding.descriptionPost.text = data.postDescription
            binding.titlePost.text = data.postTitle
            binding.imageLike.setOnClickListener {
                listClickView.clickedLike(data.randomKey)
            }
            binding.imageComment.setOnClickListener {
                listClickView.clickedComment(data.randomKey)
            }
            binding.imageLike.setOnLongClickListener {
                listClickView.longClickedLike(data.randomKey)
                return@setOnLongClickListener true
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
            binding.tagChips.removeView(binding.tagChips)
            data.tagsList.forEach {
                binding.tagChips.addChip_home_post(it)
            }
            Glide.with(context)
                .load(data.imageUri)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error)
                .into(binding.imagePost)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=MyViewHolder(
    ItemPostsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int =list.size

}
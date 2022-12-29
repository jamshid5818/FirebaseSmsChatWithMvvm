package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jx.lessons.firebaseSmsChatWithMvvm.R
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.addChip_home_post
import jx.lessons.firebaseSmsChatWithMvvm.databinding.ItemPostsBinding

class PostAdapter(var list: List<Post>,var context: Context, var listClickView: ListClickView) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    var rotationAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.rotation_360)

    inner class MyViewHolder(var binding: ItemPostsBinding):RecyclerView.ViewHolder(binding.root)   {
        fun bind(data: Post){
            var i = 0
            if (data.likeS != null) {
                binding.likeText.text= data.likeS!!.size.toString()
            } else {
                binding.likeText.text = "0"
            }
            if (data.comments != null) {
                binding.commentText.text= data.comments!!.size.toString()
            } else {
                binding.commentText.text = "0"
            }
            if (data.downloads != null) {
                binding.downloadText.text= data.downloads!!.size.toString()
            } else {
                binding.downloadText.text = "0"
            }

            binding.descriptionPost.text = data.postDescription
            binding.titlePost.text = data.postTitle
            binding.imageLike.setOnClickListener {
                listClickView.clickedLike(data.unixTime.toString())
                binding.imageLike.startAnimation(rotationAnim)
            }
            binding.imageComment.setOnClickListener {
                listClickView.clickedComment(data.unixTime)
                binding.imageComment.startAnimation(rotationAnim)
            }
            binding.imageLike.setOnLongClickListener {
                listClickView.longClickedLike(data.unixTime.toString())
                return@setOnLongClickListener true
            }
            binding.download.setOnClickListener {
                binding.download.stateListAnimator
                listClickView.downloadClicked(data.imageUri,data.unixTime.toString())
            }
            binding.imagePost.setOnClickListener {
                i++
                val handler = android.os.Handler()
                handler.postDelayed({
                    if (i==2){
                        listClickView.doubleClickImageView(data.unixTime.toString())
                        binding.imageLike.startAnimation(rotationAnim)
                    }
                    i = 0
                },500)
            }
            binding.tagChips.removeView(binding.tagChips)
            data.tagsList?.forEach {
                binding.tagChips.addChip_home_post(it)
            }
            try{
                Glide.with(context)
                    .load(data.imageUri)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error)
                    .into(binding.imagePost)
            }catch(e:Exception){
                Toast.makeText(context, "Ro'yxatdan o'ting yoki sahifani yangilang", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=MyViewHolder(
    ItemPostsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int =list.size

}
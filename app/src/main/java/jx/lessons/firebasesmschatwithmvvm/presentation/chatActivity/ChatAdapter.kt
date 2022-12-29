package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import jx.lessons.firebaseSmsChatWithMvvm.R
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Sms
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.SharedPref
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.gone

class ChatAdapter(var list:ArrayList<Sms>,var context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        private const val ITEM_RECEIVE = 1
        private const val ITEM_SENT = 2
    }

    private val shared by lazy {
        SharedPref(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_he_sms, parent, false)
            ReceiveVewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.item_my_sms, parent, false)
            SentViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = list[position]
        return if (currentMessage.emailAddress==shared.getEmail()){
            ITEM_SENT
        }else{
            ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (shared.getEmail()==list[position].emailAddress){
            holder as SentViewHolder
            if (list[position].smsText!=""){
                holder.textView.text = list[position].smsText
            }else{
                holder.textView.gone()
            }
            if (list[position].imageUri!=""){
                Glide.with(context)
                    .load(list[position].imageUri)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error)
                    .into(holder.imageView)
            }else{
                holder.imageView.gone()
            }
        }else{
            holder as ReceiveVewHolder
            if (list[position].gender=="Male"){
                holder.imagePerson.setImageResource(R.drawable.male_avatar)
            }else{
                holder.imagePerson.setImageResource(R.drawable.female_avatar)
            }
            if (list[position].smsText!=""){
                holder.textView.text = list[position].smsText
            }else{
                holder.textView.gone()
            }
            if (list[position].imageUri!=""){
                Glide.with(context)
                    .load(list[position].imageUri)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error)
                    .into(holder.imageView)
            }else{
                holder.imageView.gone()
            }
        }
    }

    override fun getItemCount(): Int =list.size

    inner class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.my_message)
        val imageView :ImageView = view.findViewById(R.id.imageMy)
    }

    inner class ReceiveVewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.he_message)
        val imagePerson:CircleImageView=view.findViewById(R.id.imagePerson)
        val imageView:ImageView = view.findViewById(R.id.imageHe)
    }
}
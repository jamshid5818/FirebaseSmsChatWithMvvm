package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import jx.lessons.firebaseSmsChatWithMvvm.R;
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post;

public class PersonAdapter extends BaseAdapter {
    private final List<Post> postList;
    private final Context context;

    public PersonAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int i) {
        return postList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_person_posts,viewGroup,false);
        ImageView imageView = view.findViewById(R.id.postImage_person);
        TextView textView = view.findViewById(R.id.likeCount);
        Post post = (Post) getItem(index);
        Glide.with(context).load(post.getImageUri()).into(imageView);
        if (post.getLikeS()!=null){
            textView.setText(String.valueOf(post.getLikeS().size()));
        }else {
            textView.setText("0");
        }

        return view;
    }
}

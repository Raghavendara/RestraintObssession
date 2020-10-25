package com.raghav.restraintobsession.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raghav.restraintobsession.R;
import com.raghav.restraintobsession.models.Post;
import com.raghav.restraintobsession.visitor_view.PostDetailActivity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData ;


    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);


        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                postDetailActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                postDetailActivity.putExtra("title",mData.get(position).getTitle());
                postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
                postDetailActivity.putExtra("description",mData.get(position).getDescription());
                postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                // will fix this later i forgot to add user name to post object
                //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                long timestamp  = (long) mData.get(position).getTimeStamp();
                postDetailActivity.putExtra("postDate",timestamp) ;
                mContext.startActivity(postDetailActivity);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imgPost;
        ImageView imgPostProfile;
        ConstraintLayout constraintLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);
            constraintLayout = itemView.findViewById(R.id.constraint);



        }


    }
}

package com.raghav.restraintobsession.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raghav.restraintobsession.R;
import com.raghav.restraintobsession.models.User;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.UserViewHolder> {

    private List<User> users;

    public MembersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserViewHolder userViewHolder = new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user,parent,false));
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView textFirstChar, textUsername, textEmail;
        ImageView imageAudioMeeting, imageVideoMeeting;



        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textFirstChar = itemView.findViewById(R.id.textFirstChar);
            textEmail = itemView.findViewById(R.id.textEmail);
            textUsername = itemView.findViewById(R.id.textUsername);
            imageVideoMeeting = itemView.findViewById(R.id.imageVideoMeeting);
            imageAudioMeeting = itemView.findViewById(R.id.imageAudioMeeting);
        }

        void setUserData(User user){
            textFirstChar.setText(user.firstName.substring(0,1));
            textUsername.setText(String.format("%s %s", user.firstName, user.lastName));
            textEmail.setText(user.email);
        }


    }
}

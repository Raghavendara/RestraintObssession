package com.raghav.restraintobsession.visitor_view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.raghav.restraintobsession.R;
import com.raghav.restraintobsession.adapters.MembersAdapter;
import com.raghav.restraintobsession.listeners.UsersListener;
import com.raghav.restraintobsession.models.User;
import com.raghav.restraintobsession.utilities.Constants;
import com.raghav.restraintobsession.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ChatMember extends AppCompatActivity implements UsersListener {
    PreferenceManager preferenceManager;
    private List<User> users;
    private MembersAdapter membersAdapter;
    private TextView textErrorMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageConference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_member);

        preferenceManager = new PreferenceManager(getApplicationContext());

        imageConference = findViewById(R.id.imageConference);

        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        textErrorMessage = findViewById(R.id.textErrorMessage);



        users = new ArrayList<>();
        membersAdapter = new MembersAdapter(users, this);
        usersRecyclerView.setAdapter(membersAdapter);

        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getUsers);
        getUsers();

    }

    private void getUsers(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    swipeRefreshLayout.setRefreshing(false);
                    String myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult()!=null){
                        users.clear();
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            if(myUserId.equals(documentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);
                        }
                        if(users.size() > 0){
                            membersAdapter.notifyDataSetChanged();
                        }else{
                            textErrorMessage.setText(String.format("%s","No users available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                    }else{

                        textErrorMessage.setText(String.format("%s", "No Users available"));
                        textErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void initiateVideoMeeting(User user) {
        if(user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(this, user.firstName+ " "+user.lastName+" is not available for meeting", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getApplicationContext(),OutgoingInvitationActivity.class);
            intent.putExtra("user",user);
            intent.putExtra("type","video");
            startActivity(intent);
        }
    }

    @Override
    public void initiateAudioMeeting(User user) {
        if(user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(this, user.firstName+ " "+user.lastName+" is not available for meeting", Toast.LENGTH_SHORT).show();
        }else
        {
            Intent intent = new Intent(getApplicationContext(),OutgoingInvitationActivity.class);
            intent.putExtra("user",user);
            intent.putExtra("type", "audio");
            startActivity(intent);
        }

    }

    @Override
    public void onMultipleUsersAction(Boolean isMultipleUsersSelected) {
        if(isMultipleUsersSelected){
            imageConference.setVisibility(View.VISIBLE);
            imageConference.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
                intent.putExtra("selectedUsers", new Gson().toJson(membersAdapter.getSelectedUsers()));
                intent.putExtra("type","video");
                intent.putExtra("isMultiple",true);
                startActivity(intent);
            });


        }else{
            imageConference.setVisibility(View.GONE);
        }

    }
}
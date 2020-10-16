package com.raghav.restraintobsession.visitor_view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.raghav.restraintobsession.R;
import com.raghav.restraintobsession.adapters.MembersAdapter;
import com.raghav.restraintobsession.models.User;
import com.raghav.restraintobsession.utilities.Constants;
import com.raghav.restraintobsession.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ChatMember extends AppCompatActivity {
    PreferenceManager preferenceManager;
    private List<User> users;
    private MembersAdapter membersAdapter;
    private TextView textErrorMessage;
    private ProgressBar usersProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_member);
        preferenceManager = new PreferenceManager(getApplicationContext());
        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        textErrorMessage = findViewById(R.id.textErrorMessage);
        usersProgressBar = findViewById(R.id.usersProgressBar);


        users = new ArrayList<>();
        membersAdapter = new MembersAdapter(users);
        usersRecyclerView.setAdapter(membersAdapter);

        getUsers();

    }

    private void getUsers(){
        usersProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        usersProgressBar.setVisibility(View.GONE);
                        String myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if (task.isSuccessful() && task.getResult()!=null){
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
                    }
                });
    }
}
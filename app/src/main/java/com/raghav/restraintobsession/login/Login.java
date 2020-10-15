package com.raghav.restraintobsession.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raghav.restraintobsession.R;
import com.raghav.restraintobsession.register.Register;
import com.raghav.restraintobsession.utilities.Constants;
import com.raghav.restraintobsession.utilities.PreferenceManager;
import com.raghav.restraintobsession.visitor_view.Dashboard;



public class Login extends AppCompatActivity {
    private EditText inputEmail,inputPassword;
    private Button buttonSignIn;
    private ProgressBar signInProgressBar;
    private PreferenceManager preferenceManager;
    TextView mregiter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }

        inputEmail = findViewById(R.id.Email);
        inputPassword = findViewById(R.id.Password);
        buttonSignIn = findViewById(R.id.signinBtn);
        signInProgressBar = findViewById(R.id.signinprogress);
        mregiter = findViewById(R.id.register);

        mregiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);

            }
        });

        buttonSignIn.setOnClickListener(view -> {

            if(inputEmail.getText().toString().trim().isEmpty()){
                Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()){
                Toast.makeText(Login.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            }else if (inputPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
            }else {
                SignUp();
            }
        });






    }

    private void SignUp() {
        buttonSignIn.setVisibility(View.INVISIBLE);
        signInProgressBar.setVisibility(View.VISIBLE);


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0 ){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_LAST_NAME,documentSnapshot.getString(Constants.KEY_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_EMAIL,documentSnapshot.getString(Constants.KEY_EMAIL));
                        preferenceManager.putString(Constants.KEY_MOBILE,documentSnapshot.getString(Constants.KEY_MOBILE));
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        signInProgressBar.setVisibility(View.INVISIBLE);
                        buttonSignIn.setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "Unable to sign in", Toast.LENGTH_SHORT).show();
                    }
                    });


    }
}

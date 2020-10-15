package com.raghav.restraintobsession.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.raghav.restraintobsession.R;
import com.raghav.restraintobsession.login.Login;
import com.raghav.restraintobsession.utilities.Constants;
import com.raghav.restraintobsession.utilities.PreferenceManager;
import com.raghav.restraintobsession.visitor_view.Dashboard;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    private static final String TAG = "Number";
    private EditText inputFirstName, inputLastName, inputEmail, inputMobileNumber, inputPassword, inputConfirmPassword;
    private Button buttonSignUp;
    private CountryCodePicker cpp;
    private ProgressBar progressSignUp;
    private PreferenceManager preferenceManager;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferenceManager = new PreferenceManager(getApplicationContext());
        fAuth = FirebaseAuth.getInstance();

        inputFirstName = findViewById(R.id.FirstName);
        inputLastName = findViewById(R.id.Lastname);
        inputEmail = findViewById(R.id.Email);
        inputMobileNumber = findViewById(R.id.Mobilenumber);
        inputPassword = findViewById(R.id.Password);
        inputConfirmPassword = findViewById(R.id.Confirmpassword);
        cpp = findViewById(R.id.ccp);
        buttonSignUp = findViewById(R.id.signupBtn);
        progressSignUp = findViewById(R.id.signupprogress);


        buttonSignUp.setOnClickListener(view -> {
            if(inputFirstName.getText().toString().trim().isEmpty()){
                Toast.makeText(Register.this, "Enter first name", Toast.LENGTH_SHORT).show();
            }else if (inputLastName.getText().toString().trim().isEmpty()){
                Toast.makeText(Register.this, "Enter last name", Toast.LENGTH_SHORT).show();
            }else if(inputEmail.getText().toString().trim().isEmpty()){
                Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()){
                Toast.makeText(Register.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            }else if (inputMobileNumber.getText().toString().trim().isEmpty()){
                Toast.makeText(Register.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            }else if (!Patterns.PHONE.matcher(inputMobileNumber.getText().toString()).matches()){
                Toast.makeText(Register.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();

            }else if (inputPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
            }else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(Register.this, "Confirm your password", Toast.LENGTH_SHORT).show();
            }else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                Toast.makeText(Register.this, "Password & confirm password must be same", Toast.LENGTH_SHORT).show();
            }else {
                SignUp();
            }
        });



    }

    private void SignUp() {
        buttonSignUp.setVisibility(View.INVISIBLE);
        progressSignUp.setVisibility(View.VISIBLE);



        fAuth.createUserWithEmailAndPassword(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                HashMap<String, Object> user = new HashMap<>();
                user.put(Constants.KEY_FIRST_NAME,inputFirstName.getText().toString());
                user.put(Constants.KEY_LAST_NAME,inputLastName.getText().toString());
                user.put(Constants.KEY_EMAIL,inputEmail.getText().toString());
                user.put(Constants.KEY_MOBILE,inputMobileNumber.getText().toString());

                database.collection(Constants.KEY_COLLECTION_USERS)
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                            preferenceManager.putString(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString());
                            preferenceManager.putString(Constants.KEY_LAST_NAME,inputLastName.getText().toString());
                            preferenceManager.putString(Constants.KEY_EMAIL,inputEmail.getText().toString());
                            preferenceManager.putString(Constants.KEY_MOBILE,inputMobileNumber.getText().toString());
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity (intent);
                        })
                        .addOnFailureListener(e -> {
                            progressSignUp.setVisibility(View.INVISIBLE);
                            buttonSignUp.setVisibility(View.VISIBLE);
                            Toast.makeText(Register.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Failed to create Account", Toast.LENGTH_SHORT).show();
            }
        });









    }

    public void SignIn(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}

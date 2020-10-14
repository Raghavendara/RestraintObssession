package com.raghav.restraintobsession;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raghav.restraintobsession.login.Login;
import com.raghav.restraintobsession.visitor_view.Dashboard;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 1500;

    //Variables
    FirebaseUser firebaseUser;
    Animation topAnim,bottomAnim;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_FULLSCREEN,FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.animation_top);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.animation_bottom);

        imageView = findViewById(R.id.app_logo);
        textView = findViewById(R.id.slogan);


        imageView.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            new Handler().postDelayed(new  Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                    finish();
                }
            }, SPLASH_SCREEN);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            }, SPLASH_SCREEN);
        }

    }
}
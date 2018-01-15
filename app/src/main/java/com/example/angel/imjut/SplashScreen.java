package com.example.angel.imjut;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    // Required
                    final Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            intent = new Intent(SplashScreen.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    };
                    final Handler h = new Handler();
                    h.removeCallbacks(runnable);
                    h.postDelayed(runnable, 50);

                } else {
                    final Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            intent = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    };
                    final Handler h = new Handler();
                    h.removeCallbacks(runnable);
                    h.postDelayed(runnable, 50);

                }
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}

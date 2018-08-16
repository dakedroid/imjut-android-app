package com.imjut.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.imjut.android.Modelos.User;
import com.imjut.android.SubirContenido.PanelSubirContenido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rm.rmswitch.RMSwitch;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };

        //DrawerLayout
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.mipmap.ic_menu_black_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Menu
        final NavigationView navigation = findViewById(R.id.navigation);
        assert navigation != null;
        navigation.setNavigationItemSelectedListener(mNavigationItemSelectedListener);
        navigation.inflateHeaderView(R.layout.design_navigation);

        final String mCurrentEmailUser = mAuth.getCurrentUser().getEmail().replace(".",",");
        Menu menuView = navigation.getMenu();
        MenuItem menuItem = menuView.getItem(0);
        final RMSwitch mToggleButton = menuItem.getActionView().findViewById(R.id.mRMSwitch);

        mUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mCurrentEmailUser);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mCurrentUser = dataSnapshot.getValue(User.class);
                assert mCurrentUser != null;
                boolean permisos = mCurrentUser.isPermisos_admin();
                Log.d("Permisos", String.valueOf(permisos));
                if(permisos){
                    navigation.getMenu().findItem(R.id.navigation_item_subir).setVisible(true);
                }else{
                    navigation.getMenu().findItem(R.id.navigation_item_subir).setVisible(false);
                }

                boolean notiActivated = mCurrentUser.isNoti_activadas();
                if(notiActivated){
                    mToggleButton.setChecked(true);
                    FirebaseMessaging.getInstance().subscribeToTopic("imjut");
                }else{
                    mToggleButton.setChecked(false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("imjut");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mToggleButton.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                if(isChecked){
                    mUserRef.child("noti_activadas").setValue(true);
                }else{
                    mUserRef.child("noti_activadas").setValue(false);
                }
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    private NavigationView.OnNavigationItemSelectedListener mNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if (handleNavigationItemSelected(item)) {
                mDrawerLayout.closeDrawers();
                return true;
            }
            return false;
        }
    };

    public boolean handleNavigationItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.navigation_item_3:
                startActivity(new Intent(this, ContactoActivity.class));
                return true;
            case R.id.navigation_item_4:
                startActivity(new Intent(this, AcercaDeActivity.class));
                return true;
            case R.id.navigation_item_subir:
                startActivity(new Intent(this, PanelSubirContenido.class));
                return true;
            case R.id.navigation_item_5:
                mAuth.signOut();
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                return true;
            default: return false;
        }
    }
}

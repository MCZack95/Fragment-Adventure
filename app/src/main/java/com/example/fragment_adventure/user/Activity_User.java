package com.example.fragment_adventure.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_User extends AppCompatActivity {

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Setting up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // Setting up the Navigation Drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        ImageButton imageButton = headerLayout.findViewById(R.id.nav_back_arrow);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // set item as selected to persist highlight
                item.setChecked(true);

                // Clears all fragments from the stack
                for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                    getSupportFragmentManager().popBackStack();
                }
                Intent intent = getIntent();
                final String username_message = intent.getStringExtra(Activity_Main.USER_NAME);
                final String password_message = intent.getStringExtra(Activity_Main.PASSWORD);
                Bundle bundle = new Bundle();
                bundle.putString(Activity_Main.USER_NAME,username_message);
                bundle.putString(Activity_Main.PASSWORD,password_message);

                if(item.toString().matches("Current Orders")){
                    // Go to User Current Orders. Item is recorded right after payment.
                    Fragment_User_Orders fragment_user_orders = new Fragment_User_Orders();
                    fragment_user_orders.setArguments(bundle);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_fragment_container,fragment_user_orders);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(item.toString().matches("History")){
                    // Go to User Transaction History. Item is recorded only if Order is Successfully completed by Owner of that stall
                    Fragment_User_History fragment_user_history = new Fragment_User_History();
                    fragment_user_history.setArguments(bundle);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_fragment_container,fragment_user_history);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if(item.toString().matches("Settings")){
                    // Go to User Settings
                    Fragment_User_Settings fragment_user_settings = new Fragment_User_Settings();
                    fragment_user_settings.setArguments(bundle);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_fragment_container,fragment_user_settings);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if(item.toString().matches("Log out")){
                    finish();
                }
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        // Inflate the Main User Fragment
        if(findViewById(R.id.user_fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }
            Intent intent = getIntent();
            final String username_message = intent.getStringExtra(Activity_Main.USER_NAME);
            final String password_message = intent.getStringExtra(Activity_Main.PASSWORD);

            Fragment_User_Main fragment_user_main = new Fragment_User_Main();
            Bundle bundle = new Bundle();
            bundle.putString(Activity_Main.USER_NAME,username_message);
            bundle.putString(Activity_Main.PASSWORD,password_message);
            fragment_user_main.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.user_fragment_container,fragment_user_main).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.e("Fragment Count",String.valueOf(fragmentManager.getBackStackEntryCount()));
        if(fragmentManager.getBackStackEntryCount() == 0){
            super.onBackPressed();
        }else{
            fragmentManager.popBackStack();
        }
    }
}

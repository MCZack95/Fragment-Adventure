package com.example.fragment_adventure.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fragment_adventure.R;

public class Activity_Main extends FragmentActivity {

    public static final String STALL_NAME = "MY_STALL";
    public static final String USER_NAME = "MY_USERNAME";
    public static final String PASSWORD = "MY_PASSWORD";
    public static final String FOOD_NAME = "MY_FOOD";

    Button loginButton;
    TextView createAccountTextView;
    EditText editUserName,editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUserName = findViewById(R.id.usernameText);
        editPassword = findViewById(R.id.passwordText);

        loginButton = findViewById(R.id.login);
        createAccountTextView = findViewById(R.id.createAccountText);

        if(findViewById(R.id.login_fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }
            Fragment_Main fragment_main = new Fragment_Main();
            getSupportFragmentManager().beginTransaction().add(R.id.login_fragment_container,fragment_main).commit();
        }
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

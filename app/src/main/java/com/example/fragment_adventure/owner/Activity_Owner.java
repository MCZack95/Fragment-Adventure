package com.example.fragment_adventure.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;


/**
 * Created by zNotAgain on 3/3/2018.
 */

public class Activity_Owner extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        // Inflate Owner Main Fragment
        if(findViewById(R.id.owner_fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }

            Intent intent = getIntent();
            final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
            Fragment_Owner_Main fragment_owner_main = new Fragment_Owner_Main();
            Bundle bundle = new Bundle();
            bundle.putString(Activity_Main.STALL_NAME,temp);
            fragment_owner_main.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.owner_fragment_container,fragment_owner_main)
                    .commit();
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

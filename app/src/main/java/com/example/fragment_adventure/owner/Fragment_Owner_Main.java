package com.example.fragment_adventure.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 15/3/2018.
 */

public class Fragment_Owner_Main extends Fragment {

    Button logOutButton,manageMenuButton,currentOrdersButton,transHistButton,ownerSettingsButton;
    TextView stallNameTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_owner_main,container,false);

        stallNameTextView = relativeLayout.findViewById(R.id.stall_Name);

        logOutButton = relativeLayout.findViewById(R.id.logout);
        manageMenuButton = relativeLayout.findViewById(R.id.manage_menu);
        currentOrdersButton = relativeLayout.findViewById(R.id.current_orders);
        transHistButton = relativeLayout.findViewById(R.id.transaction_history);
        ownerSettingsButton = relativeLayout.findViewById(R.id.settings);

        Intent intent = getActivity().getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(temp != null)
            stallNameTextView.setText(temp);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<getFragmentManager().getBackStackEntryCount();i++){
                    getFragmentManager().popBackStack();
                }
                getActivity().finish();
            }
        });

        manageMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Owner_Manage_Menu fragment_owner_manage_menu = new Fragment_Owner_Manage_Menu();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.owner_fragment_container,fragment_owner_manage_menu);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        currentOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Owner_Orders fragment_owner_orders = new Fragment_Owner_Orders();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.owner_fragment_container,fragment_owner_orders);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        transHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Owner_History fragment_owner_history = new Fragment_Owner_History();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.owner_fragment_container,fragment_owner_history);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ownerSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Owner_Settings fragment_owner_settings = new Fragment_Owner_Settings();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.owner_fragment_container,fragment_owner_settings);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return relativeLayout;
    }
}

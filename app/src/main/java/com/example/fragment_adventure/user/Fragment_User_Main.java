package com.example.fragment_adventure.user;

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

import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 11/3/2018.
 */

public class Fragment_User_Main extends Fragment {

    Button orderButton = null;
    Button logOutButton = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_main,container,false);

        orderButton = relativeLayout.findViewById(R.id.order);
        logOutButton = relativeLayout.findViewById(R.id.logout);

        Bundle incomingBundle = this.getArguments();
        if(incomingBundle != null){
            final String username_message = incomingBundle.getString(Activity_Main.USER_NAME);
            final String password_message = incomingBundle.getString(Activity_Main.PASSWORD);

            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment_User_Stall fragment_user_stall = new Fragment_User_Stall();
                    Bundle outgoingBundle = new Bundle();
                    outgoingBundle.putString(Activity_Main.USER_NAME,username_message);
                    outgoingBundle.putString(Activity_Main.PASSWORD,password_message);
                    fragment_user_stall.setArguments(outgoingBundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_fragment_container,fragment_user_stall);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return relativeLayout;
    }
}

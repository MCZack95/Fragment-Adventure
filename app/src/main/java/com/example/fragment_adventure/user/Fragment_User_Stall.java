package com.example.fragment_adventure.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;


/**
 * Created by zNotAgain on 11/3/2018.
 */

public class Fragment_User_Stall extends ListFragment implements AdapterView.OnItemClickListener {

    DatabaseHelper myDb;
    TextView mStallTextView;
    Button exitButton,nextButton;
    Adapter_User_Stall mAdapterStall;
    String[] strings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_stall,container,false);

        mStallTextView = relativeLayout.findViewById(R.id.stall_status);
        exitButton = relativeLayout.findViewById(R.id.exit);
        nextButton = relativeLayout.findViewById(R.id.next);

        Bundle incomingBundle = this.getArguments();
        if(incomingBundle != null){
            final String stallMessage = incomingBundle.getString(Activity_Main.STALL_NAME);
            final String usernameMessage = incomingBundle.getString(Activity_Main.USER_NAME);

            if(stallMessage != null){
                mStallTextView.setText(stallMessage);
            }

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(v,usernameMessage);
                }
            });

            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
        }

        return relativeLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myDb = new DatabaseHelper(getContext());
        strings = myDb.getArrayOfStall();
        mAdapterStall = new Adapter_User_Stall(getContext(),R.layout.row_stall,strings);
        setListAdapter(mAdapterStall);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(),strings[position], Toast.LENGTH_SHORT).show();
        String temp = "Stall: " + strings[position];
        mStallTextView.setText(temp);
    }

    public void sendMessage(View view,String username) {
        TextView textView = mStallTextView.findViewById(R.id.stall_status);
        String stallMessage = textView.getText().toString();
        if (stallMessage.matches("Stall: ")) {
            Toast.makeText(view.getContext(), "Stall not chosen", Toast.LENGTH_LONG).show();
            return;
        }

        Fragment_User_Food fragment_user_food = new Fragment_User_Food();
        Bundle outgoingBundle = new Bundle();
        outgoingBundle.putString(Activity_Main.STALL_NAME, stallMessage);
        outgoingBundle.putString(Activity_Main.USER_NAME, username);
        fragment_user_food.setArguments(outgoingBundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_fragment_container,fragment_user_food);
        fragmentTransaction.addToBackStack(Activity_Main.FOOD_NAME);
        fragmentTransaction.commit();
    }
}

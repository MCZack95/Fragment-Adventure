package com.example.fragment_adventure.user;

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
 * Created by zNotAgain on 14/3/2018.
 */

public class Fragment_User_Food extends ListFragment implements AdapterView.OnItemClickListener {

    // Initialise
    DatabaseHelper myDb;
    Adapter_User_Food mAdapterFood;
    TextView mStallTextView,mFoodTextView;
    Button nextButton,backButton,exitButton;
    String[] strings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_food,container,false);

        // Connect to respective items in layout
        mStallTextView = relativeLayout.findViewById(R.id.stall_status);
        mFoodTextView = relativeLayout.findViewById(R.id.food_status);
        nextButton = relativeLayout.findViewById(R.id.next);
        backButton = relativeLayout.findViewById(R.id.back);
        exitButton = relativeLayout.findViewById(R.id.exit);

        Bundle incomingBundle = this.getArguments();
        if(incomingBundle != null){
            String stallMessage = incomingBundle.getString(Activity_Main.STALL_NAME);
            String foodMessage = incomingBundle.getString(Activity_Main.FOOD_NAME);
            final String usernameMessage = incomingBundle.getString(Activity_Main.USER_NAME);

            if(stallMessage != null){
                mStallTextView.setText(stallMessage);
                if(foodMessage != null){
                    mFoodTextView.setText(foodMessage);
                }
            }

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { sendMessage(v,usernameMessage); }});

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {getFragmentManager().popBackStack();
                }
            });

            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                    getFragmentManager().popBackStack();
                }
            });
        }
        return relativeLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myDb = new DatabaseHelper(getContext());
        String stallMessage = mStallTextView.getText().toString();
        strings = myDb.getStallMenu(stallMessage.substring(7));
        mAdapterFood = new Adapter_User_Food(getContext(),R.layout.row_food,strings);
        setListAdapter(mAdapterFood);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("PLACE",strings[position]);
        String temp = "Food: " + strings[position];
        mFoodTextView.setText(temp);
    }

    public void sendMessage(View view,String username) {

        TextView stallNameTextView = this.mStallTextView.findViewById(R.id.stall_status);
        TextView foodTextView = this.mFoodTextView.findViewById(R.id.food_status);

        String stallNameMessage = stallNameTextView.getText().toString();
        String foodMessage = foodTextView.getText().toString();

        if(foodMessage.matches("Food: ")){
            Toast.makeText(view.getContext(),"Food not chosen",Toast.LENGTH_LONG).show();
            return;
        }

        Fragment_User_Payment fragment_user_payment = new Fragment_User_Payment();
        Bundle outgoingBundle = new Bundle();
        outgoingBundle.putString(Activity_Main.STALL_NAME,stallNameMessage);
        outgoingBundle.putString(Activity_Main.FOOD_NAME,foodMessage);
        outgoingBundle.putString(Activity_Main.USER_NAME,username);
        fragment_user_payment.setArguments(outgoingBundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_fragment_container,fragment_user_payment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
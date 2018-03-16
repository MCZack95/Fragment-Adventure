package com.example.fragment_adventure.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 15/3/2018.
 */

public class Fragment_User_Payment extends Fragment {

    DatabaseHelper myDb;
    TextView mStallTextView,mFoodTextView;
    Button orderButton,cancelButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_payment,container,false);
        myDb = new DatabaseHelper(getContext());

        mStallTextView = relativeLayout.findViewById(R.id.stall_status);
        mFoodTextView = relativeLayout.findViewById(R.id.food_status);
        orderButton = relativeLayout.findViewById(R.id.paymentButton);
        cancelButton = relativeLayout.findViewById(R.id.cancelPayment);

        Bundle incomingBundle = this.getArguments();
        final String stallMessage = incomingBundle.getString(Activity_Main.STALL_NAME);
        final String foodMessage = incomingBundle.getString(Activity_Main.FOOD_NAME);

        // Capture the layout's TextView and set the string as its text
        mStallTextView.setText(stallMessage);
        mFoodTextView.setText(foodMessage);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getFragmentManager().popBackStack();
            }
        });

        return relativeLayout;
    }

    public void makePayment(){
        TextView stallNameTextView = this.mStallTextView.findViewById(R.id.stall_status);
        TextView foodTextView = this.mFoodTextView.findViewById(R.id.food_status);

        String stallNameMessage = stallNameTextView.getText().toString();
        String foodMessage = foodTextView.getText().toString();

        Bundle incomingBundle = this.getArguments();
        final String usernameMessage = incomingBundle.getString(Activity_Main.USER_NAME);

        String newFoodMessage = foodNameConverter(foodMessage,stallNameMessage,usernameMessage);
        if(myDb.addOrderArrayData(newFoodMessage,usernameMessage,stallNameMessage.substring(7))){
            Toast.makeText(getContext(),"PAYMENT SUCCESSFUL",Toast.LENGTH_LONG).show();

            // Clears all fragments from stack
            for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                getFragmentManager().popBackStack();
            }

            Intent intent = getActivity().getIntent();
            final String username_message = intent.getStringExtra(Activity_Main.USER_NAME);
            final String password_message = intent.getStringExtra(Activity_Main.PASSWORD);

            // Inflates User Main Fragment
            Fragment_User_Main fragment_user_main = new Fragment_User_Main();
            Bundle bundle = new Bundle();
            bundle.putString(Activity_Main.USER_NAME,username_message);
            bundle.putString(Activity_Main.PASSWORD,password_message);
            fragment_user_main.setArguments(bundle);

            getFragmentManager().beginTransaction().add(R.id.user_fragment_container,fragment_user_main).commit();
        }
        else
            Toast.makeText(getContext(),"PAYMENT NOT SUCCESSFUL",Toast.LENGTH_LONG).show();
    }

    public String foodNameConverter(String old_foodName, String stall_name, String user_name){
        // Initialise
        int ID = 0;
        int Max_ID = 1;
        String[] strings_orders = myDb.getArrayOfOrders(stall_name.substring(7));
        String[] strings_owner_history = myDb.getArrayOfHistory(stall_name.substring(7));
        String[] strings_user_history = myDb.getUserArrayOfHistory(user_name);

        // Initial Check, increment ID to 1
        for(String element : strings_orders)
            if(old_foodName.substring(6).matches(element))
                ID++;
        for(String element : strings_owner_history)
            if(old_foodName.substring(6).matches(element))
                ID++;
        for(String element : strings_user_history)
            if(old_foodName.substring(6).matches(element))
                ID++;

        // Check if there are duplicates in Order List
        for(String element : strings_orders)
            for(int j=0;j<element.length();j++)
                if(element.charAt(j) == '-')
                    if(old_foodName.substring(6).matches(element.substring(0,j)))
                        if((ID = Integer.valueOf(element.substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        // Check if there are duplicates in Owner History List
        for(String element : strings_owner_history)
            for(int j=0;j<element.length();j++)
                if(element.charAt(j) == '-')
                    if(old_foodName.substring(6).matches(element.substring(0,j)))
                        if((ID = Integer.valueOf(element.substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        // Check if there are duplicates in User History List
        for(String element : strings_user_history)
            for (int j = 0; j < element.length(); j++)
                if (element.charAt(j) == '-')
                    if (old_foodName.substring(6).matches(element.substring(0, j)))
                        if ((ID = Integer.valueOf(element.substring(j + 1)) + 1) > Max_ID)
                            Max_ID = ID;

        if(ID != 0) { // If item already exists in Order/History List
            return  old_foodName.substring(6) + "-" + String.valueOf(Max_ID);
        }else{ // If item is not found in Order/History List
            return old_foodName.substring(6);
        }
    }
}

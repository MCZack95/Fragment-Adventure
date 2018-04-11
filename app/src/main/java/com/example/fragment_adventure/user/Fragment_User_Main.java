package com.example.fragment_adventure.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by zNotAgain on 11/3/2018.
 */

public class Fragment_User_Main extends Fragment {

    Button orderButton = null;
    Button logOutButton = null;
    Button locationButton = null;
    TextView postalCode;

    private LatLng defaultLatLng = new LatLng(1.344233,103.680142);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_main,container,false);

        orderButton = relativeLayout.findViewById(R.id.order);
        logOutButton = relativeLayout.findViewById(R.id.logout);
        locationButton = relativeLayout.findViewById(R.id.locationButton);
        postalCode = relativeLayout.findViewById(R.id.postal_code);

        Bundle incomingBundle = this.getArguments();
        if(incomingBundle != null){
            final String username_message = incomingBundle.getString(Activity_Main.USER_NAME);
            final String password_message = incomingBundle.getString(Activity_Main.PASSWORD);

            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(postalCode.getText().toString().isEmpty()){
                        Toast.makeText(getContext(),"Set Current Location First",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Fragment_User_Stall fragment_user_stall = new Fragment_User_Stall();
                    Bundle outgoingBundle = new Bundle();
                    outgoingBundle.putString(Activity_Main.USER_NAME,username_message);
                    outgoingBundle.putString(Activity_Main.PASSWORD,password_message);
                    outgoingBundle.putString("Latitude",String.valueOf(defaultLatLng.latitude));
                    outgoingBundle.putString("Longitude",String.valueOf(defaultLatLng.longitude));
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

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentLocation();
            }
        });
        return relativeLayout;
    }

    public void setCurrentLocation(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set Current Location:");
        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        mBuilder.setView(input);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                List<Address> addressList = null;

                if(!m_Text.isEmpty()){
                    Geocoder geocoder = new Geocoder(getContext());
                    try{
                        addressList = geocoder.getFromLocationName(m_Text,1);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    if(addressList != null){
                        try{
                            Address address = addressList.get(0);
                            defaultLatLng = new LatLng(address.getLatitude(),address.getLongitude());
                            postalCode.setText(address.getPostalCode());
                            Log.e("Latitude",String.valueOf(address.getLatitude()));
                            Log.e("Longitude",String.valueOf(address.getLongitude()));
                        } catch(IndexOutOfBoundsException e){
                            Toast.makeText(getContext(),"Location not found",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(),"Location not found",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getFragmentManager().popBackStack();
            }
        });

        mBuilder.show();
    }
}

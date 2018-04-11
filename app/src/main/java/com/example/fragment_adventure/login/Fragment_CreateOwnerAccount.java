package com.example.fragment_adventure.login;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by zNotAgain on 11/3/2018.
 */

public class Fragment_CreateOwnerAccount extends android.support.v4.app.Fragment {

    DatabaseHelper myDb;
    EditText username, postalCode, stallName, password, confirmPassword;
    Button registerButton;
    TextView alreadyMember;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_createowneraccount, container, false);
        myDb = new DatabaseHelper(relativeLayout.getContext());

        username = relativeLayout.findViewById(R.id.register_usernameText);
        postalCode = relativeLayout.findViewById(R.id.register_postalCode);
        stallName = relativeLayout.findViewById(R.id.register_stallName);
        password = relativeLayout.findViewById(R.id.register_passwordText);
        confirmPassword = relativeLayout.findViewById(R.id.register_confirmPasswordText);
        alreadyMember = relativeLayout.findViewById(R.id.already_member);
        registerButton = relativeLayout.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure username & postal_code & stall_name & password fields are not empty
                if (isOwnerRegisterAcceptable(username.getText().toString(),postalCode.getText().toString(), stallName.getText().toString(), password.getText().toString(), confirmPassword.getText().toString())) {
                    // check if stall name already exists
                    Cursor stall_name_res = myDb.isStallNameAcceptable(stallName.getText().toString());
                    if (stall_name_res.getCount() == 1) {
                        stallName.setError("Stall Name Already Exists");
                        return;
                    }
                    // check if postal code is valid
                    if (!isPostalCodeAcceptable(postalCode.getText().toString())){
                        postalCode.setError("Postal Code Not Valid");
                        return;
                    }
                    // check if password fields match
                    if (password.getText().toString().matches(confirmPassword.getText().toString())) {
                        Cursor user_res = myDb.checkUserLoginData(username.getText().toString(), password.getText().toString());
                        Cursor owner_res = myDb.checkOwnerLoginData(username.getText().toString(), password.getText().toString());

                        // make sure account doesn't exist in database
                        if (user_res.getCount() == 1 || owner_res.getCount() == 1) {
                            Toast.makeText(v.getContext(), "Account Already Exists", Toast.LENGTH_LONG).show();
                        } else {
                            boolean isInserted = myDb.addOwnerAccount(username.getText().toString(), postalCode.getText().toString(), stallName.getText().toString(), password.getText().toString());

                            // on successful insert to database
                            if (isInserted) {
                                Toast.makeText(v.getContext(), "Account Created", Toast.LENGTH_LONG).show();
                                Fragment_Main fragment_main = new Fragment_Main();
                                getFragmentManager().beginTransaction().replace(R.id.login_fragment_container,fragment_main).commit();
                            } else {
                                Toast.makeText(v.getContext(), "Account not Created", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        confirmPassword.setError("Does not match your password");
                    }
                } else {
                    // username & password fields are empty
                    Toast.makeText(v.getContext(), "Required Fields are Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getFragmentManager().popBackStack();
            }
        });

        return relativeLayout;
    }

    public boolean isOwnerRegisterAcceptable(String username,String postal_code, String stall_name, String password, String confirm_password) {
        return !(username.isEmpty() || postal_code.isEmpty() || stall_name.isEmpty() || password.isEmpty() || confirm_password.isEmpty());
    }

    public boolean isPostalCodeAcceptable(String code){
        List<Address> addressList = null;

        if(code.isEmpty())
            return false;

        if(code.length() == 6){
            String temp = "Singapore " + code;
            Geocoder geocoder = new Geocoder(getContext());
            try{
                addressList = geocoder.getFromLocationName(temp,1);
            }catch(IOException e){
                e.printStackTrace();
            }
            return addressList != null;
        }
        return false;
    }
}

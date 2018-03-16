package com.example.fragment_adventure.login;

import android.database.Cursor;
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

/**
 * Created by zNotAgain on 11/3/2018.
 */

public class Fragment_CreateUserAccount extends android.support.v4.app.Fragment{

    DatabaseHelper myDb;
    EditText username,password,confirmPassword;
    Button registerButton;
    TextView alreadyMember;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_createuseraccount,container,false);
        myDb = new DatabaseHelper(mRelativeLayout.getContext());

        username = mRelativeLayout.findViewById(R.id.register_usernameText);
        password = mRelativeLayout.findViewById(R.id.register_passwordText);
        confirmPassword = mRelativeLayout.findViewById(R.id.register_confirmPasswordText);
        alreadyMember = mRelativeLayout.findViewById(R.id.already_member);
        registerButton = mRelativeLayout.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure username & password fields are not empty
                if(isUserRegisterAcceptable(username.getText().toString(),password.getText().toString(),confirmPassword.getText().toString())){
                    if(password.getText().toString().matches(confirmPassword.getText().toString())){
                        Cursor user_res = myDb.checkUserLoginData(username.getText().toString(), password.getText().toString());
                        Cursor owner_res = myDb.checkOwnerLoginData(username.getText().toString(), password.getText().toString());

                        // make sure account doesn't exist in database
                        if(user_res.getCount() == 1 || owner_res.getCount() == 1){
                            Toast.makeText(v.getContext(),"Account Already Exists",Toast.LENGTH_LONG).show();
                        }else{
                            boolean isInserted = myDb.addUserAccount(username.getText().toString(),password.getText().toString());

                            // on successful insert to database
                            if(isInserted){
                                Toast.makeText(v.getContext(),"Account Created",Toast.LENGTH_LONG).show();
                                Fragment_Main fragment_main = new Fragment_Main();
                                getFragmentManager().beginTransaction().replace(R.id.login_fragment_container,fragment_main).commit();
                            }else{
                                Toast.makeText(v.getContext(),"Account not Created",Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        confirmPassword.setError("Does not match your password");
                    }
                }else{
                    // username & password fields are empty
                    Toast.makeText(v.getContext(),"Required Fields are Empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return mRelativeLayout;
    }

    public boolean isUserRegisterAcceptable(String username, String password, String confirm_password){
        return !(username.isEmpty() || password.isEmpty() || confirm_password.isEmpty());
    }
}

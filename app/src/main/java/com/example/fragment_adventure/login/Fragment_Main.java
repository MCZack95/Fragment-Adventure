package com.example.fragment_adventure.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.owner.Activity_Owner;
import com.example.fragment_adventure.user.Activity_User;
import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;

/**
 * Created by zNotAgain on 11/3/2018.
 */

public class Fragment_Main extends Fragment {

    DatabaseHelper myDb;
    Button loginButton;
    TextView createAccountTextView;
    EditText editUserName,editPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        myDb = new DatabaseHelper(relativeLayout.getContext());

        editUserName = relativeLayout.findViewById(R.id.usernameText);
        editPassword = relativeLayout.findViewById(R.id.passwordText);

        loginButton = relativeLayout.findViewById(R.id.login);
        createAccountTextView = relativeLayout.findViewById(R.id.createAccountText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make sure username & password fields are not empty
                if (isLoginAcceptable(editUserName.getText().toString(), editPassword.getText().toString())) {
                    Cursor user_res = myDb.checkUserLoginData(editUserName.getText().toString(), editPassword.getText().toString());
                    Cursor owner_res = myDb.checkOwnerLoginData(editUserName.getText().toString(), editPassword.getText().toString());

                    // make sure account exists in database
                    if (user_res.getCount() == 1) { // if user account
                        Toast.makeText(view.getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        Intent goIntent = new Intent(view.getContext(), Activity_User.class);
                        goIntent.putExtra(Activity_Main.USER_NAME, editUserName.getText().toString());
                        goIntent.putExtra(Activity_Main.PASSWORD, editPassword.getText().toString());
                        startActivity(goIntent);
                    } else if (owner_res.getCount() == 1) { // if owner account
                        Toast.makeText(view.getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        Intent goIntent = new Intent(view.getContext(), Activity_Owner.class);
                        goIntent.putExtra(Activity_Main.STALL_NAME, myDb.getStallName(editUserName.getText().toString(), editPassword.getText().toString()));
                        startActivity(goIntent);
                    } else {
                        // show login fail message
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                        mBuilder.setCancelable(true);
                        mBuilder.setTitle("Error");
                        mBuilder.setMessage("Account doesn't exists.");
                        mBuilder.show();
                    }
                } else {
                    // username & password fields are empty
                    Toast.makeText(view.getContext(), "Required Fields are Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String[] temp = {"Food User", "Stall Owner"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Choose an Account Type: ")
                        .setItems(temp, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Fragment_CreateUserAccount fragment_createUserAccount = new Fragment_CreateUserAccount();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.login_fragment_container,fragment_createUserAccount);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                } else {
                                    Fragment_CreateOwnerAccount fragment_createOwnerAccount = new Fragment_CreateOwnerAccount();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.login_fragment_container,fragment_createOwnerAccount);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            }
                        });
                mBuilder.show();
            }
        });
        return relativeLayout;
    }
    public boolean isLoginAcceptable(String username, String password){
        return !(username.isEmpty() || password.isEmpty());
    }
}
// Check valid email code
// !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()


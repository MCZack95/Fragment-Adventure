package com.example.fragment_adventure.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 15/3/2018.
 */

public class Fragment_User_Settings extends Fragment{

    DatabaseHelper myDb;
    Button backButton,editUsernameButton,editPasswordButton,deleteAllButton;
    TextView userName,passWord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_settings,container,false);
        myDb = new DatabaseHelper(getContext());

        editUsernameButton = relativeLayout.findViewById(R.id.settings_usernameButton);
        editPasswordButton = relativeLayout.findViewById(R.id.settings_passwordButton);
        deleteAllButton = relativeLayout.findViewById(R.id.settings_deleteAccountButton);
        backButton = relativeLayout.findViewById(R.id.back);
        userName = relativeLayout.findViewById(R.id.settings_username);
        passWord = relativeLayout.findViewById(R.id.settings_password);

        Intent intent = getActivity().getIntent();
        final String username_message = intent.getStringExtra(Activity_Main.USER_NAME);
        final String password_message = intent.getStringExtra(Activity_Main.PASSWORD);

        if(username_message != null && password_message != null){
            userName.setText(username_message);
            passWord.setText(password_message);
        }

        editUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Set new Username:");
                // Set up the input
                final EditText input = new EditText(v.getContext());
                // Specify the type of input expected
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                mBuilder.setView(input);

                // Set up the buttons
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if(myDb.updateUserAccountUsername(userName.getText().toString(),m_Text)){
                            Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();
                            userName.setText(m_Text);
                        }
                        else
                            Toast.makeText(v.getContext(),"Edit Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.show();
            }
        });

        editPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Set new Password:");
                // Set up the input
                final EditText input = new EditText(v.getContext());
                // Specify the type of input expected
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mBuilder.setView(input);

                // Set up the buttons
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if(myDb.updateUserAccountPassword(passWord.getText().toString(),m_Text)){
                            Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();
                            passWord.setText(m_Text);
                        }
                        else
                            Toast.makeText(v.getContext(),"Edit Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.show();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Confirm?");
                mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(myDb.deleteUserAccount(userName.getText().toString(),passWord.getText().toString()) > 0){
                            Toast.makeText(v.getContext(),"Account Successfully Deleted",Toast.LENGTH_LONG).show();
                            // Clears all fragments from stack
                            for(int i=0;i<getFragmentManager().getBackStackEntryCount();i++){
                                getFragmentManager().popBackStack();
                            }
                            // Closes Current Activity
                            getActivity().finish();
                        }else
                            Toast.makeText(v.getContext(),"Account Delete Failed",Toast.LENGTH_LONG).show();

                    }
                });
                mBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mBuilder.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getFragmentManager().popBackStack();
            }
        });

        return relativeLayout;
    }
}

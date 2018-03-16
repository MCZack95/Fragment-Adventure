package com.example.fragment_adventure.owner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 15/3/2018.
 */

public class Fragment_Owner_Settings extends Fragment {

    DatabaseHelper myDb;
    Button backButton,editUsernameButton,editPasswordButton,editStallNameButton,deleteAccountButton;
    TextView textView,userName,passWord,stallName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_owner_settings,container,false);
        myDb = new DatabaseHelper(getContext());

        textView = relativeLayout.findViewById(R.id.owner_settings_stallName);
        backButton = relativeLayout.findViewById(R.id.back);
        editUsernameButton = relativeLayout.findViewById(R.id.settings_usernameButton);
        editPasswordButton = relativeLayout.findViewById(R.id.settings_passwordButton);
        editStallNameButton = relativeLayout.findViewById(R.id.settings_stallNameButton);
        deleteAccountButton = relativeLayout.findViewById(R.id.settings_deleteAccountButton);
        userName = relativeLayout.findViewById(R.id.settings_username);
        passWord = relativeLayout.findViewById(R.id.settings_password);
        stallName = relativeLayout.findViewById(R.id.settings_stallName);

        Intent intent = getActivity().getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            textView.setText(stallNameMessage);
            userName.setText(myDb.getOwnerUsername(stallNameMessage));
            passWord.setText(myDb.getOwnerPassword(stallNameMessage));
            stallName.setText(stallNameMessage);
            Log.e("START",stallNameMessage);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getFragmentManager().popBackStack();
            }
        });

        editUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = getActivity().getIntent();
                final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);

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
                        if(myDb.updateOwnerAccountUsername(m_Text,stallNameMessage)){
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
                Intent intent = getActivity().getIntent();
                final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);

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
                        if(myDb.updateOwnerAccountPassword(m_Text,stallNameMessage)){
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

        editStallNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = getActivity().getIntent();
                final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Set new Stall Name:");
                // Set up the input
                final EditText input = new EditText(v.getContext());
                // Specify the type of input expected
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                mBuilder.setView(input);

                // Set up the buttons
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(v.getContext());
                        tempBuilder.setCancelable(false);
                        tempBuilder.setTitle("RESET REQUIRED:");
                        tempBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text = input.getText().toString();
                                if(myDb.updateOwnerAccountStallName(stallNameMessage,m_Text) > 0){
                                    Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();
                                    stallName.setText(m_Text);
                                    textView.setText(m_Text);

                                    for(int i=0;i<getFragmentManager().getBackStackEntryCount();i++){
                                        getFragmentManager().popBackStack();
                                    }
                                    getActivity().finish();

                                }
                                else
                                    Toast.makeText(v.getContext(),"Edit Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                        tempBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        tempBuilder.show();
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

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = getActivity().getIntent();
                final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Confirm?");
                mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(myDb.deleteOwnerAccount(stallNameMessage) > 0){
                            Toast.makeText(v.getContext(),"Account Successfully Deleted",Toast.LENGTH_LONG).show();

                            for(int i=0;i<getFragmentManager().getBackStackEntryCount();i++){
                                getFragmentManager().popBackStack();
                            }
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
        return relativeLayout;
    }
}

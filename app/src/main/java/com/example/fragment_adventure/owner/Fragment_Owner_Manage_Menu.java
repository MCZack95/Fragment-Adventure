package com.example.fragment_adventure.owner;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Fragment_Owner_Manage_Menu extends ListFragment {
    DatabaseHelper myDb;
    TextView mTextView;
    Adapter_Owner_Manage_Menu mAdapterManageMenu;
    Button backButton,addMenuItemButton;
    String[] strings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_owner_manage_menu,container,false);
        myDb = new DatabaseHelper(getContext());

        mTextView = relativeLayout.findViewById(R.id.stall_name_holder);
        backButton = relativeLayout.findViewById(R.id.back);
        addMenuItemButton = relativeLayout.findViewById(R.id.add_item_icon);

        Intent intent = getActivity().getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        addMenuItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Set Food Name: ");

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
                        if(myDb.addMenuArrayData(m_Text,stallNameMessage))
                            Toast.makeText(v.getContext(),"Data Added",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(v.getContext(),"Data not Added",Toast.LENGTH_SHORT).show();
                        // Resets the ListView
                        strings = myDb.getStallMenu(stallNameMessage);
                        mAdapterManageMenu = new Adapter_Owner_Manage_Menu(getContext(),R.layout.row_manage_menu,strings);
                        setListAdapter(mAdapterManageMenu);
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
        return relativeLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);

        strings = myDb.getStallMenu(stallNameMessage);
        mAdapterManageMenu = new Adapter_Owner_Manage_Menu(getContext(),R.layout.row_manage_menu,strings);
        setListAdapter(mAdapterManageMenu);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.v("PLACE",strings[position]);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Remove item?");
                mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer deletedRows = myDb.deleteMenuArrayData(strings[position],stallNameMessage);
                        if(deletedRows > 0)
                            Toast.makeText(getContext(),"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(),"Data not Deleted",Toast.LENGTH_LONG).show();

                        // Resets the ListView
                        strings = myDb.getStallMenu(stallNameMessage);
                        mAdapterManageMenu = new Adapter_Owner_Manage_Menu(getContext(),R.layout.row_manage_menu,strings);
                        setListAdapter(mAdapterManageMenu);
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
    }
}

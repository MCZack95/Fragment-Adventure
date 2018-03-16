package com.example.fragment_adventure.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 15/3/2018.
 */

public class Fragment_User_History extends ListFragment {

    DatabaseHelper myDb;
    Adapter_User_Transaction_History mAdapterTransHist;
    Button backButton,deleteAllButton;
    String[] strings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_hist,container,false);
        myDb = new DatabaseHelper(getContext());

        backButton = relativeLayout.findViewById(R.id.back);
        deleteAllButton = relativeLayout.findViewById(R.id.clearAllButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Clear All Data?");
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer deletedRows = myDb.deleteAllUserHistoryData(usernameMessage);
                        if(deletedRows > 0)
                            Toast.makeText(getContext(),"All Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(),"Data not Deleted",Toast.LENGTH_LONG).show();

                        // Resets the ListView
                        strings = myDb.getUserArrayOfHistory(usernameMessage);
                        mAdapterTransHist = new Adapter_User_Transaction_History(getContext(),R.layout.row_transaction_history,strings);
                        setListAdapter(mAdapterTransHist);
                    }
                });
                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);
        strings = myDb.getUserArrayOfHistory(usernameMessage);

        mAdapterTransHist = new Adapter_User_Transaction_History(getContext(),R.layout.row_transaction_history,strings);
        setListAdapter(mAdapterTransHist);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Clear Data?");
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer deletedRows = myDb.deleteUserHistoryArrayData(strings[position],usernameMessage);
                        if(deletedRows > 0)
                            Toast.makeText(getContext(),"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(),"Data not Deleted",Toast.LENGTH_LONG).show();

                        // Resets the ListView
                        strings = myDb.getUserArrayOfHistory(usernameMessage);
                        mAdapterTransHist = new Adapter_User_Transaction_History(getContext(),R.layout.row_transaction_history,strings);
                        setListAdapter(mAdapterTransHist);
                    }
                });
                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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

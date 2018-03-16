package com.example.fragment_adventure.user;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;
import com.example.fragment_adventure.login.Activity_Main;

/**
 * Created by zNotAgain on 15/3/2018.
 */

public class Fragment_User_Orders extends ListFragment {

    DatabaseHelper myDb;
    TextView mTextView;
    Adapter_User_Current_Orders mAdapterCurrentOrders;
    Button backButton;
    String[] strings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_orders,container,false);
        myDb = new DatabaseHelper(getContext());

        mTextView = relativeLayout.findViewById(R.id.current_orders);
        backButton = relativeLayout.findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return relativeLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle incomingBundle = this.getArguments();
        String usernameMessage = incomingBundle.getString(Activity_Main.USER_NAME);
        strings = myDb.getUserArrayOfOrders(usernameMessage);
        mAdapterCurrentOrders = new Adapter_User_Current_Orders(getContext(),R.layout.row_current_orders,strings);
        setListAdapter(mAdapterCurrentOrders);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { Toast.makeText(view.getContext(),strings[position],Toast.LENGTH_LONG).show(); }});
    }
}

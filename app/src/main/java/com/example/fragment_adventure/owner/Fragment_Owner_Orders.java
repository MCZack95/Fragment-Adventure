package com.example.fragment_adventure.owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Fragment_Owner_Orders extends ListFragment {

    DatabaseHelper myDb;
    TextView mTextView;
    Adapter_Owner_Current_Orders mAdapterCurrentOrders;
    Button backButton;
    String[] strings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_owner_orders,container,false);

        mTextView = relativeLayout.findViewById(R.id.current_orders_stallName);
        backButton = relativeLayout.findViewById(R.id.back);

        Intent intent = getActivity().getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getFragmentManager().popBackStack();
            }
        });
        return relativeLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myDb = new DatabaseHelper(getContext());

        final String stallNameMessage = mTextView.getText().toString();
        strings = myDb.getArrayOfOrders(stallNameMessage);
        mAdapterCurrentOrders = new Adapter_Owner_Current_Orders(getContext(),R.layout.row_current_orders,strings);

        setListAdapter(mAdapterCurrentOrders);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Log.v("PLACE",strings[position]);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Choose an Action: ").setItems(R.array.Array_currentOrder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            // Finish Order
                            myDb.addHistoryArrayData(strings[position],myDb.getBuyerUsername(strings[position],stallNameMessage),stallNameMessage);
                            myDb.addUserHistoryArrayData(strings[position],myDb.getBuyerUsername(strings[position],stallNameMessage),stallNameMessage);
                            Integer deletedRows = myDb.deleteOrderArrayData(strings[position],stallNameMessage);
                            if(deletedRows > 0){
                                Toast.makeText(getContext(),"Order Completed",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getContext(),"Order not Completed",Toast.LENGTH_LONG).show();

                            // Resets the ListView
                            strings = myDb.getArrayOfOrders(stallNameMessage);
                            mAdapterCurrentOrders = new Adapter_Owner_Current_Orders(getContext(),R.layout.row_current_orders,strings);
                            setListAdapter(mAdapterCurrentOrders);
                        }else{
                            // Cancel Order
                            Integer deletedRows = myDb.deleteOrderArrayData(strings[position],stallNameMessage);
                            if(deletedRows > 0)
                                Toast.makeText(getContext(),"Order Canceled",Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getContext(),"Order not Canceled",Toast.LENGTH_LONG).show();
                            // Resets the ListView
                            strings = myDb.getArrayOfOrders(stallNameMessage);
                            mAdapterCurrentOrders = new Adapter_Owner_Current_Orders(getContext(),R.layout.row_current_orders,strings);
                            setListAdapter(mAdapterCurrentOrders);
                        }
                    }
                });
                mBuilder.show();
            }
        });
    }
}

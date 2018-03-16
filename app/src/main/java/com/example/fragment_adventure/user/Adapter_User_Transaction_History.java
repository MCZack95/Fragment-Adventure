package com.example.fragment_adventure.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.R;


/**
 * Created by zNotAgain on 9/3/2018.
 */

public class Adapter_User_Transaction_History extends ArrayAdapter<String> {

    Context mContext;
    int mLayoutResId;
    String mData[] = null;

    public Adapter_User_Transaction_History(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mLayoutResId = resource;
        this.mData = objects;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Adapter_User_Transaction_History.PlaceHolder holder = null;

        //if we currently don't have a row View to reuse...
        if(row == null){
            //Create a new View
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResId,parent,false);

            holder = new Adapter_User_Transaction_History.PlaceHolder();

            holder.nameView = row.findViewById(R.id.row_text);
            holder.imageView = row.findViewById(R.id.row_image);

            row.setTag(holder);
        }else{
            //Otherwise use an existing View
            holder = (Adapter_User_Transaction_History.PlaceHolder) row.getTag();
        }

        //Getting the data from the data array
        String string = mData[position];

        //Setup and reuse the same listener for each row
        holder.nameView.setOnClickListener(PopupListener);
        Integer rowPosition = position;
        holder.nameView.setTag(rowPosition);

        //setting the view to reflect the data we need to display
        holder.nameView.setText(string);
        holder.imageView.setImageResource(R.drawable.ic_delete);

        //returning the row (because this is called getView after all
        return row;

    }

    View.OnClickListener PopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer viewPosition = (Integer) v.getTag();
            String string = mData[viewPosition];
            Toast.makeText(getContext(),string, Toast.LENGTH_SHORT).show();
        }
    };

    private static class PlaceHolder {
        TextView nameView;
        ImageView imageView;
    }
}

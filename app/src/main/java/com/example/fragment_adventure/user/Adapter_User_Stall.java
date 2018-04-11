package com.example.fragment_adventure.user;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment_adventure.DatabaseHelper;
import com.example.fragment_adventure.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Adapter_User_Stall extends ArrayAdapter<String> {

    DatabaseHelper myDb = new DatabaseHelper(getContext());
    Context mContext;
    int mLayoutResId;
    String mData[] = null;
    LatLng defaultLatLng;

    public Adapter_User_Stall(Context context, int resource, String[] objects, LatLng latLng) {
        super(context, resource, objects);
        this.mContext = context;
        this.mLayoutResId = resource;
        this.mData = objects;
        this.defaultLatLng = latLng;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlaceHolder holder = null;

        //if we currently don't have a row View to reuse...
        if(row == null){
            //Create a new View
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResId,parent,false);

            holder = new PlaceHolder();

            holder.nameView = row.findViewById(R.id.stallNameTextView);
            holder.distanceView = row.findViewById(R.id.distanceTextView);

            row.setTag(holder);
        }else{
            //Otherwise use an existing View
            holder = (PlaceHolder) row.getTag();
        }

        //Getting the data from the data array
        String string = mData[position];

        //Setup and reuse the same listener for each row
        holder.nameView.setOnClickListener(PopupListener);
        Integer rowPosition = position;
        holder.nameView.setTag(rowPosition);

        //setting the view to reflect the data we need to display
        holder.nameView.setText(string);

        //setting the distance view
        holder.distanceView.setText(String.valueOf(searchLocation(myDb.getPostalCode(string))));

        //returning the row (because this is called getView after all
        return row;

    }

    View.OnClickListener PopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer viewPosition = (Integer) v.getTag();
            String string = mData[viewPosition];
            Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
        }
    };

    private static class PlaceHolder {
        TextView nameView,distanceView;
    }

    private float searchLocation(String postal_code){
        List<Address> addressList = null;
        String temp = "Singapore " + postal_code;

        if(!postal_code.isEmpty()){
            Geocoder geocoder = new Geocoder(getContext());
            try{
                addressList = geocoder.getFromLocationName(temp,1);
            }catch(IOException e){
                e.printStackTrace();
            }
            if(addressList != null){
                try{
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    return getDistance(latLng);
                } catch(IndexOutOfBoundsException e){
                    Toast.makeText(getContext(),"Location not found",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(),"Location not found",Toast.LENGTH_SHORT).show();
            }
        }
        return 0;
    }

    private float getDistance(LatLng latLng){
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList1 = null;
        List<Address> addressList2 = null;
        try{
            addressList1 = geocoder.getFromLocation(defaultLatLng.latitude,defaultLatLng.longitude,1);
            addressList2 = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        }catch(IOException e){
            e.printStackTrace();
        }
        if(addressList1 != null && addressList2 != null){
            Address address1 = addressList1.get(0);
            Address address2 = addressList2.get(0);

            Location defaultLocation = new Location("Default");

            defaultLocation.setLatitude(address1.getLatitude());
            defaultLocation.setLongitude(address1.getLongitude());

            Location targetLocation = new Location("Target");

            targetLocation.setLatitude(address2.getLatitude());
            targetLocation.setLongitude(address2.getLongitude());

            return defaultLocation.distanceTo(targetLocation);
        }
        return 0;
    }
}

package com.example.mrt.diadiemlixi.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrt.diadiemlixi.MainActivity;
import com.example.mrt.diadiemlixi.Model.LocationModel;
import com.example.mrt.diadiemlixi.PopupLocation;
import com.example.mrt.diadiemlixi.R;

import java.util.ArrayList;

/**
 * Created by Mr.T on 17/11/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private ArrayList<LocationModel> listLocation;
    private MainActivity mActivity;
    private PopupLocation popupLocation;


    public CityAdapter(ArrayList<LocationModel> listLocation, MainActivity mActivity, PopupLocation popupLocation) {
        this.listLocation = listLocation;
        this.mActivity = mActivity;
        this.popupLocation = popupLocation;
    }


    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_location, parent, false);
        return new CityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder holder, int position) {
        final LocationModel locationModel = listLocation.get(position);
        holder.txt_City.setText(locationModel.getName());
        holder.txt_City.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.loadData();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listLocation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_City;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_City = itemView.findViewById(R.id.txt_city);
            txt_City.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    notifyDataSetChanged();
                    mActivity.loadCity(listLocation.get(position).getId(), listLocation.get(position).getName());
                    popupLocation.dismiss();
                }
            });
        }
    }
}

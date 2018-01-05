package com.example.mrt.diadiemlixi.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrt.diadiemlixi.MainActivity;
import com.example.mrt.diadiemlixi.Model.LocationModel;
import com.example.mrt.diadiemlixi.PopupLocation;
import com.example.mrt.diadiemlixi.R;

import java.util.ArrayList;

/**
 * Created by Mr.T on 17/11/2017.
 */

public class DistrictAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<LocationModel> listdistrict;
    private MainActivity mActivity;
    int selectID = -1;
    public static int HEADER = 0;
    public static int BODY = 1;


    public DistrictAdapter(ArrayList<LocationModel> listdistrict, MainActivity mActivity) {
        this.listdistrict = listdistrict;
        this.mActivity = mActivity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER) {
            return HEADER;
        } else {
            return BODY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant_city, parent, false);
            return new DistrictAdapter.HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant_district, parent, false);
            return new DistrictAdapter.IteamViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == HEADER) {
            HeaderViewHolder headerViewHolder;
            headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.txtToanquoc.setText(listdistrict.get(position).getName());

            if (selectID == -1) {
                headerViewHolder.img_toanquoc.setVisibility(View.VISIBLE);
                headerViewHolder.txtToanquoc.setTextColor(0xFFD50000);
            } else {
                headerViewHolder.img_toanquoc.setVisibility(View.INVISIBLE);
                headerViewHolder.txtToanquoc.setTextColor(0xFF000000);
            }
            final HeaderViewHolder iteamHeader = headerViewHolder;
            iteamHeader.llToanQuoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    selectID = listdistrict.get(position).getId();
                    mActivity.loadCity(listdistrict.get(position).getId(), listdistrict.get(position).getName());
                }
            });

        } else {
            IteamViewHolder iteamViewHolder = (IteamViewHolder) holder;
            iteamViewHolder.txtDistrict.setText(listdistrict.get(position).getName());
            if (listdistrict.get(position).getId() == selectID) {
                iteamViewHolder.imgDistrict.setVisibility(View.VISIBLE);
                iteamViewHolder.txtDistrict.setTextColor(0xFFD50000);
            } else {
                iteamViewHolder.imgDistrict.setVisibility(View.INVISIBLE);
                iteamViewHolder.txtDistrict.setTextColor(0xFF000000);
            }
            final IteamViewHolder iteam = iteamViewHolder;
            iteam.llDistrict.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    selectID = listdistrict.get(position).getId();
                    mActivity.loadCityDistrict(listdistrict.get(position).getParent_id(), listdistrict.get(position).getId());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (listdistrict == null) {
            return HEADER;
        }
        return listdistrict.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llToanQuoc;
        private ImageView img_toanquoc;
        private TextView txtToanquoc;
        private LinearLayout llDoitinhthanh;


        public HeaderViewHolder(final View itemView) {
            super(itemView);
            llToanQuoc = itemView.findViewById(R.id.ll_ToanQuoc);
            img_toanquoc = itemView.findViewById(R.id.img_toanquoc);
            txtToanquoc = itemView.findViewById(R.id.txt_toanquoc);
            llDoitinhthanh = itemView.findViewById(R.id.ll_doitinhthanh);

            llDoitinhthanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectID = -1;
                    PopupLocation popupLocation = new PopupLocation(mActivity);
                    popupLocation.show();
                }
            });

        }
    }

    public class IteamViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llDistrict;
        private ImageView imgDistrict;
        private TextView txtDistrict;

        public IteamViewHolder(final View itemView) {
            super(itemView);
            llDistrict = itemView.findViewById(R.id.ll_district);
            imgDistrict = itemView.findViewById(R.id.img_district);
            txtDistrict = itemView.findViewById(R.id.txt_district);


        }
    }


}

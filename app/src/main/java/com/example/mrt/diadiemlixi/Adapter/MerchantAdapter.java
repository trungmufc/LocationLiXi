package com.example.mrt.diadiemlixi.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrt.diadiemlixi.MainActivity;
import com.example.mrt.diadiemlixi.Model.MerchantModel;
import com.example.mrt.diadiemlixi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.T on 17/11/2017.
 */

public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.ViewHolder> {
    private ArrayList<MerchantModel> listMerchant;
    private MainActivity mActivity;

    public MerchantAdapter(ArrayList<MerchantModel> listMerchant, MainActivity mActivity) {
        this.listMerchant = listMerchant;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_merchand, parent, false);
        return new MerchantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MerchantModel merchantModel = listMerchant.get(position);
        String url = merchantModel.getImage_medium();
        Picasso.with(mActivity).load(url).into(holder.imgMerchant);
        holder.txtNameMerchant.setText(merchantModel.getName());
        holder.txtNumberMerchant.setText("#" + merchantModel.getDiscount_rate() + "%");
        holder.txtTonghoadon.setText("#" + merchantModel.getInfo_condition());

    }

    @Override
    public int getItemCount() {
        return listMerchant.size();
    }

    public void filter(List<MerchantModel> merchantList){
        listMerchant = new ArrayList<>();
        listMerchant.addAll(merchantList);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMerchant;
        TextView txtNameMerchant;
        TextView txtTonghoadon;
        TextView txtNumberMerchant;

        public ViewHolder(View itemView) {
            super(itemView);
            imgMerchant = itemView.findViewById(R.id.img_merchant);
            txtNameMerchant = itemView.findViewById(R.id.txt_name_merchant);
            txtTonghoadon = itemView.findViewById(R.id.txt_tonghoadon);
            txtNumberMerchant = itemView.findViewById(R.id.txt_number_merchant);

            Display display = mActivity.getWindowManager().getDefaultDisplay();
            int height = (display.getWidth() * 9) / 16;
            imgMerchant.getLayoutParams().height = height;
        }
    }
}

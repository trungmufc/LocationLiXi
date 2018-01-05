package com.example.mrt.diadiemlixi.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrt.diadiemlixi.MainActivity;
import com.example.mrt.diadiemlixi.Model.CategoryModel;
import com.example.mrt.diadiemlixi.R;

import java.util.ArrayList;

/**
 * Created by Mr.T on 17/11/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryModel> listCategory;
    private MainActivity mActivity;
    private String url;

    public CategoryAdapter(ArrayList<CategoryModel> listCategory, MainActivity mActivity) {
        this.listCategory = listCategory;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant_danhmuc, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CategoryModel categoryModel = listCategory.get(position);
        holder.txtTatca.setText(categoryModel.getName());
        if (categoryModel.isCheck()) {
            holder.imgCheckTatca.setVisibility(View.VISIBLE);
            holder.txtTatca.setTextColor(0xFFD50000);
        } else {
            holder.imgCheckTatca.setVisibility(View.INVISIBLE);
            holder.txtTatca.setTextColor(0xFF000000);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listCategory.size(); i++) {
                    listCategory.get(i).setCheck(false);

                }
                listCategory.get(position).setCheck(true);
                notifyDataSetChanged();
                mActivity.loadCategory(categoryModel.getId(), categoryModel.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCheckTatca;
        TextView txtTatca;

        public ViewHolder(View itemView) {
            super(itemView);
            imgCheckTatca = itemView.findViewById(R.id.img_check_tatca);
            txtTatca = itemView.findViewById(R.id.txt_tatca);
        }
    }
}

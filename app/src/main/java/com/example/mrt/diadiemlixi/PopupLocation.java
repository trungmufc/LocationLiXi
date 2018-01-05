package com.example.mrt.diadiemlixi;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrt.diadiemlixi.Adapter.CityAdapter;
import com.example.mrt.diadiemlixi.Model.LocationModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by 09123 on 10/27/2015.
 */
public class PopupLocation extends Dialog implements
        View.OnClickListener {

    protected MainActivity activity;
    private CityAdapter cityAdapter;

    private ArrayList<LocationModel> locationList;

    private RecyclerView rl_Location;
    private LinearLayout ll_root_popup_menu;


    String mss = "";

    public PopupLocation(MainActivity a) {
        super(a);
        activity = a;
    }
    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(
                (WindowManager.LayoutParams) params);
//        if (!dimBackgroud)
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        if (fullScreen)
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_location);
        findViews();
        loadDataPoppup(0);

    }

    private void findViews() {


        ll_root_popup_menu =  findViewById(R.id.ll_root_popup_menu);
        rl_Location =  findViewById(R.id.RecycleTinhthanh);
        rl_Location.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));


    }

    private void loadDataPoppup(final int provinceId) {
        locationList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = "http://45.118.151.83:8000/region?parent_id=" + provinceId;

        JsonObjectRequest JsonResTown = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    locationList.clear();
                try {
                    JSONArray jsonArrTown = response.getJSONArray("records");
                    locationList.add(new LocationModel(-1, "Toàn quốc", 1));
                    for (int i = 0; i < jsonArrTown.length(); i++) {
                        JSONObject JsonTown = jsonArrTown.getJSONObject(i);
                        locationList.add(new LocationModel(
                                JsonTown.getInt("id"),
                                JsonTown.getString("name"),
                                JsonTown.getInt("parent_id")
                        ));
                    }
                    cityAdapter = new CityAdapter(locationList, activity, PopupLocation.this);
                    rl_Location.setAdapter(cityAdapter);
                    cityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(JsonResTown);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_dismiss:
                dismiss();
                break;
            default:
                break;
        }
    }

}

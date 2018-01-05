package com.example.mrt.diadiemlixi;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrt.diadiemlixi.Adapter.CategoryAdapter;
import com.example.mrt.diadiemlixi.Adapter.DistrictAdapter;
import com.example.mrt.diadiemlixi.Adapter.MerchantAdapter;
import com.example.mrt.diadiemlixi.Model.CategoryModel;
import com.example.mrt.diadiemlixi.Model.LocationModel;
import com.example.mrt.diadiemlixi.Model.MerchantModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "";
    private final String GAN_NHAT = "geo_distance%20asc";
    private final String LATTITUDE = "lat=";
    private final String LONGITUDE = "lng=";

    private final String MOI_NHAT = "id%20desc";
    private final String HOT_NHAT = "discount_rate%20desc";
    private final String NHIEU_NGUOI_NHAT = "count_attend%20desc";
    private final String KHUYEN_MAI_NHIEU_NHAT = "count_promotion%20desc";

    private String CATEGORY_ID = "category_id=";
    private String CITY_ID = "city_id=";
    private String DISTRICT_ID = "district_id=";


    private RelativeLayout rl_search;
    private ImageView img_search;
    private EditText edt_search;
    private ImageView img_clear_text;
    private LinearLayout ll_menu;
    private LinearLayout ll_tab_sort;
    private TextView txt_sort;
    private LinearLayout ll_tab_filter;
    private TextView txt_filter;
    private LinearLayout ll_tab_location;
    private TextView txt_location;
    private RecyclerView rv_merchant;
    private LinearLayout ll_sort;
    private LinearLayout ll_gannhat;
    private ImageView Img_gannhat;
    private TextView txt_GanNhat;
    private LinearLayout ll_MoiNhat;
    private ImageView Img_Moinhat;
    private TextView txt_Moinhat;
    private LinearLayout ll_Hotnhat;
    private ImageView Img_Hotnhat;
    private TextView txt_Hotnhat;
    private LinearLayout ll_Nhieunguoinhat;
    private ImageView Img_Nhieunguoi;
    private TextView txt_Nhieunguoi;
    private LinearLayout ll_SaleOff;
    private ImageView Img_SaleOff;
    private TextView txt_SaleOff;
    private RecyclerView rv_category;
    private RecyclerView rv_district;
    private SwipeRefreshLayout swipe_refresh;

    private ArrayList<MerchantModel> listMerchant;
    private MerchantAdapter merchantAdapter;

    private ArrayList<CategoryModel> listCategory;
    private CategoryAdapter categoryAdapter;

    private ArrayList<LocationModel> listLocation;
    private DistrictAdapter districtAdapter;


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude, longitude;

    int idPage = 1;
    String sort = MOI_NHAT;
    int category = -1;
    int city_id = -1;
    int district_id = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();


        loadAdapter();
        loadDataCategory();
        loadCity(-1, "Toàn quốc");
        loadData();
        isOnline();
        initEvent();
        scrollRecyclerView();
        searchMerchant();
        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }

    private void searchMerchant() {
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager input = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    input.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    edt_search.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private void scrollRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        rv_merchant.setLayoutManager(mLayoutManager);
        merchantAdapter = new MerchantAdapter(listMerchant, this);
        rv_merchant.setAdapter(merchantAdapter);

        rv_merchant.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        loadData();
                    }
                }
            }
        });
    }

    private void findViews() {
        swipe_refresh = findViewById(R.id.swipe_refresh);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        img_search = (ImageView) findViewById(R.id.img_search);
        edt_search = (EditText) findViewById(R.id.edt_search);
        img_clear_text = (ImageView) findViewById(R.id.img_clear_text);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        ll_tab_sort = (LinearLayout) findViewById(R.id.ll_tab_sort);
        txt_sort = (TextView) findViewById(R.id.txt_sort);
        ll_tab_filter = (LinearLayout) findViewById(R.id.ll_tab_filter);
        txt_filter = (TextView) findViewById(R.id.txt_filter);
        ll_tab_location = (LinearLayout) findViewById(R.id.ll_tab_location);
        txt_location = (TextView) findViewById(R.id.txt_location);
        rv_merchant = (RecyclerView) findViewById(R.id.rv_merchant);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        ll_gannhat = (LinearLayout) findViewById(R.id.ll_gannhat);
        Img_gannhat = (ImageView) findViewById(R.id.Img_gannhat);
        txt_GanNhat = (TextView) findViewById(R.id.txt_GanNhat);
        ll_MoiNhat = (LinearLayout) findViewById(R.id.ll_MoiNhat);
        Img_Moinhat = (ImageView) findViewById(R.id.Img_Moinhat);
        txt_Moinhat = (TextView) findViewById(R.id.txt_Moinhat);
        ll_Hotnhat = (LinearLayout) findViewById(R.id.ll_Hotnhat);
        Img_Hotnhat = (ImageView) findViewById(R.id.Img_Hotnhat);
        txt_Hotnhat = (TextView) findViewById(R.id.txt_Hotnhat);
        ll_Nhieunguoinhat = (LinearLayout) findViewById(R.id.ll_Nhieunguoinhat);
        Img_Nhieunguoi = (ImageView) findViewById(R.id.Img_Nhieunguoi);
        txt_Nhieunguoi = (TextView) findViewById(R.id.txt_Nhieunguoi);
        ll_SaleOff = (LinearLayout) findViewById(R.id.ll_SaleOff);
        Img_SaleOff = (ImageView) findViewById(R.id.Img_SaleOff);
        txt_SaleOff = (TextView) findViewById(R.id.txt_SaleOff);
        rv_category = (RecyclerView) findViewById(R.id.rv_category);
       /* sv_Toanquoc = (ScrollView) findViewById(R.id.sv_Toanquoc);
        ll_Toanquoc = (LinearLayout) findViewById(R.id.ll_Toanquoc);*/
        /*ll_selected_location = (LinearLayout) findViewById(R.id.ll_selected_location);
        img_selected_location = (ImageView) findViewById(R.id.img_selected_location);
        txt_selected_location = (TextView) findViewById(R.id.txt_selected_location);
        ll_Doitinhthanh = (LinearLayout) findViewById(R.id.ll_Doitinhthanh);
        txt_DoiTinhThanh = (TextView) findViewById(R.id.txt_DoiTinhThanh);*/
        rv_district = (RecyclerView) findViewById(R.id.rv_district);
    }

    private void filter(String list_merchant) {
        ArrayList<MerchantModel> merchantList = new ArrayList<>();
        for (MerchantModel mc : listMerchant) {
            if (mc.getName().toLowerCase().contains(list_merchant.toLowerCase())) {
                merchantList.add(mc);
            }
            merchantAdapter.filter(merchantList);
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null

                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            //Toast.makeText(getApplicationContext(), "Internet Connection Not Present", Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Check your internet connection and try again.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
            return false;
        }
    }
    public void initEvent() {
        ll_tab_sort.setOnClickListener(this);
        ll_tab_filter.setOnClickListener(this);
        ll_tab_location.setOnClickListener(this);
        ll_gannhat.setOnClickListener(this);
        ll_MoiNhat.setOnClickListener(this);
        ll_Hotnhat.setOnClickListener(this);
        ll_Nhieunguoinhat.setOnClickListener(this);
        ll_SaleOff.setOnClickListener(this);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh.setRefreshing(false);
                isOnline();
                shuffItems();
                idPage = 1;
                loadData();
            }
        });


    }
    public void shuffItems(){
        if(isOnline()){
            //Collections.shuffle(listMerchant);
            merchantAdapter = new MerchantAdapter(listMerchant, this);
            rv_merchant.setAdapter(merchantAdapter);
        }else {
            listMerchant.clear();
        }


    }
    public void loadAdapter() {
        listMerchant = new ArrayList<>();
        merchantAdapter = new MerchantAdapter(listMerchant, this);
        rv_merchant.setAdapter(merchantAdapter);
        rv_merchant.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        listCategory = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(listCategory, this);
        rv_category.setAdapter(categoryAdapter);
        rv_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        listLocation = new ArrayList<>();
        districtAdapter = new DistrictAdapter(listLocation, MainActivity.this);
        rv_district.setAdapter(districtAdapter);
        rv_district.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));


    }

    public void loadDataCategory() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String Url = "http://45.118.151.83:8000/category?parent_id=0";
        Log.d("category", Url);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("records");
                            listCategory.add(new CategoryModel("Tất cả cửa hàng", -1));
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listCategory.add(new CategoryModel(
                                        jsonObject.getString("name"),
                                        jsonObject.getInt("id")
                                ));
                            }

                            categoryAdapter.notifyDataSetChanged();
                            //Log.d("myapi",categoryList.size()+"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void loadCategory(int idCategory, String name) {
        rv_category.setVisibility(View.GONE);
        txt_filter.setText(name);
        ll_tab_filter.setBackgroundColor(0x000);
        if (listMerchant.size() > 0) {
            listMerchant.clear();
        }
        idPage = 1;
        category = idCategory;
        loadData();
    }

    public void loadDataCity(final int provinceId, final String city_name) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String Url = "http://45.118.151.83:8000/region?parent_id=" + provinceId;

        JsonObjectRequest JsonResTown = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listLocation.clear();
                try {
                    JSONArray jsonArrTown = response.getJSONArray("records");
                    listLocation.add(new LocationModel(provinceId, city_name, -1));
                    for (int i = 0; i < jsonArrTown.length(); i++) {
                        JSONObject JsonTown = jsonArrTown.getJSONObject(i);
                        listLocation.add(new LocationModel(
                                JsonTown.getInt("id"),
                                JsonTown.getString("name"),
                                JsonTown.getInt("parent_id")
                        ));
                    }
                    districtAdapter.notifyDataSetChanged();
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

    public void loadCity(int idCity, String nameCity) {
        rv_district.setVisibility(View.GONE);
        txt_location.setText(nameCity);
        ll_tab_location.setBackgroundColor(0x000);
        if (listMerchant.size() > 0) {
            listMerchant.clear();
        }
        idPage = 1;
        city_id = idCity;
        loadDataCity(city_id, nameCity);
        loadData();

    }

    public void loadCityDistrict(int id_City, int id_District) {
        rv_district.setVisibility(View.GONE);
        ll_tab_location.setBackgroundColor(0x000);
        if (listMerchant.size() > 0) {
            listMerchant.clear();
        }
        idPage = 1;
        city_id = id_City;
        district_id = id_District;
        loadData();
    }


    public void loadData() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://45.118.151.83:8000/merchant?"
                + "page=" + idPage
                + "&" + "order=" + sort;

        if (category != -1)
            url = url + "&" + CATEGORY_ID + category;
        if (city_id != -1)
            url = url + "&" + CITY_ID + city_id;
        if (district_id != -1)
            url = url + "&" + DISTRICT_ID + district_id;

       /* if (category != -1) {
            url = "http://45.118.151.83:8000/merchant?"
                    + "page=" + idPage
                    + "&" + CATEGORY_ID + category
                    + "&" + "order=" + sort;
        }
        if (city_id != -1) {
            url = "http://45.118.151.83:8000/merchant?"
                    + "page=" + idPage
                    + "&" + CATEGORY_ID + category
                    + "&" + CITY_ID + city_id
                    + "&" + "order=" + sort;
        }
        if (district_id != -1) {
            url = "http://45.118.151.83:8000/merchant?"
                    + "page=" + idPage
                    + "&" + CATEGORY_ID + category
                    + "&" + CITY_ID + city_id
                    + "&" + DISTRICT_ID + district_id
                    + "&" + "order=" + sort;
        }*/

        Log.d("mylog", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* listMerchand.clear();*/
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = (JSONArray) jsonObject.get("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                MerchantModel merchant = new MerchantModel();
                                JSONObject res = (JSONObject) jsonArray.get(i);

                                merchant.setName((String) res.get("name"));
                                merchant.setInfo_condition((String) res.get("info_condition"));
                                int a = (int) res.get("discount_rate");
                                merchant.setDiscount_rate(String.valueOf(a));

                                JSONArray jsonImage = (JSONArray) res.get("image_list");
                                for (int j = 0; j < jsonImage.length(); j++) {
                                    JSONObject js = (JSONObject) jsonImage.get(j);
                                    merchant.setImage_medium((String) js.get("image_medium"));
                                }
                                listMerchant.add(merchant);
                            }
/*                            if(merchantAdapter != null){
                                merchantAdapter.notifyDataSetChanged();
                            }*/
                            if (jsonArray.length() > 0) {
                                idPage++;
                            }
                            merchantAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(stringRequest);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab_sort:
                if (ll_sort.getVisibility() == View.VISIBLE) {
                    visibleTabSort(false);
                } else {
                    visibleTabSort(true);
                    visibleTabFilter(false);
                    visibleTabLocation(false);
                }
                break;
            case R.id.ll_tab_filter:
                if (rv_category.getVisibility() == View.VISIBLE) {
                    visibleTabFilter(false);
                } else {
                    visibleTabSort(false);
                    visibleTabFilter(true);
                    visibleTabLocation(false);
                }
                break;
            case R.id.ll_tab_location:
                if (rv_district.getVisibility() == View.VISIBLE) {
                    visibleTabLocation(false);
                } else {
                    visibleTabSort(false);
                    visibleTabFilter(false);
                    visibleTabLocation(true);
                }
                break;

            case R.id.ll_gannhat:

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
                if (listMerchant.size() > 0) {
                    listMerchant.clear();
                }

                idPage = 1;
                sort = GAN_NHAT + "&" + LATTITUDE +  lattitude + "&" + LONGITUDE + longitude;
                loadData();
                visibleGanNhat(true);
                visibleMoiNhat(false);
                visibleHotNhat(false);
                visibleTabNhieuNguoi(false);
                visibleTabSaleOff(false);
                break;
            case R.id.ll_MoiNhat:
                if (listMerchant.size() > 0) {
                    listMerchant.clear();
                }
                idPage = 1;
                sort = MOI_NHAT;
                loadData();
                visibleGanNhat(false);
                visibleMoiNhat(true);
                visibleHotNhat(false);
                visibleTabNhieuNguoi(false);
                visibleTabSaleOff(false);
                break;
            case R.id.ll_Hotnhat:
                if (listMerchant.size() > 0) {
                    listMerchant.clear();
                }
                idPage = 1;
                sort = HOT_NHAT;
                loadData();
                visibleGanNhat(false);
                visibleMoiNhat(false);
                visibleHotNhat(true);
                visibleTabNhieuNguoi(false);
                visibleTabSaleOff(false);
                break;
            case R.id.ll_Nhieunguoinhat:
                if (listMerchant.size() > 0) {
                    listMerchant.clear();
                }
                idPage = 1;
                sort = NHIEU_NGUOI_NHAT;
                loadData();
                visibleGanNhat(false);
                visibleMoiNhat(false);
                visibleHotNhat(false);
                visibleTabNhieuNguoi(true);
                visibleTabSaleOff(false);
                break;
            case R.id.ll_SaleOff:
                if (listMerchant.size() > 0) {
                    listMerchant.clear();
                }
                idPage = 1;
                sort = KHUYEN_MAI_NHIEU_NHAT;
                loadData();
                visibleGanNhat(false);
                visibleMoiNhat(false);
                visibleHotNhat(false);
                visibleTabNhieuNguoi(false);
                visibleTabSaleOff(true);
                break;

        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Toast.makeText(getApplicationContext(), "Your current location is" + "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //region Tab Sort Visible
    private void visibleGanNhat(boolean visible) {
        if (visible) {

            Img_gannhat.setVisibility(View.VISIBLE);
            txt_GanNhat.setTextColor(0xffff0000);
            ll_sort.setVisibility(View.GONE);
            ll_tab_sort.setBackgroundColor(0x000);
            txt_sort.setText("Gần nhất");
        } else {
            Img_gannhat.setVisibility(View.INVISIBLE);
            txt_GanNhat.setTextColor(0xff000000);
        }
    }

    private void visibleMoiNhat(boolean visible) {
        if (visible) {
            Img_Moinhat.setVisibility(View.VISIBLE);
            txt_Moinhat.setTextColor(0xffff0000);
            ll_sort.setVisibility(View.GONE);
            ll_tab_sort.setBackgroundColor(0x000);
            txt_sort.setText("Mới nhất");
        } else {
            Img_Moinhat.setVisibility(View.INVISIBLE);
            txt_Moinhat.setTextColor(0xff000000);
        }
    }

    private void visibleHotNhat(boolean visible) {
        if (visible) {
            Img_Hotnhat.setVisibility(View.VISIBLE);
            txt_Hotnhat.setTextColor(0xffff0000);
            ll_sort.setVisibility(View.GONE);
            ll_tab_sort.setBackgroundColor(0x000);
            txt_sort.setText("Hot nhất");
        } else {
            Img_Hotnhat.setVisibility(View.INVISIBLE);
            txt_Hotnhat.setTextColor(0xff000000);
        }
    }

    private void visibleTabNhieuNguoi(boolean visible) {
        if (visible) {
            Img_Nhieunguoi.setVisibility(View.VISIBLE);
            txt_Nhieunguoi.setTextColor(0xffff0000);
            ll_sort.setVisibility(View.GONE);
            ll_tab_sort.setBackgroundColor(0x000);
            txt_sort.setText("Nhiều người tham gia nhất");
        } else {
            Img_Nhieunguoi.setVisibility(View.INVISIBLE);
            txt_Nhieunguoi.setTextColor(0xff000000);
        }
    }

    private void visibleTabSaleOff(boolean visible) {
        if (visible) {
            Img_SaleOff.setVisibility(View.VISIBLE);
            txt_SaleOff.setTextColor(0xffff0000);
            ll_sort.setVisibility(View.GONE);
            ll_tab_sort.setBackgroundColor(0x000);
            txt_sort.setText("Khuyến mãi nhiều nhất");
        } else {
            Img_SaleOff.setVisibility(View.INVISIBLE);
            txt_SaleOff.setTextColor(0xff000000);
        }
    }
    //endregion End Tab Sort

    /*------------------------------------------------------------------------------------------------------*/

    //region Menu Visible Tab
    private void visibleTabSort(boolean visible) {
        if (visible) {
            ll_sort.setVisibility(View.VISIBLE);
            ll_tab_sort.setBackgroundColor(0xffe6e6e6);
        } else {
            ll_sort.setVisibility(View.GONE);
            ll_tab_sort.setBackgroundColor(0x000);
        }
    }

    private void visibleTabFilter(boolean visible) {
        if (visible) {
            rv_category.setVisibility(View.VISIBLE);
            ll_tab_filter.setBackgroundColor(0xffe6e6e6);
        } else {
            rv_category.setVisibility(View.GONE);
            ll_tab_filter.setBackgroundColor(0x000);
        }
    }

    private void visibleTabLocation(boolean visible) {
        if (visible) {
            rv_district.setVisibility(View.VISIBLE);
            ll_tab_location.setBackgroundColor(0xffe6e6e6);
        } else {
            rv_district.setVisibility(View.GONE);
            ll_tab_location.setBackgroundColor(0x000);
        }
    }
    //endregion End Menu Tab
}
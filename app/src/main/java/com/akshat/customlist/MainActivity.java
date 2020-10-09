package com.akshat.customlist;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akshat.customlist.models.RoutingData;
import com.akshat.customlist.utils.SortUtil;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.InfinitePlaceHolderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView toolBarTitle;
    LinearLayout emptyLayout;
    EditText searchText;
    ImageView searchIcon;
    ImageView closeIcon;
    LinearLayout dataLayout;
    InfinitePlaceHolderView placeHolderView;
    public RequestQueue rQueue;
    public List<RoutingData> routes = new ArrayList<>();

    LinearLayout llDestPrefix;
    LinearLayout llProtocol;
    LinearLayout llAge;
    LinearLayout llNextHop;

    String URL = "https://gist.githubusercontent.com/git-akshat/5105aaae3df799bdc22257112c922337/raw/d7a29e75664428a35290963ea30f484931965e00/routing_info.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBarTitle = findViewById(R.id.tv_title_toolbar);
        placeHolderView = findViewById(R.id.place_holder_view);
        emptyLayout = findViewById(R.id.empty_layout);
        searchText = findViewById(R.id.search_text);
        searchIcon = findViewById(R.id.search_icon);
        closeIcon = findViewById(R.id.close_icon);
        dataLayout = findViewById(R.id.data_layout);

        llDestPrefix = findViewById(R.id.ll_dest_prefix);
        llAge = findViewById(R.id.ll_age);
        llProtocol = findViewById(R.id.ll_protocol);
        llNextHop = findViewById(R.id.ll_next_hop);

        toolBarTitle.setText("WAN1: Routes");

        rQueue = Volley.newRequestQueue(this);


        dataLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);

        jsonParse(URL);

        searchText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (searchText.getText().toString().length() >= 1) {
                    placeHolderView.removeAllViews();
                }
            }
            return false;
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    placeHolderView.removeAllViews();
                    closeIcon.setVisibility(View.GONE);
                } else if (s.length() > 0) {
                    closeIcon.setVisibility(View.VISIBLE);
                }
                showRoutingList(routes, s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
                showRoutingList(routes, null);
            }
        });

    }

    private void showRoutingList(List<RoutingData> routes, String keyword) {
        placeHolderView.removeAllViews();
        List<RoutingData> routingDataList = new ArrayList<>();

        int count = 0;

        if (routes.size() == 0) {
            dataLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            dataLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

            for (RoutingData r : routes) {
                if (searchText.getText().toString().trim().equals("") || r.getProtocol().toLowerCase().contains(keyword) || r.getDestPrefix().toLowerCase().contains(keyword) || r.getNext_hop().toLowerCase().contains(keyword)) {
                    placeHolderView.addView(new RouteViewItem(r, this));
                    routingDataList.add(r);
                    count++;
                }
            }

            if (count == 0) {
                emptyLayout.setVisibility(View.VISIBLE);
                dataLayout.setVisibility(View.GONE);
            }
        }

        if (searchText.getText().toString().length() > 0) {
            closeIcon.setVisibility(View.VISIBLE);
        } else {
            closeIcon.setVisibility(View.GONE);
        }
        sortData(routingDataList);
    }

    public void jsonParse(String url) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject route = response.getJSONObject(i);
                            String destPrefix = route.getString("dest_prefix");
                            String protocol = route.getString("protocol");
                            int age = route.getInt("age");
                            String nextHop = route.getString("next_hop");
                            routes.add(new RoutingData(destPrefix, protocol, age, nextHop));
                        }
                        showRoutingList(routes, null);
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        });
        rQueue.add(request);
    }

    public void showEmpty() {
        dataLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        hideKeyboard(this, dataLayout);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager
                .HIDE_NOT_ALWAYS);
    }

    private void sortData(final List<RoutingData> routingDataList) {
        llDestPrefix.setOnClickListener(view -> {
            llProtocol.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llDestPrefix.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_blue);
            llAge.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llNextHop.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            SortUtil.sortByDestPrefix(routingDataList);
            showRoutingList(routingDataList, "");
        });

        llProtocol.setOnClickListener(view -> {
            llProtocol.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_blue);
            llDestPrefix.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llAge.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llNextHop.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            SortUtil.sortByProtocol(routingDataList);
            showRoutingList(routingDataList, "");
        });

        llAge.setOnClickListener(view -> {
            llProtocol.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llDestPrefix.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llAge.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_blue);
            llNextHop.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            SortUtil.sortByAge(routingDataList);
            showRoutingList(routingDataList, "");
        });

        llNextHop.setOnClickListener(view -> {
            llProtocol.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llDestPrefix.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llAge.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_grey);
            llNextHop.getChildAt(1).setBackgroundResource(R.drawable.ic_sort_by_alpha_blue);
            SortUtil.sortByNextHop(routingDataList);
            showRoutingList(routingDataList, "");
        });
    }

}

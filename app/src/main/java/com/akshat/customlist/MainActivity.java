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

    String URL = "https://gist.githubusercontent.com/git-akshat/5105aaae3df799bdc22257112c922337/raw/a58eb28b591bae62d501c9df373719c45f11d4b7/routing_info.json";

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
                Toast.makeText(MainActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                showRoutingList(routes, s);
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

    private void showRoutingList(List<RoutingData> routes, CharSequence keyword) {
        placeHolderView.removeAllViews();
        if (routes.size() == 0) {
            dataLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            dataLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

            for (RoutingData r : routes) {
                if (searchText.getText().toString().trim().equals("") || r.getProtocol().contains(keyword) || r.getDestPrefix().contains(keyword) || r.getNext_hop().contains(keyword)) {
                    placeHolderView.addView(new RouteViewItem(r, this));
                }
            }
        }

        if (searchText.getText().toString().length() > 0) {
            closeIcon.setVisibility(View.VISIBLE);
        } else {
            closeIcon.setVisibility(View.GONE);
        }
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

}

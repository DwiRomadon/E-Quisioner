package com.project.e_quisioner.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project.e_quisioner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.NewsAdapterAdmin;
import helper.NewsDataAdmin;
import volley.AppController;
import volley.Config_URL;

public class View_Quis extends AppCompatActivity {

    ListView list;
    List<NewsDataAdmin> newsList = new ArrayList<NewsDataAdmin>();

    private static final String TAG = View_Quis.class.getSimpleName();
    private int offSet = 0;

    NewsAdapterAdmin adapter;
    Handler handler;
    Runnable runnable;

    private ProgressDialog pDialog;

    int socketTimeout  = 30000; // 30 seconds. You can change it
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    public static final String TAG_ID       = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__quis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("View Quis");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        list = (ListView) findViewById(R.id.list_news);
        newsList.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(View_Quis.this, EditQuis.class);
                intent.putExtra(TAG_ID, newsList.get(position).getId());
                startActivity(intent);
            }
        });

        adapter = new NewsAdapterAdmin(View_Quis.this, newsList);
        list.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        callNews();
    }

    private void callNews(){

        pDialog.setMessage("Please Wait.....");
        showDialog();
        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(Config_URL.VIEW_QUIS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hideDialog();
                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);

                                        NewsDataAdmin news = new NewsDataAdmin();
                                        news.setId(obj.getString(TAG_ID));
                                        news.setNomor(obj.getString("nomor"));

                                        news.setTgl(obj.getString("tgl"));
                                        news.setIsi(obj.getString("isi"));

                                        // adding news to news array
                                        newsList.add(news);

                                        Log.d(TAG, "offSet " + offSet);


                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }hideDialog();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        });
        arrReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void onBackPressed()
    {
        Intent a = new Intent(View_Quis.this, AdminActivity.class);
        startActivity(a);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

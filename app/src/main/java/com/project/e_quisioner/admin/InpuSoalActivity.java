package com.project.e_quisioner.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.e_quisioner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.AppController;
import volley.Config_URL;

public class InpuSoalActivity extends AppCompatActivity {

    private static final String TAG = InpuSoalActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    int x=0;
    int y=1;

    private TextView nomer;
    private EditText isiQuis;
    private Button submit, btnHapus;

    int socketTimeout  = 30000; // 30 seconds. You can change it
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inpu_soal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Input Your Question");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        nomer = (TextView) findViewById(R.id.no);
        isiQuis = (EditText) findViewById(R.id.txtIsi);
        submit = (Button) findViewById(R.id.btnSubmit);
        btnHapus = (Button) findViewById(R.id.btnHapus);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int awal = 1;
                //nomer.setText(String.valueOf(awal));
                //nomer.setText(String.valueOf(x));

                String no = nomer.getText().toString();
                String isi = isiQuis.getText().toString();

                if(!no.isEmpty() && !isi.isEmpty()){
                    inputQuis(no, isi);
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Isi Tidak Boleh Kosong !", Toast.LENGTH_LONG)
                            .show();
                }

                isiQuis.setText(null);
            }
        });
        //x += 1;
        //nomer.setText(String.valueOf(x));

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Yakin Ingin Menghapus Semua Data?")
                        .setCancelable(false)//tidak bisa tekan tombol back
                        //jika pilih yess
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteAllData();
                            }
                        })
                        //jika pilih no
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }


    private void inputQuis(final String nomor,final String isiQuis) {
        // Tag used to cancel the request
        String tag_string_req = "req_input";

        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String sukses = jObj.getString("success");
                        Toast.makeText(getApplicationContext(),
                                sukses, Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config_URL.TAG, Config_URL.TAG_INPUT_QUIS);
                params.put("nomor", nomor);
                params.put("quis", isiQuis);
                return params;
            }

        };

        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*
      Method untuk menghapus semua data
     */
    public void deleteAllData(){
        // Tag used to cancel the request
        String tag_string_req = "req_input";

        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String sukses = jObj.getString("success");
                        Toast.makeText(getApplicationContext(),
                                sukses, Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config_URL.TAG, "deleteall");
                return params;
            }

        };

        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed()
    {
        Intent a = new Intent(InpuSoalActivity.this, AdminActivity.class);
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
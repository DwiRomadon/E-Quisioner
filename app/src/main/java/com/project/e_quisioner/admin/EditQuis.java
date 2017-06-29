package com.project.e_quisioner.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.e_quisioner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.AppController;
import volley.Config_URL;

public class EditQuis extends AppCompatActivity {

    private static final String TAG = EditText.class.getSimpleName();

    String ids;
    String tag_json_obj = "json_obj_req";

    private EditText txtNo, txtIsi;
    TextView idd;

    private ProgressDialog pDialog;

    public static final String TAG_ID = "id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Soal");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        txtNo = (EditText)findViewById(R.id.no);
        txtIsi = (EditText)findViewById(R.id.txtIsi);
        idd     = (TextView)findViewById(R.id.idnya);
        txtNo.setEnabled(false);

        ids = getIntent().getStringExtra(TAG_ID);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getDatabyID(ids);
    }

    private void getDatabyID(final String id){

        pDialog.setMessage("Please Wait.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Config_URL.EDIT_QUIS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                hideDialog();
                try {
                    JSONObject obj = new JSONObject(response);

                    String no      = obj.getString("no");
                    String Isi      = obj.getString("isi");
                    String idnya   = obj.getString(TAG_ID);

                    txtNo.setText(no);
                    txtIsi.setText(Html.fromHtml(Isi));
                    idd.setText(idnya);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                Toast.makeText(EditQuis.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(EditQuis.this, View_Quis.class);
        startActivity(a);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
}

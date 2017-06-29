package com.project.e_quisioner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.AppController;
import volley.Config_URL;

/**
 * Created by wolfsoft on 10/11/2015.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {

    private View view;
    private static final String TAG = SigninFragment.class.getSimpleName();
    private TextInputLayout etNama , etEmail , etPass , etAlamat ;
    private TextView Register ;
    private ProgressDialog pDialog;
    private Context context;

    int socketTimeout = 30000; // 30 seconds. You can change it
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_signup, container, false);
        context = getContext();
        pDialog = new ProgressDialog(context);
        etNama = (TextInputLayout) view.findViewById(R.id.etFullname);
        etEmail = (TextInputLayout) view.findViewById(R.id.etEmail);
        etPass = (TextInputLayout) view.findViewById(R.id.etPass);
        etAlamat = (TextInputLayout) view.findViewById(R.id.etAlamat);
        Register = (TextView) view.findViewById(R.id.create);
        Register.setOnClickListener(this);

        return view;

    }


    @Override
    public void onClick(View v) {
        if (v== Register){
            String username = etNama.getEditText().getText().toString();
            String email = etEmail.getEditText().getText().toString();
            String password = etPass.getEditText().getText().toString();
            String alamat = etAlamat.getEditText().getText().toString();

            if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !alamat.isEmpty()){
                registerUser(username, email, password, alamat);
            }else{
                //Prompt user to enter credential
                Toast.makeText(getActivity(),
                        "Masukan Email atau Password Anda !!",
                        Toast.LENGTH_LONG).show();
            }
            etNama.getEditText().setText(null);
            etEmail.getEditText().setText(null);
            etAlamat.getEditText().setText(null);
            etPass.getEditText().setText(null);
        }
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String username,final String email,final String password, final String alamat) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        //message if succes register
                        String berhasil = jObj.getString("berhasil");
                        Toast.makeText(getActivity(),
                                berhasil, Toast.LENGTH_LONG).show();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config_URL.TAG, Config_URL.TAG_REGISTER);
                params.put(Config_URL.username, username);
                params.put(Config_URL.email, email);
                params.put(Config_URL.password, password);
                params.put(Config_URL.address, alamat);
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
}


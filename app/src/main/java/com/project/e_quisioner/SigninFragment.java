package com.project.e_quisioner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.project.e_quisioner.admin.AdminActivity;
import com.project.e_quisioner.user.UserBiasa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import helper.SessionManager;
import volley.AppController;
import volley.Config_URL;


/**
 * Created by wolfsoft on 10/11/2015.
 */
public class SigninFragment extends Fragment implements View.OnClickListener{

    String TAG = SigninFragment.class.getSimpleName();
    private View view;
    private TextView login;
    private TextInputLayout etEmail, etPass;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Context context;

    SharedPreferences prefs;

    private String level = "";
    private String id="";
    private String username = "";
    private String emailnya = "";

    int socketTimeout = 30000; // 30 seconds. You can change it
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);
        context = getContext();
        pDialog = new ProgressDialog(context);
        etEmail = (TextInputLayout) view.findViewById(R.id.etEmail);
        etPass = (TextInputLayout) view.findViewById(R.id.etPass);
        login = (TextView) view.findViewById(R.id.login);
        login.setOnClickListener(this);

        prefs = this.getActivity().getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);

        // Session manager
        session = new SessionManager(context);

        // Check if user is already logged in or not
        id             = prefs.getString("id","");
        level          = prefs.getString("level", "");
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            if(level.contains("1")){
                Intent intent = new Intent(context, AdminActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }else if (level.contains("2")){
                Intent intent = new Intent(context, UserBiasa.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        }

        return view;

    }

    @Override
    public void onClick(View v) {
        if (v == login){
            String email = etEmail.getEditText().getText().toString();
            String password = etPass.getEditText().getText().toString();

            //Check for empty data in the form
            if(email.trim().length() > 0 && password.trim().length() > 0){
                checkLogin(email, password);
            }else{
                //Prompt user to enter credential
                Toast.makeText(getActivity(),
                        "Masukan Email atau Password Anda !!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * fungsi to veryfy login details in mysql db
     **/

    private void checkLogin(final String email, final String password){

        //Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Login, Please Wait.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        username = jObj.getString("username");
                        emailnya = jObj.getString("email");
                        level = jObj.getString("level");
                        id = jObj.getString("id");
                        //user successsfully
                        //Create login Session
                        session.setLogin(true);

                        storeRegIdinSharedPref(context,id, username, email, level);
                        String theRole = level;
                        if(theRole.equals("1")){
                            //Lauch to main activity
                            Intent i = new Intent(context,
                                    AdminActivity.class);
                            i.putExtra("iduser", id);
                            i.putExtra("username", username);
                            i.putExtra("email",email);
                            i.putExtra("level",level);
                            startActivity(i);
                        }else if(theRole.equals("2")) {
                            //Lauch to main activity
                            Intent i = new Intent(context,
                                    UserBiasa.class);
                            i.putExtra("iduser", id);
                            i.putExtra("username", username);
                            i.putExtra("email",email);
                            i.putExtra("level",level);
                            startActivity(i);
                        }

                    }else {
                        String error_msg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
                                error_msg, Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Login Error : " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config_URL.TAG, Config_URL.TAG_LOGIN);
                params.put(Config_URL.email, email);
                params.put(Config_URL.password, password);

                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void storeRegIdinSharedPref(Context context,String iduser,String usernme, String email, String level) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("iduser", iduser);
        editor.putString("username", usernme);
        editor.putString("email", email);
        editor.putString("level", level);
        editor.commit();
    }

}

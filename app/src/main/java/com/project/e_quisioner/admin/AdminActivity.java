package com.project.e_quisioner.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.e_quisioner.LoginSignup;
import com.project.e_quisioner.R;

import helper.SessionManager;

public class AdminActivity extends AppCompatActivity {

    private SessionManager session;
    TextView nameText;
    TextView emailText;
    TextView idText, lvl;
    CircularImageView gambarnya;
    ImageButton btnBuatMateri, editMateri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        nameText             = (TextView)findViewById(R.id.nameText);
        emailText            = (TextView)findViewById(R.id.emailText);
        idText            = (TextView)findViewById(R.id.idText);
        gambarnya = (CircularImageView)findViewById(R.id.imageProfile);
        lvl = (TextView)findViewById(R.id.lvl);

        btnBuatMateri = (ImageButton)findViewById(R.id.btnBuatMateri);
        btnBuatMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(AdminActivity.this, InpuSoalActivity.class);
                startActivity(a);
                finish();
            }
        });

        editMateri = (ImageButton)findViewById(R.id.btn3);
        editMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(AdminActivity.this, View_Quis.class);
                startActivity(a);
                finish();
            }
        });

        // Session manager
        session = new SessionManager(getApplicationContext());

        //Session Login
        if(session.isLoggedIn()){
            gambarnya.setBorderColor(getResources().getColor(R.color.colorbar));
            gambarnya.setBorderWidth(10);
            // Add Shadow with default param
            gambarnya.addShadow();
            // or with custom param
            gambarnya.setShadowRadius(20);
            gambarnya.setShadowColor(Color.parseColor("#34495E"));


            SharedPreferences prefs = getSharedPreferences("UserDetails",
                    Context.MODE_PRIVATE);


            String username = prefs.getString("username","");
            String email    = prefs.getString("email", "");
            String level    = prefs.getString("level", "");
            final String idusernya   = prefs.getString("iduser", "");

            nameText.setText(username);
            emailText.setText(email);
            idText.setText(idusernya);
            lvl.setText(level);
            lvl.setVisibility(View.INVISIBLE);
            idText.setVisibility(View.INVISIBLE);
        }
    }

    private void logoutUser() {
        session.setLogin(false);
        session.setSkip(false);
        session.setSessid(0);

        // Launching the login activity
        Intent intent = new Intent(AdminActivity.this, LoginSignup.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
        exit();
    }

    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yakin Ingin Keluar Dari Aplikasi?")
                .setCancelable(false)//tidak bisa tekan tombol back
                //jika pilih yess
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logoutUser();
                        finish();
                    }
                })
                //jika pilih no
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}

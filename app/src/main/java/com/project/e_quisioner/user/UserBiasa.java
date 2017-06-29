package com.project.e_quisioner.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.e_quisioner.LoginSignup;
import com.project.e_quisioner.R;

import helper.SessionManager;

public class UserBiasa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_biasa);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session manager
        session = new SessionManager(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        TextView txtNama = (TextView)findViewById(R.id.sumpahininama);
        TextView txtEmail = (TextView)findViewById(R.id.sumpahiniemail);
        //Session Login
        if(session.isLoggedIn()){
            aksesUserLogin();

            String username = prefs.getString("username","");
            String email    = prefs.getString("email", "");
            txtNama.setText(username);
            txtEmail.setText(email);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_biasa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            session.setLogin(false);
            session.setSkip(false);
            session.setSessid(0);

            // Launching the login activity
            Intent intent = new Intent(UserBiasa.this, LoginSignup.class);
            startActivity(intent);
            finish();
        }/* else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    //drawer user login
    public void aksesUserLogin(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_user_biasa, null);
        navigationView.addHeaderView(header);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        TextView nameText             = (TextView)header.findViewById(R.id.nameText);
        TextView emailText            = (TextView)header.findViewById(R.id.emailText);
        TextView idText            = (TextView)header.findViewById(R.id.idText);
        TextView leveling             = (TextView)header.findViewById(R.id.lvl);

        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);


        String username = prefs.getString("username","");
        String email    = prefs.getString("email", "");
        String level    = prefs.getString("level", "");
        final String idusernya   = prefs.getString("iduser", "");

        nameText.setText(username);
        emailText.setText(email);
        idText.setText(idusernya);
        leveling.setText(level);
        leveling.setVisibility(View.INVISIBLE);
        idText.setVisibility(View.INVISIBLE);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void logoutUser() {
        session.setLogin(false);
        session.setSkip(false);
        session.setSessid(0);

        // Launching the login activity
        Intent intent = new Intent(UserBiasa.this, LoginSignup.class);
        startActivity(intent);
        finish();
    }

}

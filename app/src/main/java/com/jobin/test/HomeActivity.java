package com.jobin.test;

import android.app.ActivityOptions;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.jobin.test.AdminPanel.AdminEnquiry;
import com.jobin.test.navigation.FirstFragment;
import com.jobin.test.navigation.FourthFragment;
import com.jobin.test.navigation.SecondFragment;
import com.jobin.test.navigation.ThirdFragment;
import com.jobin.test.profile.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Data data;
    ArrayList<Data> List = new ArrayList<>();
    String username;
    FragmentManager fragmentManager;
    Fragment fragment;
    BottomNavigationView bottomNavigation;
    String sharedimage,sharedtext;
    String profile_name,profile_image;
    private static String KEY_USERNAME = "username";
    TextView txt_profile_name,nav_name;
    NetworkImageView nav_image;
    private static String UPLOAD_URL = "http://techpakka.com/android/user_details.php?";
    ProgressDialog progress;
    SharedPreferences pref;
    FragmentTransaction transaction;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        fetchData();
        nav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);

                View sharedView = nav_image;
                String transitionName = getString(R.string.blue);
                String transitiontxtName = getString(R.string.texttransform);
                Pair<View, String> p1 = Pair.create((View)nav_image, transitionName);
                Pair<View, String> p2 = Pair.create((View)nav_name, transitiontxtName);

                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this,p1,p2);
                i.putExtra("imageurl",sharedimage);
                i.putExtra("sharedtext",sharedtext);
                startActivity(i, transitionActivityOptions.toBundle());
            }
        });


    }

    private void initViews() {
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("check","hai");
        editor.apply();
        username = pref.getString("username","");




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        nav_image = (NetworkImageView) hView.findViewById(R.id.nav_image);
        nav_name = (TextView) hView.findViewById(R.id.nav_name);
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_menu);
        fragmentManager = getSupportFragmentManager();
        fragment = new FirstFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container,fragment).commit();


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.menu_home:
                        Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_notifications:
                        fragment = new SecondFragment();
                        break;
                    case R.id.menu_search:
                        fragment = new ThirdFragment();
                        break;
                    case R.id.menu_news:
                        fragment = new FourthFragment();
                        break;
                }
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_enquiry) {

          Intent intent = new Intent(HomeActivity.this, AdminEnquiry.class);
            startActivity(intent);

        }else if (id == R.id.nav_logout) {

            Boolean isLoggedIn = pref.getBoolean("loggedin",true);
            if (isLoggedIn){
                Toast.makeText(this, "sign out", Toast.LENGTH_SHORT).show();
                editor.clear().apply();
                editor.apply();
                finish();
            }
            else{
                Toast.makeText(this, "not sign in", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void fetchData(){
        Boolean isLoggedIn = pref.getBoolean("loggedin",true);
        if (isLoggedIn){
            progress = ProgressDialog.show(HomeActivity.this,"Please wait...","checking",false,false);
            progress.setCanceledOnTouchOutside(true);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){

                JsonArrayRequest request = new JsonArrayRequest(UPLOAD_URL + "username=" + username + "&type=" + "select",
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progress.dismiss();
                                for (int i=0;i<response.length();i++){
                                    try{
                                        JSONObject obj = response.getJSONObject(i);
                                        data =new Data();
                                        data.setName(obj.getString("username"));
                                        data.setImg_url(obj.getString("image"));
                                        List.add(data);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Data data2 = List.get(0);

                                nav_name.setText(data2.getName());
                                nav_image.setImageUrl(data2.getImg_url(),imageLoader);
                                sharedimage = data2.getImg_url();
                                sharedtext = data2.getName();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                    }
                });
                AppController.getInstance().addToRequestQueue(request);

            }
            else Toast.makeText(this, "no network connection", Toast.LENGTH_SHORT).show();
        }

    }





}

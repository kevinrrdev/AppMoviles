package com.example.kevin.appmoviles.Activity;


import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.kevin.appmoviles.Fragment.MapMainFragment;
import com.example.kevin.appmoviles.R;
import com.example.kevin.appmoviles.gps.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private TextView tvNavUserNames,tvNavUserCharge;
    private View navHeader;
    GoogleMap mGoogleMap;
    SupportMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this,mDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        setStatusBarTranslucent(true);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        tvNavUserNames = navHeader.findViewById(R.id.tvNavUserNames);
        tvNavUserCharge = navHeader.findViewById(R.id.tvNavUserCharge);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCustomer);
        mMapFragment.getMapAsync(this);
        //setFragment(0);

    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }



    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            ContextThemeWrapper ctw= new ContextThemeWrapper(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            AlertDialog.Builder dialog= new AlertDialog.Builder(ctw);
            dialog.setTitle("Salir");
            dialog.setMessage("Estas seguro que quieres salir?");
            dialog.setNegativeButton("No", null);
            dialog.setNeutralButton("Cancel",null);
            dialog.setPositiveButton(R.string.option_yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) { MainActivity.this.finish(); }
            });
            AlertDialog ad= dialog.create();
            ad.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_task_list) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_drawer, menu);
        return true;
    }

    public void setFragment(int position){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position){
            case 0:
                if (position!=0){

                }
                // set check options on navigation view
                navigationView.getMenu().getItem(position).setChecked(true);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction= fragmentManager.beginTransaction();
                MapMainFragment MapMainFragment= new MapMainFragment();
               // fragmentTransaction.replace(R.id.content_fragment, MapMainFragment);
                //fragmentTransaction.commit();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        GPSTracker gps= new GPSTracker(this);
        if(gps.canGetLocation()) {
            double lat = gps.getLatitude();
            double lon = gps.getLongitude();
            LatLng origin = new LatLng(lat, lon);

            setCameraPosition(origin,15);
        }
    }
    private void setCameraPosition(LatLng target,float defaultZoom){

        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.zoom(defaultZoom);
        builder.target(target);

        this.mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
    }
}

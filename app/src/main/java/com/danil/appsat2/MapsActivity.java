package com.danil.appsat2;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**************Bind Service imports***********/
import com.danil.appsat2.PosCalcService.PCSBinder;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.PolylineOptionsCreator;

public class MapsActivity extends FragmentActivity {

    Marker marker; //satellite icon/marker on map

    //Variables to communicate with service
    PosCalcService PCSservice;
    boolean isBound = false;
    PolylineOptions sat_trajectory_options; //sat projection
    Handler updatemap, initmap, orbit_trajectory; //handler to update map
    Runnable updatemapTask, initmapTask, orbit_trajectoryTask; //runnables for using methods that require service access
    private GoogleMap map; // Might be null if Google Play services APK is not available.
    Bitmap b; //satelitee icon for map
    TextView maptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //put satname on top of activity
        setTitle(getsatname());

        //init map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        maptext = (TextView) findViewById(R.id.mapsTV); //set up map text view

        //create custom marker
        Bitmap src = BitmapFactory.decodeResource(this.getResources(),R.drawable.satellite); //load icon
        b = Bitmap.createScaledBitmap(src, 100, 100 ,false); //scale icon

        //establish connection with Service
        bindService(createIntents(), PCSconnection, Service.BIND_AUTO_CREATE);

        //check if Service is bound
        if(isBound = true){
            Toast.makeText(this, "PosCalcService bound", Toast.LENGTH_SHORT).show();
        }

    }

    private Bundle retrieveBundleIntents() {
        Bundle bundle = getIntent().getExtras();
        return bundle;
    }

    private Intent createIntents() {
        Intent i = new Intent(this, PosCalcService.class);
        return i;
    }

    private CharSequence getsatname() {
        String name = retrieveBundleIntents().getString("SatName");
        return name;
    }

    @Override
    protected void onStop() {
        super.onStop();
        updatemap.removeCallbacks(updatemapTask); //stops retrieving data from service
        finish();
    }

    /**********************************Binding with Service methods****************************/
    private ServiceConnection PCSconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PCSBinder binder = (PCSBinder) iBinder;
            PCSservice = binder.getService();
            isBound = true;

            initmap = new Handler();
            initmapTask = new Runnable() { //initialise marker in map (first calculated position
                @Override
                public void run() {
                    markerControl(PCSservice.latlng(retrieveBundleIntents())); //create a marker at given position
                }
            };
            initmapTask.run(); //run the actual initialisation of the marker
            /*
            orbit_trajectory = new Handler();
            orbit_trajectoryTask = new Runnable() {
                @Override
                public void run() {
                    sat_trajectory_options = new PolylineOptions()              //create polyline options for a trajectory poly line
                            .addAll(PCSservice.orbit_trajectory(retrieveBundleIntents(), 5))
                            .geodesic(true);
                }
            };
            orbit_trajectory.post(orbit_trajectoryTask);
*/
            updatemap = new Handler();
            updatemapTask = new Runnable() { //move marker to new coordinates
                @Override
                public void run() {

                    Log.i("HANDLER", "Handler running"); //DEBUG
                    moveMarker(PCSservice.latlng(retrieveBundleIntents()), PCSservice.getVelocity()); //update marker to new position, diplaying it's speed and pos
                    Log.i("HANDLER", "PCSService : " + PCSservice.latlng(retrieveBundleIntents()).toString()); //DEBUG

                    updatemap.postDelayed(updatemapTask, 100); //run handler every 5000 milliseconds
                }

                ;//run method end
            };//updatemapTask end
            updatemap.post(updatemapTask); //actually run the updatemapTask

        }



        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;

        }
    };

    //creates satelite by setting a cusotm bitmap
    private void markerControl(LatLng coor) { //moves marker to desired location
        if(marker!= null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(coor) //sets marker at calculated position
                .icon(BitmapDescriptorFactory.fromBitmap(b)));    //sets custom icon for marker

    }

    //used to "move" the sat icon over the map
    private void moveMarker(LatLng newcoor, double velocity){
        marker.remove();
        markerControl(newcoor);
        updateText(newcoor, velocity);
        map.moveCamera(CameraUpdateFactory.newLatLng(newcoor));

    }

    //updates text that displays lat and long on the screen
    private void updateText(LatLng coor, double velocity){
        maptext.setTextColor(Color.WHITE);
        maptext.setText("Velocity : " + velocity + " [km/s]\nLatitude : " + coor.latitude + "\nLongitude : " + coor.longitude);
    }

    //switches map type
    public void onRadioButtonClick(View v){

        //is the button checked
        boolean checked = ((RadioButton) v).isChecked();

        //check wich radio button is checked
        switch(v.getId()) {
            case R.id.rb_hybrid:
                if(checked){
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                break;
            case R.id.rb_normal:
                if(checked){
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.rb_sat:
                if(checked){
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                break;
            case R.id.rb_terrain:
                if(checked){
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
                break;
        }
    }
}
package com.danil.appsat2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.gano.jsattrack.Utilities.Utilities.Sidereal;
import com.gano.jsattrack.Utilities.Utilities.Time;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.me.chiandh.library.SDP4;
import uk.me.chiandh.library.SDP4InvalidNumException;
import uk.me.chiandh.library.SDP4NoLineOneException;

public class PosCalcService extends Service {

    /****************PosCalc variables**************/
    double[] coor; //array use in LatLngCalc
    LatLng LatLongCalc_result; //result of LatLngCalc
    List<LatLng> orbit_trajectory_results; //orbit_trajectory results
    private double x,y,z,latitude,longitude, rsqrt, cos,tanlat,r,xy2, xy2sqrt, gmst, velocity; //vas for LatLngCalc
    private long init_time,intermidiate_time; //variables used in orbit_trajectory()
    protected double a = 6378.135; //radius of the earth in km
    protected long HtoMillis = 3600000; //1h in milliseconds
    protected long MtoMillis = 60000; //1 minute in milliseconds
    protected double HtoM = 60; //1H in minutes
    String strDate; //Nice representation of time HH:mm:ss

    private final IBinder PCSbinder = new PCSBinder();

    public class PCSBinder extends Binder {
        PosCalcService getService(){
            return PosCalcService.this;
        }
    }

    public PosCalcService(){}

    public LatLng LatLongCalc(Bundle bundle, long current_sys_time ) {

        String Line1 = bundle.getString("TLE_Line1");
        String Line2 = bundle.getString("TLE_Line2");

        //Create instance of SDP4 class
        SDP4 sdp4 = new SDP4();

        //get current date in hh:mm:ss ()for map snippet afterwards
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        strDate = sdfDate.format(date);

        //get Julian Date
        double juldate = (current_sys_time) / 86400000. + 587.5 - 10000.;


        //get modified julian date (Julian date minus 2400000.5) (UT)
        Time t = new Time();
        //"init" of time -> can get mjd from this class
        t.update2CurrentTime();
        double mjd = t.getMJD();


        //SDP4 Init() method
        try {
            sdp4.Init();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //SDP4 ReadNorad12 modified method
        try {
            sdp4.ModifiedReadNorad12(Line1, Line2);
        } catch (SDP4NoLineOneException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SDP4InvalidNumException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        sdp4.GetPosVel(juldate);
        coor = sdp4.itsR;
        //scale coordiantes
        x = coor[0]*1000000;
        y = coor[1]*1000000;
        z = coor[2]*1000000;

        //get latitude
        xy2 = (x*x) +(y*y);
        xy2sqrt = Math.sqrt(xy2);
        tanlat = z/xy2sqrt;
        latitude= Math.atan(tanlat)* 180 / Math.PI;


        //get gmst for ECI to ECEF conversion
        gmst = Sidereal.Greenwich_Mean_Sidereal_Deg(mjd);

        //get longitude

        //longitude = ((Math.atan(y/x)));//*180/Math.PI)-gmst;
        longitude = Math.toDegrees(Math.atan2(y,x));
        //Make longitude positive if negative
        if (longitude < 0){
            longitude = longitude +360;
        }
        //ECI TO ECEF
        longitude = longitude -gmst;

        //Get Altitude (a = radius of the earth)
        rsqrt = (x*x) +(y*y) + (z*z);
        r = Math.sqrt(rsqrt)- a;

        //calculate velocity
        velocity = Math.sqrt(Math.pow(sdp4.itsV[0], 2)*Math.pow(sdp4.itsV[1], 2)*Math.pow(sdp4.itsV[2], 2));

        //results
        LatLongCalc_result = new LatLng(latitude,longitude);
        return LatLongCalc_result;
    }

    //return satellites current position
    public LatLng latlng(Bundle bundle){
        LatLng coor = LatLongCalc(bundle, System.currentTimeMillis());
        return coor;
    }

    //return list of satelllites positions every "step" minutes 12h in the past and 12h in future
    public List<LatLng> orbit_trajectory(Bundle bundle, int step){

        init_time = System.currentTimeMillis()- 12*HtoMillis; //init time 12h before current time
        long loop_init = (long) (12*HtoM/step); //number of iterations of step to get to 720min (12h)

        for(long i = -loop_init; i<loop_init; i++){ //run loop from -12h to 12h by "step" steps
            init_time = init_time + (i*step)*MtoMillis; //init_time + 5 minutes at each iteration
            orbit_trajectory_results.add(LatLongCalc(bundle, init_time));
        }
    return orbit_trajectory_results;
    }


    //will return time of last latlng object, so must be called after latlng method
    public String getTime(){
        return strDate;
    }

    public double getVelocity() { return velocity; }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service", "onBind method");
        return PCSbinder;
    }
}

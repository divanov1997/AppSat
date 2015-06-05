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

import uk.me.chiandh.library.SDP4;
import uk.me.chiandh.library.SDP4InvalidNumException;
import uk.me.chiandh.library.SDP4NoLineOneException;

public class PosCalcService extends Service {

    /****************PosCalc variables**************/
    double[] coor;
    private double x,y,z,latitude,longitude, rsqrt, cos,tanlat,r,xy2, xy2sqrt, gmst, velocity;
    protected double a = 6378.135; //radius of the earth in km
    String strDate;

    private final IBinder PCSbinder = new PCSBinder();

    public class PCSBinder extends Binder {
        PosCalcService getService(){
            return PosCalcService.this;
        }
    }

    public PosCalcService(){}

    public void LatLongCalc(Bundle bundle) {

        String Line1 = bundle.getString("TLE_Line1");
        String Line2 = bundle.getString("TLE_Line2");

        //Create instance of SDP4 class
        SDP4 sdp4 = new SDP4();

        //get current date in hh:mm:ss ()for map snippet afterwards
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        strDate = sdfDate.format(date);

        //get Julian Date
        double juldate = (System.currentTimeMillis()) / 86400000. + 587.5 - 10000.;


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
        //TESTS


        //Get Altitude (a = radius of the earth)
        rsqrt = (x*x) +(y*y) + (z*z);
        r = Math.sqrt(rsqrt)- a;

        //calculate velocity
        velocity = Math.sqrt(Math.pow(sdp4.itsV[0], 2)*Math.pow(sdp4.itsV[1], 2)*Math.pow(sdp4.itsV[2], 2));

    }

    public LatLng latlng(Bundle bundle){
        LatLongCalc(bundle);
        LatLng coor = new LatLng(latitude,longitude);
        return coor;
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

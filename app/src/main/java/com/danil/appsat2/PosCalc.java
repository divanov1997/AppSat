package com.danil.appsat2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gano.jsattrack.Utilities.Utilities.Sidereal;
import com.gano.jsattrack.Utilities.Utilities.Time;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.me.chiandh.library.SDP4;
import uk.me.chiandh.library.SDP4InvalidNumException;
import uk.me.chiandh.library.SDP4NoLineOneException;

public class PosCalc extends Activity{
	
	double[] coor;
	double x,y,z,latitude,longitude, rsqrt, cos,tanlat,r,xy2, xy2sqrt, gmst;
	protected double a = 6378.135; //radius of the earth in km
	String xcor,ycor,zcor,acor,bcor,rcor;



    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos_calc);


        // show loading spinner
        ProgressDialog progDial = new ProgressDialog(PosCalc.this);
        progDial.setTitle("Loading...");
        progDial.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDial.show();

		TextView textView = (TextView) findViewById(R.id.displaymessagetextview);
		TextView textView2 = (TextView) findViewById(R.id.displaymessagetextview2);
		TextView textView3 = (TextView) findViewById(R.id.displaymessagetextview3);
		/*
        TextView textView4 = (TextView) findViewById(R.id.displaymessagetextview4);
		TextView textView5 = (TextView) findViewById(R.id.displaymessagetextview5);
		TextView textView6 = (TextView) findViewById(R.id.displaymessagetextview6);
		TextView textView7 = (TextView) findViewById(R.id.displaymessagetextview7);
		TextView textView8 = (TextView) findViewById(R.id.displaymessagetextview8);
		TextView textView9 = (TextView) findViewById(R.id.displaymessagetextview9);
		*/
		//create instance of Times class
		
		
		Bundle extras = getIntent().getExtras();
	    	if (extras == null) {
	    		return;
	    	}
	    String Line1 = extras.getString("TLE_Line1");
	    String Line2 = extras.getString("TLE_Line2");
	    String satname = extras.getString("SatName");

		//Create instace of SDP4 class
		SDP4 sdp4 = new SDP4();
		
		//get current date in hh:mm:ss ()for map snippet afterwards
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String strDate = sdfDate.format(date);

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
		int l = coor.length;
		x = coor[0]*1000000;
		y = coor[1]*1000000;
		z = coor[2]*1000000;

		String length = Integer.toString(l);
		

		xcor = Double.toString(x);
		ycor = Double.toString(y);
		zcor = Double.toString(z);

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
        r = Math.sqrt(rsqrt);


    //display everything
        /*
		textView.setTextSize(40);
		textView.setText(satname);
		textView2.setText("Satellite Designator :\n"+sdp4.itsDesignator);
		textView3.setText(length);
        textView4.setText("X : "+ xcor);
        textView5.setText("Y : "+ ycor);
        textView6.setText("Z : "+ zcor);textView7.setText("Lat : "+Double.toString(latitude));textView8.setText("Long : "+Double.toString(longitude));
        textView9.setText("Alt : "+Double.toString(r-a));

		*/











		//intent to start map, and load coordinates
        Intent intent = new Intent(PosCalc.this, MapsActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("satname", satname);
        intent.putExtra("strDate", strDate);
        startActivity(intent);
        finish();
		
	}

}//end
package com.danil.appsat2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
        //TextView to notify user about files
        final TextView tv = (TextView) findViewById(R.id.splash_tv);
		//Thread that sleeps for 1 second, then starts activity
		Thread timer = new Thread(){
			public void run() {

                try {
                    verifyFiles();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent openMainActivity = new Intent(Splash.this, Menu.class);
                startActivity(openMainActivity);
            }

            private void verifyFiles() throws InterruptedException {
                //list of all necesary files
                String siscat[] = {"tle-new.txt","stations.txt","visual.txt","1999-025.txt","iridium-33-debris.txt",
                        "cosmos-2251-debris.txt","2012-044.txt"};
                //run loop once per file
                for(int i = 0; i<siscat.length; i = i+1){
                    File file = new File(Splash.this.getFilesDir(), siscat[i]);
                    if(!file.exists()){
                        Intent service = new Intent(Splash.this, RefreshTLE.class);
                        startService(service);
                        Thread.sleep(1000);
                    }
                }
            }


        };
			timer.start();
			
		}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	

}

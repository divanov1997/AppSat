package com.danil.appsat2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//Thread that sleeps for 1 second, then starts activity
		Thread timer = new Thread(){
			public void run(){
			try {
				sleep(1000);
			
			}catch (InterruptedException e)	{
				e.printStackTrace();
				}finally{
                    Intent openMainActivity = new Intent(Splash.this, AndroidDashboardDesignActivity.class);
					startActivity(openMainActivity);
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

package com.danil.appsat2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class RefreshTLE extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String Refresh_TLE = "com.danil.appsat2.action.REFRESH_TLE";
    Handler mHandler;

    public RefreshTLE() {
        super("RefreshTLE");
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Special Interest Satellites category
        mHandler.post(new DisplayToast(this, "RefreshTLE is running."));
        String siscatURL = "http://www.celestrak.com/NORAD/elements/";
        String siscat[] = {"tle-new.txt","stations.txt","visual.txt","1999-025.txt","iridium-33-debris.txt",
                "cosmos-2251-debris.txt","2012-044.txt"};

        int i = 0;
        while(i < siscat.length){
            File existfile = new File(this.getFilesDir(), siscat[i]);
            if(!existfile.exists()){
                existfile.delete();
            }
            File newfile = new File(this.getFilesDir(), siscat[i]);
            updateTLE(siscatURL,siscat,newfile, i);
            i++;
        }//end while
        mHandler.post(new DisplayToast(this, "TLEs updated."));

        File test = new File(RefreshTLE.this.getFilesDir(),"tle-new.txt");
        mHandler.post(new DisplayToast(this, getFilesDir().toString()));
        stopSelf();
    }//end onHandleIntent

    private void updateTLE(String siscatURL, String[] siscat, File newfile, int i) {
        ConnectivityManager connecManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connecManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mCelData = connecManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(mWifi.isConnected()){
            getTLE(siscatURL,siscat,newfile,i);
        }else{
            if(mCelData.isConnected()){
                getTLE(siscatURL,siscat,newfile,i);
            }else{
                mHandler.post(new DisplayToast(this, "Connection to "+siscatURL+siscat+" failed"));
                stopSelf();
            }
        }
    }


    /**
 * Fetches TLE data from website and copies each contents of each category to a file.
 * @param webpage Beginning of Celestrak URL
 * @param URLEndArray String array containing the end of each url, specifiying the precise category
 * @param file File to which the contents of the webpage are copied
 * @param i loop counter from while loop to download the contents of a webpage to the corresponding file
 * */
    private void getTLE(String webpage,String[] URLEndArray,File file, int i) {

        try{
            //open corresponding webpage with TLEs
            String webPage = webpage + URLEndArray[i];
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);

            //write TLE to file
            FileWriter fwrite = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fwrite);

            String aLine = null;
            while((aLine = in.readLine()) != null){
                //Process each line and add output to Dest.txt file
                out.write(aLine);
                out.newLine();
            }

            //close in and out buffers
            in.close(); out.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }  //end catch ioexception
    }//updateTLE

    public class DisplayToast implements Runnable {
        private final Context mContext;
        String mText;

        public DisplayToast(Context mContext, String text){
            this.mContext = mContext;
            mText = text;
        }

        @Override
        public void run() {
            Toast.makeText(mContext,mText,Toast.LENGTH_SHORT).show();
        }
    }
}//end refreshTLE entends IntentService


package com.danil.appsat2;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

/**
 * Created by Danil on 3/6/15.
 */
public class Utilities extends Activity{

    //Reads a file, but only keeps every third line and puts it in a string List
    public ArrayList<String> ReadFile(String filename, int firstline){

        String strLine;
        ArrayList<String> arrayList = new ArrayList<String>();

        try{
            // Open the file
            AssetManager am = getAssets();
            InputStream inputStream = am.open(filename);

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            LineNumberReader reader = new LineNumberReader(br);

            //read every third line
            while ((strLine = reader.readLine()) != null){
                if ((reader.getLineNumber()+3-firstline) % 3 == 0){
                    arrayList.add(strLine);
                }
            }

            //Close the input stream
            in.close();


        }catch (Exception e){	//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        return arrayList;

    }
}

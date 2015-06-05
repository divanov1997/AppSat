package com.danil.appsat2;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class HundredBrightest extends ListActivity {
    /** Called when the activity is first created. */
   
	
	
	
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayList<String> category = ReadFile("brightest.txt", 1);
        
        setListAdapter(new ArrayAdapter<String>(HundredBrightest.this,
				android.R.layout.simple_list_item_1, category));
        
    }
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
   public ArrayList<String> SatName(){ 
     
     String strLine;
	 ArrayList<String> stringList = new ArrayList<String>();

    try{
        // Open the file
        AssetManager am = getAssets();
        InputStream inputStream = am.open("brightest.txt");
 
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(inputStream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        LineNumberReader reader = new LineNumberReader(br);
 
         //read every third line
        while ((strLine = reader.readLine()) != null){
	        if ((reader.getLineNumber()+2) % 3 == 0){
		         stringList.add(strLine);
	        }
        }

        //Close the input stream
        in.close();
 
 
    }catch (Exception e){	//Catch exception if any
    System.err.println("Error: " + e.getMessage());
    }
 
    return stringList;
 
   }
   public ArrayList<String> Line1(){
	     
	     String strLine;
		 ArrayList<String> Line1List = new ArrayList<String>();
		
	 try{
	 
	 // Open the file that is the first 
	 // command line parameter

         AssetManager am = getAssets();
         InputStream inputStream = am.open("brightest.txt");
	 // Get the object of DataInputStream
	 DataInputStream in = new DataInputStream(inputStream);
	 BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 LineNumberReader reader = new LineNumberReader(br);
	 
	 //read every third line
	 while ((strLine = reader.readLine()) != null){
		 if ((reader.getLineNumber()+1) % 3 == 0){
			 Line1List.add(strLine);
		 }
	 }

	 //Close the input stream
	 in.close();
	 
	 
	   }catch (Exception e){	//Catch exception if any
	 System.err.println("Error: " + e.getMessage());
	 }
	 
	return Line1List;
	 
	 }   
   public ArrayList<String> Line2(){ 
	     
	     String strLine;
		 ArrayList<String> Line2List = new ArrayList<String>();
		 
	 try{
	 
	 // Open the file that is the first 
	 // command line parameter

         AssetManager am = getAssets();
         InputStream inputStream = am.open("brightest.txt");
	 
	 // Get the object of DataInputStream
	 DataInputStream in = new DataInputStream(inputStream);
	 BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 LineNumberReader reader = new LineNumberReader(br);
	 
	 //populate array list by reading every third line
	 while ((strLine = reader.readLine()) != null){
		 if ((reader.getLineNumber()) % 3 == 0){
			 Line2List.add(strLine);
		 }
	 }

	 //Close the input stream
	 in.close();
	 
	 
	   }catch (Exception e){	//Catch exception if any
	 System.err.println("Error: " + e.getMessage());
	 }
	 
	return Line2List;
	 
	 }

@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);

	String satname = SatName().get(position);
	String tleline1 = Line1().get(position);
	String tleline2 = Line2().get(position);

    Intent intent = new Intent (HundredBrightest.this, PosCalc.class);
	intent.putExtra("DataLine1", tleline1);
	intent.putExtra("DataLine2", tleline2);
	intent.putExtra("satname", satname);
	//launch activity PosCalc
	startActivity(intent);
}




   
}
